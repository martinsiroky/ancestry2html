// Last revision 2.3.08
package cz.sirsi.ancestry.core.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.Warnings;
import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.core.data.AdditionalFile;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.data.Partnership;
import cz.sirsi.ancestry.core.data.Person;
import cz.sirsi.ancestry.core.data.Place;
import cz.sirsi.ancestry.core.data.Statistics;
import cz.sirsi.ancestry.core.data.comparator.AdditionalFileComparator;
import cz.sirsi.ancestry.core.data.comparator.PersonComparator;
import cz.sirsi.ancestry.core.data.comparator.PlacesComparator;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.freemarker.MessageMethod;
import cz.sirsi.ancestry.core.freemarker.UtilsMethods;
import cz.sirsi.ancestry.core.tools.MyCalendar;
import cz.sirsi.ancestry.core.tools.Tools;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

/**
 * Html generator methods
 * 
 * @author msiroky
 */
public class HtmlGenerator implements Constants {

	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(HtmlGenerator.class);

	/**
	 * Map that stores data needed for template processing
	 */
	private Map<String, Object> freemarkerMap = new HashMap<String, Object>();

	/**
	 * Generates page with list of credentials
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generateCredentialsListPage(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status
				.setProcessStatus(HtmlGenerator.class, ".generateCredentialsListPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		List<AdditionalFile> credentialsList = new ArrayList<AdditionalFile>();
		credentialsList.addAll(tree.getCredentialsMap().values());

		Collections.sort(credentialsList, new AdditionalFileComparator());

		this.freemarkerMap.put(Constants.FTL_LIST_CREDENTIALS, credentialsList);

		this.freemarkerMap.put("fileName", config.getCredentialsListFileName(null));

		processTemplate("credentials.htm", config.getCredentialsListFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(),
				config.getOutputDirectory());
	}

	/**
	 * Generates all credentials pages
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generateCredentialsPages(Tree tree) throws AppException {
		Config config = Config.getInstance();

		int counter = 0;
		for (AdditionalFile credential : tree.getCredentialsMap().values()) {

			if (!credential.isVisible()) {
				continue;
			}

			counter++;
			Main.status.setProcessStatus(HtmlGenerator.class, ".generateCredentialsPages-" + MessagesTools.getWebMessages().getCurrentLanguage()
					+ counter);

			this.freemarkerMap.put(FTL_CREDENTIAL, credential);

			this.freemarkerMap.put("fileName", config.getCredentialFileName(credential.getId(), null));

			processTemplate("credential.htm", config.getCredentialFileName(credential.getId(), config.getFileNamePrefix()), config
					.getFreemarkerConfiguration(), config.getOutputDirectory());

			try {
				File sourceFile = null;
				if (!config.isProjectXml()) {
					sourceFile = new File(new File(config.getDataFolder(), "dokumenty"), "dokument" + credential.getId() + ".jpg");
				} else {
					sourceFile = new File(new File(config.getProjectFile().getParent(), "documents"), "document" + credential.getId() + ".jpg");
				}

				if (credential.getExternalFileName() != null) {
					sourceFile = new File(credential.getExternalFileName());
				}
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "credential" + credential.getId() + ".jpg"), config
						.getMaxSizePhotoX(), config.getMaxSizePhotoY());
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "minicredential" + credential.getId() + ".jpg"), config
						.getMaxSizeMiniPhotoX(), config.getMaxSizeMiniPhotoY());
			} catch (Throwable t) {
				String message = "The file for credential with id=" + credential.getId() + " can not be copied.";
				log.error(message, t);
				Warnings.getInstance().addWarning(message);
			}
		}
	}

	/**
	 * Generates page with list of documents
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generateDocumentsListPage(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generateDocumentsListPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		List<AdditionalFile> documentsList = new ArrayList<AdditionalFile>();
		documentsList.addAll(tree.getDocumentsMap().values());

		Collections.sort(documentsList, new AdditionalFileComparator());

		this.freemarkerMap.put(Constants.FTL_LIST_DOCUMENTS, documentsList);

		this.freemarkerMap.put("fileName", config.getDocumentsListFileName(null));

		processTemplate("documents.htm", config.getDocumentsListFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(),
				config.getOutputDirectory());
	}

	/**
	 * Generates all document pages
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generateDocumentsPages(Tree tree) throws AppException {
		Config config = Config.getInstance();

		int counter = 0;
		for (AdditionalFile document : tree.getDocumentsMap().values()) {

			if (!document.isVisible()) {
				continue;
			}

			counter++;
			Main.status.setProcessStatus(HtmlGenerator.class, ".generateDocumentsPages-" + MessagesTools.getWebMessages().getCurrentLanguage()
					+ counter);

			this.freemarkerMap.put(FTL_DOCUMENT, document);

			this.freemarkerMap.put("fileName", config.getDocumentFileName(document.getId(), null));

			processTemplate("document.htm", config.getDocumentFileName(document.getId(), config.getFileNamePrefix()), config
					.getFreemarkerConfiguration(), config.getOutputDirectory());

			try {
				File sourceFile = null;
				if (!config.isProjectXml()) {
					sourceFile = new File(new File(config.getDataFolder(), "soubory"), "soubor" + document.getId() + "." + document.getExtension());
				} else {
					sourceFile = new File(new File(config.getProjectFile().getParent(), "files"), "file" + document.getId() + "."
							+ document.getExtension());
				}

				if (document.getExternalFileName() != null) {
					sourceFile = new File(document.getExternalFileName());
				}
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "document" + document.getId() + "." + document.getExtension()));
			} catch (Throwable t) {
				String message = "The file for document with id=" + document.getId() + " can not be copied.";
				log.error(message, t);
				Warnings.getInstance().addWarning(message);
			}
		}
	}

	/**
	 * Generates index page
	 * 
	 * @param tree Object that stores all people data
	 * @throws AppException In case generating page failed
	 */
	public void generateIndexPage(Tree tree) throws AppException {
		Main.status.setProcessStatus(HtmlGenerator.class, ".generateIndexPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		Config config = Config.getInstance();
		Map<String, Object> globalMap = new HashMap<String, Object>();
		this.freemarkerMap.put(FTL_GLOBAL, globalMap);

		Global global = Global.getInstance();
		UtilsMethods utils = UtilsMethods.getInstance();

		globalMap.put("title", global.getTitle());
		globalMap.put("author", global.getAuthor());
		globalMap.put("profile", MessagesTools.getGuiMessages().getMessage(global.getProfileName(), global.getProfileName(), null, false));
		globalMap.put("template", global.getTemplateName());
		globalMap.put("additionalInfo", global.getAdditionalInfo());
		globalMap.put("allowedGroups", config.isShowInfoAllowed() ? config.getGroupsOnly() : new ArrayList<String>());
		globalMap.put("hiddenGroups", config.isShowInfoBlocked() ? config.getGroupsHide() : new ArrayList<String>());
		globalMap.put("created", global.getCreated());
		globalMap.put("cooperated", global.getCooperated());
		globalMap.put("rodUser1", global.getRodUser1());
		globalMap.put("rodUser2", global.getRodUser2());
		globalMap.put("rodUser3", global.getRodUser3());
		globalMap.put("rodUser4", global.getRodUser4());
		globalMap.put("rodUser5", global.getRodUser5());
		globalMap.put("today", new MyCalendar().format());
		globalMap.put("buildTime", Tools.getBuild());
		globalMap.put("generatorVersion", Tools.getVersion());
		globalMap.put("ancestryVersion", tree.getAncestryVersion());

		this.freemarkerMap.put("languages", Config.getInstance().getLanguages());

		this.freemarkerMap.put("msg", new MessageMethod());

		this.freemarkerMap.put("utils", utils);

		this.freemarkerMap.put("config", config);

		this.freemarkerMap.put("fileName", config.getIndexFileName(null));

		prepareMyConfigMap();

		processTemplate("index.htm", config.getIndexFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());
	}

	/**
	 * Loads properties specific for template and put it into freemarker map for kye="my"
	 * 
	 * @throws AppException In case loading properties failed
	 */
	private void prepareMyConfigMap() throws AppException {
		Map<String, Object> myMap = new HashMap<String, Object>();
		this.freemarkerMap.put("my", myMap);

		if (Config.getInstance().getTemplatePropertiesFile() != null) {
			Properties properties = new Properties();
			try {
				properties.load(new InputStreamReader(new FileInputStream(Config.getInstance().getTemplatePropertiesFile()), Config.getInstance()
						.getPropertiesEncoding()));
			} catch (IOException e) {
				log.error("Preparing of myConfig map failed", e);
				throw new AppException("Preparing of myConfig map failed", e);
			}

			for (Object key : properties.keySet()) {
				myMap.put((String) key, properties.get(key));
			}
		}
	}

	/**
	 * Generates javascript file used on pages
	 * 
	 * @throws AppException In case generating failed
	 */
	public void generateJavascript() throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generateJavascript-" + MessagesTools.getWebMessages().getCurrentLanguage());

		this.freemarkerMap.put("fileName", config.getJavascriptFileName(null));

		processTemplate("ancestry.js", config.getJavascriptFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());
	}

	/**
	 * Generates page with list of all people
	 * 
	 * @param tree Object that stores all people data
	 * @throws AppException In case generating page failed
	 */
	public void generateListPage(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generateListPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		List<Person> peopleList = new ArrayList<Person>();
		for (Person person : tree.getPeopleMap().values()) {
			if (!person.isHidden()) {
				peopleList.add(person);
			}
		}

		Collections.sort(peopleList, new PersonComparator(PersonComparator.SECOND, PersonComparator.FIRST));
		this.freemarkerMap.put(Constants.FTL_LIST_PEOPLE, peopleList);

		List<Partnership> partnershipList = new ArrayList<Partnership>();
		for (Partnership partnership : tree.getPartnershipMap().values()) {
			if (!partnership.getMan().isHidden() && !partnership.getWoman().isHidden()) {
				partnershipList.add(partnership);
			}
		}
		this.freemarkerMap.put(Constants.FTL_LIST_PARTNERSHIP, partnershipList);

		this.freemarkerMap.put("firstLetters", tree.getFirstLetters());

		this.freemarkerMap.put("fileName", config.getListFileName(null));

		processTemplate("list.htm", config.getListFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());

		// TODO uncomment when table template will be done
		// this.freemarkerMap.put("fileName", config.getTableFileName(null));
		//
		// processTemplate("table.htm", config.getTableFileName(config.getFileNamePrefix()), config
		// .getFreemarkerConfiguration(), config.getOutputDirectory());
	}

	/**
	 * Generates page with list of all people sorted hierarchically
	 * 
	 * @param tree Object that stores all people data
	 * @throws AppException In case generating page failed
	 */
	public void generateListPageHierarchically(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generateListPageHierarchically-"
				+ MessagesTools.getWebMessages().getCurrentLanguage());

		List<Person> peopleList = new ArrayList<Person>();
		for (Person person : tree.getPeopleMap().values()) {
			if (!person.isHidden()) {
				peopleList.add(person);
			}
		}

		Collections.sort(peopleList, new PersonComparator(-PersonComparator.HIERARCHY, PersonComparator.SECOND, PersonComparator.FIRST));
		this.freemarkerMap.put(FTL_LIST_PEOPLE, peopleList);
		this.freemarkerMap.put(FTL_MIN_HIERARCHY, tree.getMinHierarchy());
		this.freemarkerMap.put(FTL_MAX_HIERARCHY, tree.getMaxHierarchy());

		this.freemarkerMap.put("fileName", config.getListHierarchyFileName(null));

		processTemplate("listHierarchy.htm", config.getListHierarchyFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(),
				config.getOutputDirectory());
	}

	/**
	 * Generates all partnership pages
	 * 
	 * @param tree Object that stores all people data
	 * @throws AppException In case generating page failed
	 */
	public void generatePartnershipPages(Tree tree) throws AppException {
		Config config = Config.getInstance();

		int counter = 0;
		for (Partnership partnership : tree.getPartnershipMap().values()) {
			if (partnership.getMan().isHidden() || partnership.getWoman().isHidden()) {
				continue;
			}

			if (counter++ % 20 == 0) {
				Main.status.setProcessStatus(HtmlGenerator.class, ".generatePartnershipPages-"
						+ MessagesTools.getWebMessages().getCurrentLanguage() + counter);
			}

			this.freemarkerMap.put(FTL_PARTNERSHIP, partnership);

			int id = partnership.getId();
			this.freemarkerMap.put("fileName", config.getPartnersFile(id, null));

			processTemplate("partners.htm", config.getPartnersFile(id, config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
					.getOutputDirectory());
		}
	}

	/**
	 * Generates all personal pages
	 * 
	 * @param tree Object that stores all people data
	 * @throws AppException In case generating pages failed
	 */
	public void generatePersonalPages(Tree tree) throws AppException {
		Config config = Config.getInstance();
		int counter = 0;
		for (Person person : tree.getPeopleMap().values()) {
			if (person.isHidden()) {
				continue;
			}

			if (counter++ % 10 == 0) {
				Main.status.setProcessStatus(HtmlGenerator.class, ".generatePersonalPages-" + MessagesTools.getWebMessages().getCurrentLanguage()
						+ counter);
			}

			this.freemarkerMap.put(FTL_CURRENT_PERSON, person);
			Person mother = person.getMother();

			this.freemarkerMap.put(FTL_MOTHER_PERSON, mother != null ? mother : Constants.EMPTY_WOMAN_PERSON);
			Person father = person.getFather();
			this.freemarkerMap.put(FTL_FATHER_PERSON, father != null ? father : Constants.EMPTY_MAN_PERSON);
			Person motherOfFather = father != null ? father.getMother() : null;
			this.freemarkerMap.put(FTL_MOTHER_OF_FATHER_PERSON, motherOfFather != null ? motherOfFather : Constants.EMPTY_WOMAN_PERSON);
			Person fatherOfFather = father != null ? father.getFather() : null;
			this.freemarkerMap.put(FTL_FATHER_OF_FATHER_PERSON, fatherOfFather != null ? fatherOfFather : Constants.EMPTY_MAN_PERSON);
			Person motherOfMother = mother != null ? mother.getMother() : null;
			this.freemarkerMap.put(FTL_MOTHER_OF_MOTHER_PERSON, motherOfMother != null ? motherOfMother : Constants.EMPTY_WOMAN_PERSON);
			Person fatherOfMother = mother != null ? mother.getFather() : null;
			this.freemarkerMap.put(FTL_FATHER_OF_MOTHER_PERSON, fatherOfMother != null ? fatherOfMother : Constants.EMPTY_MAN_PERSON);

			this.freemarkerMap.put(FTL_CURRENT_PERSON, person);

			this.freemarkerMap.put("fileName", config.getPersonalFile(person.getId(), null));

			processTemplate("person.htm", config.getPersonalFile(person.getId(), config.getFileNamePrefix()),
					config.getFreemarkerConfiguration(), config.getOutputDirectory());

			if (!person.isHead()) {
				continue;
			}

			try {
				File sourceFile = null;
				if (!config.isProjectXml()) {
					sourceFile = new File(new File(config.getDataFolder(), "hlavy"), "hlava" + person.getId() + ".jpg");
				} else {
					sourceFile = new File(new File(config.getProjectFile().getParent(), "heads"), "head" + person.getId() + ".jpg");
				}

				if (person.getHeadFile() != null) {
					sourceFile = new File(person.getHeadFile());
				}
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "hlava" + person.getId() + ".jpg"), config.getMaxSizeHeadX(), config
						.getMaxSizeHeadY());
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "minihlava" + person.getId() + ".jpg"), config.getMaxSizeMiniHeadX(),
						config.getMaxSizeMiniHeadY());

			} catch (Throwable t) {
				String message = "The head photo for person with id=" + person.getId() + " can not be copied.";
				log.error(message, t);
				Warnings.getInstance().addWarning(message);
			}
		}
	}

	/**
	 * Generates all photo pages
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generatePhotoPages(Tree tree) throws AppException {
		Config config = Config.getInstance();

		int counter = 0;
		for (AdditionalFile photo : tree.getPhotosMap().values()) {
			if (!photo.isVisible()) {
				continue;
			}

			counter++;
			Main.status.setProcessStatus(HtmlGenerator.class, ".generatePhotoPages-" + MessagesTools.getWebMessages().getCurrentLanguage()
					+ counter);

			this.freemarkerMap.put(FTL_PHOTO, photo);

			this.freemarkerMap.put("fileName", config.getPhotoFileName(photo.getId(), null));

			processTemplate("photo.htm", config.getPhotoFileName(photo.getId(), config.getFileNamePrefix()), config.getFreemarkerConfiguration(),
					config.getOutputDirectory());

			try {
				File sourceFile = null;
				if (!config.isProjectXml()) {
					sourceFile = new File(new File(config.getDataFolder(), "foto"), "foto" + photo.getId() + ".jpg");
				} else {
					sourceFile = new File(new File(config.getProjectFile().getParent(), "photos"), "photo" + photo.getId() + ".jpg");
				}

				if (photo.getExternalFileName() != null) {
					sourceFile = new File(photo.getExternalFileName());
				}
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "foto" + photo.getId() + ".jpg"), config.getMaxSizePhotoX(), config
						.getMaxSizePhotoY());
				Tools.copyFile(sourceFile, new File(config.getOutputImgDir(), "minifoto" + photo.getId() + ".jpg"), config.getMaxSizeMiniPhotoX(),
						config.getMaxSizeMiniPhotoY());
			} catch (Throwable t) {
				String message = "The image for photo with id=" + photo.getId() + " can not be copied.";
				log.error(message, t);
				Warnings.getInstance().addWarning(message);
			}
		}
	}

	/**
	 * Generates page with list of photos
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generatePhotosListPage(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generatePhotosListPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		List<AdditionalFile> photosList = new ArrayList<AdditionalFile>();
		photosList.addAll(tree.getPhotosMap().values());

		Collections.sort(photosList, new AdditionalFileComparator());

		this.freemarkerMap.put(Constants.FTL_LIST_PHOTOS, photosList);

		this.freemarkerMap.put("fileName", config.getPhotosListFileName(null));

		processTemplate("photos.htm", config.getPhotosListFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());
	}

	/**
	 * Generates list of places page
	 * 
	 * @param tree Stores all tree data
	 * @throws AppException In case generating failed
	 */
	public void generatePlacesPage(Tree tree) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generatePlacesPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		List<Place> places = new ArrayList<Place>();

		for (Place place : tree.getPlacesList()) {
			final Person person = place.getPerson();
			if (!person.isHidden()) {
				Place placeToAdd = new Place(place.getTown(), place.getPerson(), 0);
				if (place.isBirthPlace() && !config.isHideBirthData(person.isLive())) {
					placeToAdd.addType(Place.BIRTH);
				}
				if (place.isDeathPlace() && !config.isHideDeathData()) {
					placeToAdd.addType(Place.DEATH);
				}
				if (place.isBurialPlace() && !config.isHideDeathData()) {
					placeToAdd.addType(Place.BURIAL);
				}
				if (place.isLivePlace() && !config.isHideLivePlace(person.isLive())) {
					placeToAdd.addType(Place.LIVE);
				}
				
				if (placeToAdd.isBirthPlace() || placeToAdd.isBurialPlace() || placeToAdd.isDeathPlace() || placeToAdd.isLivePlace()) {
					places.add(placeToAdd);
				}
			}
		}

		Collections.sort(places, new PlacesComparator());
		this.freemarkerMap.put(Constants.FTL_LIST_PLACES, places);

		this.freemarkerMap.put("fileName", config.getPlacesListFileName(null));

		processTemplate("places.htm", config.getPlacesListFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());
	}

	/**
	 * Generates statistics page
	 * 
	 * @param statistics Object that stores statistics
	 * @throws AppException In case generating failed
	 */
	public void generateStatisticPage(Statistics statistics) throws AppException {
		Config config = Config.getInstance();

		Main.status.setProcessStatus(HtmlGenerator.class, ".generateStatisticPage-" + MessagesTools.getWebMessages().getCurrentLanguage());

		this.freemarkerMap.put(FTL_STATISTIC, statistics);

		this.freemarkerMap.put("fileName", config.getStatisticFileName(null));

		processTemplate("stats.htm", config.getStatisticFileName(config.getFileNamePrefix()), config.getFreemarkerConfiguration(), config
				.getOutputDirectory());
	}

	/**
	 * Initializes freemarker
	 * 
	 * @throws AppException In case initialization failed
	 */
	public void initializeFreemarker() throws AppException {
		try {
			Config.getInstance().getFreemarkerConfiguration().setDirectoryForTemplateLoading(Config.getInstance().getInputTemplateDirectory());
			Config.getInstance().getFreemarkerConfiguration().setObjectWrapper(new DefaultObjectWrapper());
		} catch (IOException e) {
			log.error("Error getting template folder", e);
			throw new AppException("Error getting template folder", e);
		}

		Main.status.printOutputMessage("progress.preparingOutputDone");
	}

	/**
	 * Prepares directories for output
	 */
	public void prepareOutputDirectories() {

		if (!Config.getInstance().getOutputDirectory().exists()) {
			log.debug("Create output directory");
			Config.getInstance().getOutputDirectory().mkdirs();
		} else {
			// TODO overwriting and cleaning
			/*
			 * for (File file : Config.getInstance().getOutputDirectory().listFiles( new
			 * MyFileFilter(Config.getInstance().getPersonalFileName().replaceAll("\\{id\\}", "\\\\d+")))) {
			 * 
			 * file.delete(); }
			 */
		}

		if (!Config.getInstance().getOutputImgDir().exists()) {
			log.debug("Create image directory");
			Config.getInstance().getOutputImgDir().mkdirs();
		} else {
			// TODO overwriting and cleaning
			/*
			 * for (File file : Config.getInstance().getOutputImgDir().listFiles()) { file.delete(); }
			 */
		}

		if (!Config.getInstance().getOutputCssDir().exists()) {
			log.debug("Create css directory");
			Config.getInstance().getOutputCssDir().mkdirs();
		} else {
			// TODO overwriting and cleaning
			/*
			 * for (File file : Config.getInstance().getOutputCssDir().listFiles()) { file.delete(); }
			 */
		}
	}

	/**
	 * Processes css files and copy them to the css output directory
	 * 
	 * @throws AppException In case generating css failed
	 */
	public void processCssFiles() throws AppException {
		Main.status.setProcessStatus(HtmlGenerator.class, ".processCssFiles");

		try {
			Configuration freemarkerCssConfiguration = new Configuration();
			freemarkerCssConfiguration.setDirectoryForTemplateLoading(Config.getInstance().getInputCssDir());
			freemarkerCssConfiguration.setObjectWrapper(new DefaultObjectWrapper());

			for (File cssFile : (Config.getInstance().getInputCssDir()).listFiles()) {
				if (!cssFile.getName().startsWith(".")) {
					processTemplate(cssFile.getName(), cssFile.getName(), freemarkerCssConfiguration, Config.getInstance().getOutputCssDir());
				}
			}
		} catch (IOException e) {
			log.error("Processing css files failed", e);
			throw new AppException("Processing css files failed", e);
		}
	}

	/**
	 * Copies images folders from template to output directory
	 * 
	 * @throws AppException In case copying failed
	 */
	public void processImages() throws AppException {
		Main.status.setProcessStatus(HtmlGenerator.class, ".processImages");

		try {
			Tools.copyDirectory(Config.getInstance().getInputImgDir(), Config.getInstance().getOutputImgDir());
		} catch (IOException e) {
			log.error("Copying img directory failed", e);
			throw new AppException("Copying img directory failed", e);
		}
	}

	/**
	 * Copies static javascript from template to output directory
	 * 
	 * @throws AppException In case copying failed
	 */
	public void processJavascripts() throws AppException {
		Main.status.setProcessStatus(HtmlGenerator.class, ".processJavascripts");

		try {
			Tools.copyDirectory(Config.getInstance().getInputJsDir(), Config.getInstance().getOutputJsDir());
		} catch (IOException e) {
			log.error("Copying img directory failed", e);
			throw new AppException("Copying img directory failed", e);
		}
	}

	/**
	 * Generates html page from given template
	 * 
	 * @param filename Template file name
	 * @param output Output file name
	 * @param freemarkerConfig Freemarker configuration - used for loading templates
	 * @param outputDirectory Directory for output
	 * @throws AppException In case reading template failed
	 */
	public void processTemplate(String filename, String output, Configuration freemarkerConfig, File outputDirectory) throws AppException {
		Main.status.printOutputMessage("progress.generatingFile", output);
		log.info("Generating file: " + output);

		File outputDir = outputDirectory;

		File outputPath = new File(outputDir, output);
		outputDir = outputPath.getParentFile();

		if (!outputDir.exists()) {
			outputDir.mkdirs();
		}

		BufferedWriter writer = null;

		try {
			// TODO solve overwriting
			freemarkerConfig.setURLEscapingCharset("UTF-8");
			freemarkerConfig.setNumberFormat("####");

			Template temp = freemarkerConfig.getTemplate(filename, Config.getInstance().getTemplateEncoding() != null ? Config.getInstance()
					.getTemplateEncoding() : "UTF-8");

			writer = new BufferedWriter(Config.getInstance().getOutputEncoding() != null ? new OutputStreamWriter(
					new FileOutputStream(outputPath), Config.getInstance().getOutputEncoding()) : new FileWriter(outputPath));

			temp.process(this.freemarkerMap, writer);

			Main.status.printOutputMessage("progress.generatingFileDone", output);
			log.info("File " + output + " generated");
		} catch (Throwable t) {
			log.error("Error processing template file " + filename, t);
			throw new AppException("Error while processing template file " + filename, t);
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}
}
