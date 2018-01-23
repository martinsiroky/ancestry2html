// Last revision 29.4.08
package cz.sirsi.ancestry.core.tools;

import static cz.sirsi.ancestry.core.Constants.OPTION_HELP;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.CollationElementIterator;
import java.text.RuleBasedCollator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.twmacinta.util.MD5;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.main.Main;

/**
 * Tools methods for application
 * 
 * @author msiroky
 */
public class Tools {

	private static Properties filesMD5 = new Properties();

	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(Tools.class);

	/**
	 * Compare two representation of application version
	 * 
	 * @param first Version of first application
	 * @param second Version of second application
	 * @return negative number in case second version is newer, positive in case first version is newer, 0 in case both versions are the same
	 */
	public static int compareTwoVersions(String first, String second) {
		String[] firstVersion = first.split("\\.");
		String[] secondVersion = second.split("\\.");
		for (int i = 0; i < firstVersion.length && i < secondVersion.length; i++) {

			int comparation = firstVersion[i].compareTo(secondVersion[i]);
			if (comparation != 0) {
				return comparation;
			}
		}

		return firstVersion.length - secondVersion.length;
	}

	/**
	 * @param image Instance of image to resize
	 * @param targetLocation Location of target image
	 * @param maxWidth Max width of resized image
	 * @param maxHeight Max heigth of resized image
	 * @throws IOException In case writing image to file failed
	 */
	private static void copyAndResizeImage(BufferedImage image, File targetLocation, Integer maxWidth, Integer maxHeight) throws IOException {
		int width = image.getWidth();
		int height = image.getHeight();
		double ratio = ((double) width) / height;

		if (width > maxWidth) {
			width = maxWidth;
			height = (int) Math.round(width / ratio);
		}
		if (height > maxHeight) {
			height = maxHeight;
			width = (int) Math.round(height * ratio);
		}

		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D bufImageGraphics = newImage.createGraphics();

		bufImageGraphics.drawImage(image, 0, 0, width, height, null);

		ImageIO.write(newImage, "jpg", targetLocation);
	}

	/**
	 * Copies source directory to target directory
	 * 
	 * @param sourceLocation Source directory to copy from
	 * @param targetLocation Target directory to copy to
	 * @throws IOException In case copying failed
	 */
	@Deprecated
	// use FileUtils.copyDirectory with filter
	public static void copyDirectory(File sourceLocation, File targetLocation) throws IOException {
		log.info("Copying " + sourceLocation + " to " + targetLocation);

		if (sourceLocation.isDirectory()) {

			// TODO filter "." files and folders or something else
			if (!sourceLocation.getName().startsWith(".")) {

				if (!targetLocation.exists()) {
					targetLocation.mkdir();
				}

				String[] children = sourceLocation.list();
				for (String child : children) {
					copyDirectory(new File(sourceLocation, child), new File(targetLocation, child));
				}
			}
		} else {

			copyFile(sourceLocation, targetLocation);
		}

		log.info("Copying done!");
	}

	/**
	 * Copies source file to target
	 * 
	 * @param sourceLocation Source file
	 * @param targetLocation Target file
	 * @throws FileNotFoundException In case source file is not found
	 * @throws IOException In case reading or writing file failed
	 */
	public static void copyFile(File sourceLocation, File targetLocation) throws FileNotFoundException, IOException {
		copyFile(sourceLocation, targetLocation, null, null);
	}

	/**
	 * Copies source file to target
	 * 
	 * @param sourceLocation Source file
	 * @param targetLocation Target file
	 * @param maxWidth Max width of picture (or null if the file is not picture)
	 * @param maxHeight Max height of picture (or null if the file is not picture)
	 * @throws FileNotFoundException In case source file is not found
	 * @throws IOException In case reading or writing file failed
	 */
	public static void copyFile(File sourceLocation, File targetLocation, Integer maxWidth, Integer maxHeight) throws FileNotFoundException,
			IOException {

		// TODO overwriting
		String md5 = MD5.asHex(MD5.getHash(sourceLocation)) + ";" + maxWidth + ";" + maxHeight;
		if (targetLocation.exists() && md5.equals(filesMD5.get(targetLocation.getAbsolutePath()))) {
			return;
		}

		filesMD5.put(targetLocation.getAbsolutePath(), md5);

		BufferedImage image = null;

		if (maxHeight != null && maxWidth != null) {
			try {
				image = ImageIO.read(sourceLocation);
			} catch (IOException ex) {
				// nothing, copy whole file
			}
		}

		if (image != null) {
			copyAndResizeImage(image, targetLocation, maxWidth, maxHeight);
		} else {
			FileUtils.copyFile(sourceLocation, targetLocation);
		}
	}

	/**
	 * Transform relative path to absolute if it is needed
	 * 
	 * @param basePath Base path (it is placed before relative path)
	 * @param file Absolute or relative path, in case it is relative the basePath will be added before
	 * @return Path
	 * @throws AppException In case getting absolute path from relative failed
	 */
	public static File getAbsolutePath(File basePath, String file) throws AppException {
		try {
			if (new File(file).isAbsolute()) {
				return new File(file);
			}

			return new File(basePath, file).getCanonicalFile();
		} catch (IOException e) {
			throw new AppException("Can not convert relative path to absolute.", e);
		}
	}

	/**
	 * @return Base path of application
	 * @throws AppException In case getting current directory failed
	 */
	public static File getBasePath() throws AppException {
		try {
			return new File(".").getCanonicalFile();
		} catch (IOException e) {
			throw new AppException("Can not get working directory.", e);
		}
	}

	/**
	 * @return Build time of jar
	 */
	public static String getBuild() {
		String build = "?";

		try {
			File jarFile = new File(Tools.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			build = new SimpleDateFormat("yyMMdd").format(new Date(jarFile.lastModified()));
		} catch (Throwable t) {
			log.warn("Can not get build time.", t);
		}

		return build;
	}

	/**
	 * @param file File to get its canonical form
	 * @return Absolute path to properties file
	 * @throws AppException In case getting canonical form of file failed
	 */
	public static File getCanonicalFile(File file) throws AppException {
		try {
			File confFileNormalized = file.getCanonicalFile();

			return confFileNormalized;
		} catch (IOException e) {
			throw new AppException("Getting file failed - " + file.getName(), e);
		}
	}

	/**
	 * Returns first letter of word (may be two or more chars - due to locale)
	 * 
	 * @param word Word
	 * @return First letter of the word
	 */
	public static String getFirstLetter(String word) {
		RuleBasedCollator ruleBasedCollator = (RuleBasedCollator) Config.getInstance().getCollator();
		int end = getLocaleCharacterEnd(word, 0, ruleBasedCollator);
		return word.substring(0, end);
	}

	/**
	 * @param lang Language
	 * @param baseFileName File name with "${lang}" property to replace
	 * @return File name for given language.
	 */
	public static String getLanguageFileName(String lang, String baseFileName) {
		if (Config.getInstance().getDefaultLanguage().equals(lang)) {
			return baseFileName.replace("${lang}", "");
		}
		return baseFileName.replace("${lang}", lang);
	}

	/**
	 * Gets end of letter for given locale (e.g. 'ch' is one letter in czech) Note: get from google
	 * 
	 * @param string Word
	 * @param start Start of letter
	 * @param collator Collator (stores rules for locale)
	 * @return End position of letter
	 */
	private static int getLocaleCharacterEnd(String string, int start, RuleBasedCollator collator) {
		int lastPosition = start;
		CollationElementIterator it = collator.getCollationElementIterator(string.substring(start, string.length()));
		it.next(); // discard first collation element
		int primary;

		// accumulate characters until we get to a non-zero primary

		do {
			lastPosition = it.getOffset();
			int ce = it.next();
			if (ce == CollationElementIterator.NULLORDER) {
				break;
			}
			primary = CollationElementIterator.primaryOrder(ce);
		} while (primary == 0);
		return lastPosition;
	}

	/**
	 * @return Version of application
	 */
	public static String getVersion() {
		Properties props = new Properties();
		try {

			InputStream is = Tools.class.getResourceAsStream("/META-INF/maven/cz.sirsi.Ancestry2html/Ancestry2html/pom.properties");

			if (is != null) {
				props.load(is);
				return props.getProperty("version");
			}

			log.warn("Application version only can be determined from within the JAR file.");
		} catch (IOException e) {
			log.warn("Application version can not be get from the JAR file.");
		}

		return "devel";
	}

	/**
	 * Checks if some group is contained in both lists
	 * 
	 * @param groups First group
	 * @param groups2 Second group
	 * @return True in case some group is contained in both lists
	 */
	public static boolean isInGroup(List<String> groups, List<String> groups2) {
		for (String g : groups) {
			if (groups2.contains(g)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Loads MD5 information from file
	 * 
	 * @param file Properties file that stores md5 sums of files that were previously used
	 */
	public static void loadFilesMD5(File file) {
		try {
			filesMD5.load(new FileInputStream(file));
		} catch (Exception e) {
			// nothing (maybe file don't exist - first run of application or was deleted)
		}
	}

	/**
	 * Parses the command line from given arguments.
	 * 
	 * @param opts Options for the parser.
	 * @param args Arguments to parse command line from.
	 * @return Parsed CommandLine object
	 */
	@Deprecated
	public static CommandLine parseArgs(Option[] opts, String[] args) {
		Options options = new Options();
		log.debug("Parsing arguments of cmd...");

		for (String arg : args) {
			log.debug(arg);
		}

		options.addOption(OPTION_HELP);

		for (Option opt : opts) {
			options.addOption(opt);
		}

		CommandLine cmd = null;
		CommandLineParser parser = new PosixParser();

		try {
			cmd = parser.parse(options, args);

			if (cmd.hasOption(OPTION_HELP.getOpt())) {
				printHelp(options);
				System.exit(0);
			}

		} catch (ParseException ex) {
			Main.status.printOutputMessage("progress.errorParsingParameters");
			printHelp(options);
			log.error("Parsing parameters failed.", ex);
			System.exit(1);
		}

		log.debug("Parsing arguments of cmd done");

		return cmd;
	}

	/**
	 * Parse int from string. In case parsing failed then returns default value.
	 * 
	 * @param text String to parse int from
	 * @param defaultValue DEfault value to return in case parsing failed
	 * @return Parsed int or default value
	 */
	public static int parseInt(String text, int defaultValue) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	/**
	 * Gets height from 2 elements array with width and height (as strings)
	 * 
	 * @param widthAndHeight 2 elements array with height and width
	 * @return Parsed height as int
	 */
	public static int pictureHeight(String[] widthAndHeight) {
		try {
			return Integer.parseInt(widthAndHeight[1]);
		} catch (NumberFormatException ex) {
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * Gets width from 2 elements array with width and height (as strings)
	 * 
	 * @param widthAndHeight 2 elements array with height and width
	 * @return Parsed width as int
	 */
	public static int pictureWidth(String[] widthAndHeight) {
		try {
			return Integer.parseInt(widthAndHeight[0]);
		} catch (NumberFormatException ex) {
			return Integer.MAX_VALUE;
		}
	}

	/**
	 * Prints help to output
	 * 
	 * @param options Options for commandline use
	 */
	@Deprecated
	private static void printHelp(Options options) {
		new HelpFormatter().printHelp("Options: ", "\n", options, "\n", true);
	}

	/**
	 * Removes trailing and leading quotas from text
	 * 
	 * @param text Text to "trim"
	 * @return Text without quotas
	 */
	public static String removeQuotas(String text) {
		String textWithoutQuotas = text;
		if (textWithoutQuotas.startsWith("\"") && textWithoutQuotas.endsWith("\"")) {
			textWithoutQuotas = textWithoutQuotas.substring(1, textWithoutQuotas.length() - 1);
		}
		return textWithoutQuotas;
	}

	/**
	 * Stores MD5 information to file
	 * 
	 * @param file File
	 */
	public static void storeFilesMD5(File file) {
		try {
			filesMD5.store(new FileOutputStream(file), null);
		} catch (IOException e) {
			log.error("Can not store MD5-store file.", e);
		}
	}

	/**
	 * Transforms all file path separators to "/" (because html path can contain only "/" not "\")
	 * 
	 * @param url Url to transform
	 * @return Transformed url
	 */
	public static String transformSeparators(String url) {
		return url.replace(File.separator, "/");
	}

	/**
	 * Trims text, but return empty string in case source text is null
	 * 
	 * @param textToTrim Test to trim
	 * @return Trimmed text or empty strimg
	 */
	public static String trim(String textToTrim) {
		return textToTrim == null ? "" : textToTrim.trim();
	}
}
