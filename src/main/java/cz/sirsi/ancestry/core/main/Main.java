//Last revision 15.12.08
package cz.sirsi.ancestry.core.main;

import static cz.sirsi.ancestry.core.Constants.OPTION_CONFIG;
import static cz.sirsi.ancestry.core.Constants.OPTION_CSS_DIR;
import static cz.sirsi.ancestry.core.Constants.OPTION_EMPTY_PEOPLE;
import static cz.sirsi.ancestry.core.Constants.OPTION_FILENAME;
import static cz.sirsi.ancestry.core.Constants.OPTION_HTML_DIR;
import static cz.sirsi.ancestry.core.Constants.OPTION_IMGS_DIR;
import static cz.sirsi.ancestry.core.Constants.OPTION_TEMPLATES_DIR;
import static cz.sirsi.ancestry.core.Constants.OPTION_TEMPLATE_PROPERTIES;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.StatusListener;
import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.data.Statistics;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.load.rod.RodLoader;
import cz.sirsi.ancestry.core.load.rodx.RodxLoader;
import cz.sirsi.ancestry.core.tools.Tools;
import cz.sirsi.ancestry.core.tools.Unzip;

/**
 * Application for generating html pages for family tree
 * 
 * @author msiroky
 */
public class Main {

	private static Logger log = Logger.getLogger(Main.class);

	/**
	 * Instance of StatusListener
	 */
	public static StatusListener status;

	/**
	 * Generates all pages for given language
	 * 
	 * @param htmlGenerator Instance of initialized HtmlGenerator used for generating
	 * @param language Given language to generate files for
	 * @param statistics Initialized statistics object with computed statistics data
	 * @throws AppException In case generating failed for any reason
	 */
	private static void generateFilesForLanguage(HtmlGenerator htmlGenerator, String language, Statistics statistics)
			throws AppException {
		Config config = Config.getInstance();
		Tree tree = Tree.getInstance();

		MessagesTools.getWebMessages().setCurrentLanguage(language);
		config.setFileNamePrefix(language);

		List<Object> params = new ArrayList<Object>();
		params.add("locale");

		config.initLocaleDependentConfiguration(MessagesTools.getWebMessages().getMessage(params));

		Tree.getInstance().computeFirstLetters();

		htmlGenerator.generateIndexPage(tree);
		htmlGenerator.generateJavascript();
		htmlGenerator.generatePhotosListPage(tree);
		htmlGenerator.generateDocumentsListPage(tree);
		htmlGenerator.generateCredentialsListPage(tree);
		htmlGenerator.generateStatisticPage(statistics);
		htmlGenerator.generateListPage(tree);
		htmlGenerator.generateListPageHierarchically(tree);
		htmlGenerator.generatePartnershipPages(tree);
		htmlGenerator.generatePersonalPages(tree);
		htmlGenerator.generatePlacesPage(tree);
		htmlGenerator.generatePhotoPages(tree);
		htmlGenerator.generateDocumentsPages(tree);
		htmlGenerator.generateCredentialsPages(tree);
	}

	/**
	 * @throws AppException In case unzipping rodz or loading rod or rodx failed
	 */
	private static void loadProject() throws AppException {
		Config config = Config.getInstance();
		if (config.isProjectZipped()) {
			File extractDir = new File(System.getProperty("java.io.tmpdir"), "ancestry2htmlExtracted");
			Unzip.unzip(config.getProjectFile(), extractDir);
			config.setProjectFile(new File(extractDir, "project.rodx"));
			config.setExtractDir(extractDir);
		}

		if (config.isProjectXml()) {
			RodxLoader.getInstance().loadProject(config.getProjectFile());
		} else {
			RodLoader.getInstance().loadProject(config.getProjectFile());
		}
	}

	/**
	 * Main method called from external module (e.g. gui window)
	 * 
	 * @param args Arguments of main method
	 * @param properties Properties to set some properties from external module
	 * @param statusListener Object for processing status information e.g. into progress bar status
	 * @return Path to generated index.html or null in case generating failed
	 */
	public static String main(String[] args, Properties properties, StatusListener statusListener) {
		Main.status = statusListener;
		Config config = Config.getInstance();
		Tree tree = Tree.getInstance();
		tree.reset();
		Global.getInstance().reset();

		try {
			CommandLine cmd = Tools.parseArgs(new Option[] { OPTION_FILENAME, OPTION_HTML_DIR, OPTION_TEMPLATES_DIR,
					OPTION_EMPTY_PEOPLE, OPTION_CONFIG, OPTION_IMGS_DIR, OPTION_CSS_DIR, OPTION_TEMPLATE_PROPERTIES }, args);

			config.processConfiguration(cmd, properties);

			loadProject();

			Tools.loadFilesMD5(new File(config.getOutputDirectory(), ".lastGeneration.properties"));

			tree.computeAditionalData();

			MessagesTools.getWebMessages().initMessagesIfNeeded();

			Main.status.setProcessStatus(Main.class, ".computeStatistics");

			Statistics statistics = new Statistics();
			statistics.computeStatistics();

			Main.status.setProcessStatus(Main.class, ".initGenerator");

			HtmlGenerator htmlGenerator = new HtmlGenerator();

			htmlGenerator.prepareOutputDirectories();

			htmlGenerator.initializeFreemarker();

			for (String language : config.getLanguages()) {
				statistics.reset();
				generateFilesForLanguage(htmlGenerator, language, statistics);
			}

			htmlGenerator.processImages();
			htmlGenerator.processCssFiles();
			htmlGenerator.processJavascripts();

			Tools.storeFilesMD5(new File(config.getOutputDirectory(), ".lastGeneration.properties"));

			// returns index.html of generated pages (to automatic showing in browser)
			return new File(config.getOutputDirectory(), config.getIndexFileName(config.getFileNamePrefix()))
					.getAbsolutePath();
		} catch (AppException e) {
			Main.status.error(e.getMessage());
		} catch (Throwable t) {
			log.error("Unexpected error.", t);

			StringBuffer error = new StringBuffer();

			error.append(t.getMessage());

			for (StackTraceElement stackTraceElement : t.getStackTrace()) {
				error.append("\n");
				error.append(stackTraceElement.toString());
			}

			Main.status.error(error.toString());
		} finally {
			if (config.getExtractDir() != null) {
				try {
					FileUtils.deleteDirectory(config.getExtractDir());
				} catch (IOException e) {
					// TODO log and ignore
					e.printStackTrace();
				}
			}
		}

		return null;
	}
}
