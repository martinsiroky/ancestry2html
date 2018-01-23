// Last revision 29.4.08
package cz.sirsi.ancestry.core;

import java.io.File;

import org.apache.commons.cli.Option;

import cz.sirsi.ancestry.core.data.Person;

/**
 * Constants used in application
 * 
 * @author msiroky
 */
public interface Constants {
	/**
	 * Count of default privacy profiles (which can not be deleted)
	 */
	public static final int DEFAULT_PRIVACY_PROFILES_COUNT = 4;

	/**
	 * Person with all empty fields (used when person is null for replacing data)
	 */
	public static final Person EMPTY_MAN_PERSON = new Person(true);

	/**
	 * Person with all empty fields (used when person is null for replacing data)
	 */
	public static final Person EMPTY_WOMAN_PERSON = new Person(false);

	/**
	 * Key for storing credential
	 */
	public static final String FTL_CREDENTIAL = "cred";

	/**
	 * Key for storing current person
	 */
	public static final String FTL_CURRENT_PERSON = "c";

	/**
	 * Key for storing document
	 */
	public static final String FTL_DOCUMENT = "doc";

	/**
	 * Key for storing father of father
	 */
	public static final String FTL_FATHER_OF_FATHER_PERSON = "ff";

	/**
	 * Key for storing father of mother
	 */
	public static final String FTL_FATHER_OF_MOTHER_PERSON = "fm";

	/**
	 * Key for storing father
	 */
	public static final String FTL_FATHER_PERSON = "f";

	/**
	 * Key for storing global parameter
	 */
	public static final String FTL_GLOBAL = "g";

	/**
	 * Key for storing list of credentials
	 */
	public static final String FTL_LIST_CREDENTIALS = "credentialsList";

	/**
	 * Key for storing list of documents
	 */
	public static final String FTL_LIST_DOCUMENTS = "documentsList";

	/**
	 * Key for storing list of partnerships
	 */
	public static final String FTL_LIST_PARTNERSHIP = "partnershipList";

	/**
	 * Key for storing list of people
	 */
	public static final String FTL_LIST_PEOPLE = "peopleList";

	/**
	 * Key for storing list of photos
	 */
	public static final String FTL_LIST_PHOTOS = "photosList";

	/**
	 * Key for storing list of places
	 */
	public static final String FTL_LIST_PLACES = "placesList";

	/**
	 * Key for storing maximal hierarchy of person
	 */
	public static final String FTL_MAX_HIERARCHY = "maxHierarchy";

	/**
	 * Key for storing minimal hierarchy of person
	 */
	public static final String FTL_MIN_HIERARCHY = "minHierarchy";

	/**
	 * Key for storing mother of father
	 */
	public static final String FTL_MOTHER_OF_FATHER_PERSON = "mf";

	/**
	 * Key for storing mother of mother
	 */
	public static final String FTL_MOTHER_OF_MOTHER_PERSON = "mm";

	/**
	 * Key for storing mother
	 */
	public static final String FTL_MOTHER_PERSON = "m";

	/**
	 * Key for storing partnership
	 */
	public static final String FTL_PARTNERSHIP = "p";

	/**
	 * Key for storing photo
	 */
	public static final String FTL_PHOTO = "ph";

	/**
	 * Key for storing statistic data
	 */
	public static final String FTL_STATISTIC = "st";

	/**
	 * Argument for configuration file
	 */
	public static final Option OPTION_CONFIG = new Option("c", "config", true, "configuration file");

	/**
	 * Argument for css output directory
	 */
	public static final Option OPTION_CSS_DIR = new Option("s", "css", true,
			"Directory to generate css file(s). Corresponding 'outputCssDir' property in config file. MANDATORY.");

	/**
	 * Argument for showing empty person
	 */
	public static final Option OPTION_EMPTY_PEOPLE = new Option("e", "empty", false,
			"Shows empty people in personal page. Corresponding 'emptyPeople' property in config file. OPTIONAL - by default disabled.");

	/**
	 * Argument for input file name
	 */
	public static final Option OPTION_FILENAME = new Option("f", "file", true,
			"File to proccess. Corresponding 'file' property in config file. MANDATORY.");

	/**
	 * Arguments help option.
	 */
	public static final Option OPTION_HELP = new Option("h", "help", false, "prints this help message");

	/**
	 * Argument for html output directory
	 */
	public static final Option OPTION_HTML_DIR = new Option("o", "output", true,
			"Directory to generate html. Corresponding 'outputDir' property in config file. MANDATORY.");

	/**
	 * Argument for images output directory
	 */
	public static final Option OPTION_IMGS_DIR = new Option("i", "images", true,
			"Directory to generate images. Corresponding 'outputImgDir' property in config file. MANDATORY.");

	/**
	 * Argument for template properties file name
	 */
	public static final Option OPTION_TEMPLATE_PROPERTIES = new Option(
			"T",
			"templateProperties",
			true,
			"Properties file with variables which can be replaces in templates. Corresponding 'templatePropertiesFile' property in config file. OPTIONAL.");

	/**
	 * Argument for templates directory
	 */
	public static final Option OPTION_TEMPLATES_DIR = new Option("t", "templates", true,
			"Directory contained html templates. Corresponding 'templatesDir' property in config file. MANDATORY.");

	public static final String PRIVACY_PROFILES_FILE_NAME = ".." + File.separator + "etc" + File.separator + "privacyProfiles.properties";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_ADDRESS_SPLITTER = "addressSeparator";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_AUTOCLOSE = "autoclose";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_BIRTH_DEATH_SPLITTER = "birthDeathSplitter";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_BLOCK_HIDDEN = "blockHidden";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_CREDENTIAL_FILE_NAME = "credentialFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_CREDENTIALS_FILE_NAME = "credentialsFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_DEFAULT_LANGUAGE = "defaultLanguage";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_DOCUMENT_FILE_NAME = "documentFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_DOCUMENTS_FILE_NAME = "documentsFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_ENABLE_DEBUG = "enableDebug";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_FILE = "file";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_FIRST_NAME_FIRST = "firstNameFirst";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_FROM_TO_SPLITTER = "fromToSplitter";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_GROUPS_HIDE = "groupsHide";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_GROUPS_ONLY = "groupsOnly";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_GUI_LANGUAGE = "guiLanguage";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_ADDITIONAL_INFO = "hideAdditionalInfo";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_BIRTH_DATA = "hideBirthData";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_CONTACT = "hideContact";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_CREDENTIALS = "hideCredentials";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_DEATH_DATA = "hideDeathData";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_DOCUMENTS = "hideDocuments";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_EDUCATION = "hideEducation";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_HEADS = "hideHeads";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_JOBS = "hideJobs";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_LIVE_PLACES = "hideLivePlaces";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_MARRIAGE_DATA = "hideMarriageDate";

	/** The Constant PROPERTY_HIDE_MARRIAGE_PLACE. */
	public static final String PROPERTY_HIDE_MARRIAGE_PLACE = "hideMarriagePlace";

	/** The Constant PROPERTY_HIDE_NATIONALITY. */
	public static final String PROPERTY_HIDE_NATIONALITY = "hideNationality";

	/** The Constant PROPERTY_HIDE_PATH_TO_MAIN_PERSON. */
	public static final String PROPERTY_HIDE_PATH_TO_MAIN_PERSON = "hidePath";

	/** The Constant PROPERTY_HIDE_ADDITIONAL_INFO_ON_INDEX. */
	public static final String PROPERTY_HIDE_ADDITIONAL_INFO_ON_INDEX = "hideAdditionalInfoOnIndex";

	/** The Constant PROPERTY_SHOW_MAIN_PERSON. */
	public static final String PROPERTY_SHOW_MAIN_PERSON = "showMainPerson";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_PHOTOS = "hidePhotos";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_REGISTRY_INFO = "hideRegistryInfo";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_RELIGION = "hideReligion";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_USER_DEFINED = "hideUserDefined";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_HIDE_WEB = "hideWeb";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_CHECK_NEW_VERSION = "checkNewVersion";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_INDEX_FILE_NAME = "indexFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_INPUT_CSS_DIR = "inputCssDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_INPUT_IMG_DIR = "inputImgDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_INPUT_JS_DIR = "inputJsDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_JAVASCRIPT_FILE_NAME = "javascriptFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_LANGUAGES = "languages";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_LAST_CHECK = "lastCheckForNewVersion";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_LIST_FILE_NAME = "listFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_LIST_HIERARCHY_FILE_NAME = "listHierarchyFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAIN_PERSON_ID = "mainPersonId";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAPS = "maps";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAX_SIZE_HEAD = "maxSizeHead";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAX_SIZE_MINI_HEAD = "maxSizeMiniHead";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAX_SIZE_MINI_PHOTO = "maxSizeMiniPhoto";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_MAX_SIZE_PHOTO = "maxSizePhoto";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_OPEN_BROWSER = "openBrowser";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_OUTPUT_CSS_DIR = "outputCssDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_OUTPUT_DIR = "outputDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_OUTPUT_ENCODING = "outputEncoding";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_OUTPUT_IMG_DIR = "outputImgDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_PARTNERSHIP_FILE_NAME = "partnershipFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_PERSONAL_FILE_NAME = "personalFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_PHOTO_FILE_NAME = "photoFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_PHOTOS_FILE_NAME = "photosFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_PLACES_LIST_FILE_NAME = "placesListFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_BIRTH_IN_LISTS = "showBirthInLists";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_DEATH_IN_LISTS = "showDeathInLists";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_ID = "showId";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_ID_IN_LISTS = "showIdInLists";

	/** The Constant PROPERTY_SHOW_INFO_ALLOWED. */
	public static final String PROPERTY_SHOW_INFO_ALLOWED = "showInfoAllowed";

	/** The Constant PROPERTY_SHOW_INFO_BLOCKED. */
	public static final String PROPERTY_SHOW_INFO_BLOCKED = "showInfoBlocked";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_SHORT_DESC_IN_LISTS = "showShortDescInLists";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_SHOW_WEDDING_NAME_IN_LISTS = "showWeddingNameInLists";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_STATISTICS_FILE_NAME = "statisticsFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TABLE_FILE_NAME = "tableFileName";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TEMPLATE_ENCODING = "templateEncoding";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TEMPLATE_PROPERTIES_FILE = "templatePropertiesFile";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TEMPLATES_DIR = "templatesDir";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TO_PREFIX = "toPrefix";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TOWN_IN_ADDRESS = "townPositionInAddress";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_TOWN_IN_BIRTH = "townPositionInBirth";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_USE_BURIAL_PLACE = "useBurialPlace";

	/**
	 * see doc/properties-manual.txt for details
	 */
	public static final String PROPERTY_USE_MAIDEN_NAME = "useMaidenName";

	/**
	 * Tag for start of global block in rod file
	 */
	public static final String ROD_GLOBAL_TAG = "<obecne>";

	/**
	 * Tag for start of marriage block in rod file
	 */
	public static final String ROD_MARRIAGE_TAG = "<svatba>";

	/**
	 * Tag for start of person block in rod file
	 */
	public static final String ROD_PERSON_TAG = "<osoba>";

	public static final Person UNKNOWN_MAN_PERSON = new Person(true, -2);

	public static final Person UNKNOWN_WOMAN_PERSON = new Person(false, -2);
}
