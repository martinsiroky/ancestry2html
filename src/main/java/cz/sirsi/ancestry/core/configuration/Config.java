package cz.sirsi.ancestry.core.configuration;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.text.ParseException;
import java.text.RuleBasedCollator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.MyProperties;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.main.Main;
import cz.sirsi.ancestry.core.tools.Tools;
import freemarker.template.Configuration;

/**
 * Object that stores configuration of application
 * 
 * @author msiroky
 */
public class Config implements Constants {

	/**
	 * Singleton instance
	 */
	private static Config configInstance;

	/**
	 * Instance of logger
	 */
	private static Logger log = Logger.getLogger(Config.class);

	/**
	 * @return Singleton instance of Config
	 */
	public static Config getInstance() {
		if (configInstance != null) {
			return configInstance;
		}

		if (configInstance == null) {
			configInstance = new Config();
		}

		return configInstance;
	}

	private String addressSplitter;

	private File basePathOfPropertiesFile;

	private String birthDeathSplitter;

	private boolean blockHidden = false;

	private Collator collator = Collator.getInstance();

	private String credentialFileName;

	private String credentialsListFileName;

	private String defaultLanguage = "cz";

	private String documentFileName;

	private String documentsListFileName;

	private File extractDir = null;

	private String fileNamePrefix = "";

	private Configuration freemarkerConfiguration = new Configuration();

	private String fromToSplitter;

	private List<String> groupsHide = new ArrayList<String>();

	private List<String> groupsOnly = new ArrayList<String>();

	private String hideAdditionalInfo;

	private boolean hideAdditionalInfoOnIndex;

	private String hideBirthData;

	private String hideContact;

	private String hideCredentials;

	private String hideDeathData;

	private String hideDocuments;

	private String hideEducation;

	private String hideHeads;

	private String hideJobs;

	private String hideLivePlaces;

	private String hideMarriageDate;

	private String hideMarriagePlace;

	private String hideNationality;

	private boolean hidePathToMain;

	private String hidePhotos;

	private String hideRegistryInfo;

	private String hideReligion;

	private String hideUserDefined;

	private String hideWeb;

	private String indexFileName;

	private File inputCssDir;

	private File inputImgDir;

	private File inputJsDir;

	private File inputTemplateDirectory;

	private String javascriptFileName;

	private List<String> languages;

	private String listFileName;

	private String listHierarchyFileName;

	private Locale locale;

	private String mainPersonIdent = "0";

	private String mapUrl;

	private int maxSizeHeadX;

	private int maxSizeHeadY;

	private int maxSizeMiniHeadX;

	private int maxSizeMiniHeadY;

	private int maxSizeMiniPhotoX;

	private int maxSizeMiniPhotoY;

	private int maxSizePhotoX;

	private int maxSizePhotoY;

	private File outputCssDir;

	private File outputDirectory;

	private String outputEncoding;

	private File outputImgDir;

	private File outputJsDir;

	private String partnershipFileName;

	private String personalFileName;

	private String photoFileName;

	private String photosListFileName;

	private String placesListFileName;

	private File projectFile;

	private String propertiesEncoding;

	private boolean showBirthInLists = true;

	private boolean showDeathInLists = true;

	private boolean showId = false;

	private boolean showIdInLists = false;

	private boolean showInfoAllowed = true;

	private boolean showInfoBlocked = true;

	private boolean showMainPerson = true;

	private boolean showShortDescInLists = false;

	private boolean showWeddingNameInLists = false;

	private String statisticsFileName;

	private String tableFileName;

	private String templateEncoding;

	private File templatePropertiesFile;

	private String toPrefix;

	private int townPositionInAddress;

	private int townPositionInBirth;

	private boolean useBurialPlace = true;

	private boolean useMaidenName = true;

	protected Config() {
		// nothing
	}

	/**
	 * @return the addressSplitter
	 */
	public String getAddressSplitter() {
		return this.addressSplitter;
	}

	/**
	 * @return the birthDeathSplitter
	 */
	public String getBirthDeathSplitter() {
		return this.birthDeathSplitter;
	}

	/**
	 * @return the collator
	 */
	public Collator getCollator() {
		return this.collator;
	}

	/**
	 * @param id Id of credential
	 * @param lang Currently selected language
	 * @return Name of file for given credential id
	 */
	public String getCredentialFileName(int id, String lang) {
		return getCredentialFileName(lang).replace("${id}", id + "");
	}

	/**
	 * @param lang Currently selected language
	 * @return Name of file
	 */
	public String getCredentialFileName(String lang) {
		String fileName = this.credentialFileName != null && this.credentialFileName.contains("${id}") ? this.credentialFileName : "credential"
				+ File.separator + "credential${id}.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return the credentialsListFileName
	 */
	public String getCredentialsListFileName(String lang) {
		String fileName = this.credentialsListFileName != null && this.credentialsListFileName.trim().length() > 0 ? this.credentialsListFileName
				: "list" + File.separator + "credentials.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return Folder with all documents for rod file
	 */
	public File getDataFolder() {
		return new File(this.projectFile.getParent(), this.projectFile.getName().substring(0, this.projectFile.getName().lastIndexOf(".")));
	}

	/**
	 * @return the defaultLanguage
	 */
	public String getDefaultLanguage() {
		return this.defaultLanguage;
	}

	/**
	 * @param id Id of document
	 * @param lang Currently selected language
	 * @return Name of file for given document id
	 */
	public String getDocumentFileName(int id, String lang) {
		return getDocumentFileName(lang).replace("${id}", id + "");
	}

	/**
	 * @param lang Currently selected language
	 * @return Name of file
	 */
	public String getDocumentFileName(String lang) {
		String fileName = this.documentFileName != null && this.documentFileName.contains("${id}") ? this.documentFileName : "document"
				+ File.separator + "document${id}.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return the documentsListFileName
	 */
	public String getDocumentsListFileName(String lang) {
		String fileName = this.documentsListFileName != null && this.documentsListFileName.trim().length() > 0 ? this.documentsListFileName
				: "list" + File.separator + "documents.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return the extractDir
	 */
	public File getExtractDir() {
		return this.extractDir;
	}

	/**
	 * @return the fileNamePrefix
	 */
	public String getFileNamePrefix() {
		return this.fileNamePrefix;
	}

	/**
	 * @return Freemarker configuration
	 */
	public Configuration getFreemarkerConfiguration() {
		return this.freemarkerConfiguration;
	}

	/**
	 * @return the fromToSplitter
	 */
	public String getFromToSplitter() {
		return this.fromToSplitter;
	}

	/**
	 * @return the groupsHide
	 */
	public List<String> getGroupsHide() {
		return this.groupsHide;
	}

	/**
	 * @return the groupsOnly
	 */
	public List<String> getGroupsOnly() {
		return this.groupsOnly;
	}

	/**
	 * @param lang Language
	 * @return the indexFileName
	 */
	public String getIndexFileName(String lang) {
		String fileName = this.indexFileName != null && this.indexFileName.trim().length() > 0 ? this.indexFileName : "index.htm";

		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return Input directory with css
	 */
	public File getInputCssDir() {
		return this.inputCssDir;
	}

	/**
	 * @return Input directory with images
	 */
	public File getInputImgDir() {
		return this.inputImgDir;
	}

	/**
	 * @return Input directory with javascript sources
	 */
	public File getInputJsDir() {
		return this.inputJsDir;
	}

	/**
	 * @return the templateFolder
	 */
	public File getInputTemplateDirectory() {
		return this.inputTemplateDirectory;
	}

	/**
	 * @param lang Currently selected language
	 * @return Name of javascript file for given language
	 */
	public String getJavascriptFileName(String lang) {
		String fileName = this.javascriptFileName != null && this.javascriptFileName.trim().length() > 0 ? this.javascriptFileName : "js"
				+ File.separator + "ancestry.js";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return the languages
	 */
	public List<String> getLanguages() {
		return this.languages;
	}

	/**
	 * @param lang Language
	 * @return the listFileName
	 */
	public String getListFileName(String lang) {
		String fileName = this.listFileName != null && this.listFileName.trim().length() > 0 ? this.listFileName : "list" + File.separator
				+ "list.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return Name of file for hierarchical list
	 */
	public String getListHierarchyFileName(String lang) {
		String fileName = this.listHierarchyFileName != null && this.listHierarchyFileName.trim().length() > 0 ? this.listHierarchyFileName
				: "list" + File.separator + "listHierarchy.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return Currently selected locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	/**
	 * @return the mainPersonId
	 */
	public String getMainPersonId() {
		return this.mainPersonIdent;
	}

	/**
	 * @return the mapUrl
	 */
	public String getMapUrl() {
		return this.mapUrl;
	}

	/**
	 * @return the maxSizeHeadX
	 */
	public int getMaxSizeHeadX() {
		return this.maxSizeHeadX;
	}

	/**
	 * @return the maxSizeHeadY
	 */
	public int getMaxSizeHeadY() {
		return this.maxSizeHeadY;
	}

	/**
	 * @return the maxSizeMiniHeadX
	 */
	public int getMaxSizeMiniHeadX() {
		return this.maxSizeMiniHeadX;
	}

	/**
	 * @return the maxSizeMiniHeadY
	 */
	public int getMaxSizeMiniHeadY() {
		return this.maxSizeMiniHeadY;
	}

	/**
	 * @return the maxSizeMiniPhotoX
	 */
	public int getMaxSizeMiniPhotoX() {
		return this.maxSizeMiniPhotoX;
	}

	/**
	 * @return the maxSizeMiniPhotoY
	 */
	public int getMaxSizeMiniPhotoY() {
		return this.maxSizeMiniPhotoY;
	}

	/**
	 * @return the maxSizePhotoX
	 */
	public int getMaxSizePhotoX() {
		return this.maxSizePhotoX;
	}

	/**
	 * @return the maxSizePhotoY
	 */
	public int getMaxSizePhotoY() {
		return this.maxSizePhotoY;
	}

	/**
	 * @return the outputCssDir
	 */
	public File getOutputCssDir() {
		return this.outputCssDir != null ? this.outputCssDir : new File(getOutputDirectory(), "css");
	}

	/**
	 * @return the outputDirectory
	 */
	public File getOutputDirectory() {
		return this.outputDirectory != null ? this.outputDirectory : new File(System.getProperty("user.home"), "html");
	}

	/**
	 * @return the outputEncoding
	 */
	public String getOutputEncoding() {
		return this.outputEncoding;
	}

	/**
	 * @return the outputImgDir
	 */
	public File getOutputImgDir() {
		return this.outputImgDir != null ? this.outputImgDir : new File(getOutputDirectory(), "img");
	}

	/**
	 * @return the outputJsDir
	 */
	public File getOutputJsDir() {
		return this.outputJsDir != null ? this.outputJsDir : new File(getOutputDirectory(), "js");
	}

	/**
	 * @param id Id of partnership
	 * @param lang Currently selected language
	 * @return Name of partners file for given id
	 */
	public String getPartnersFile(int id, String lang) {
		return getPartnershipFileName(lang).replace("${id}", id + "");
	}

	/**
	 * @param lang Currently selected language
	 * @return Partnership file name
	 */
	public String getPartnershipFileName(String lang) {
		String fileName = this.partnershipFileName != null && this.partnershipFileName.contains("${id}") ? this.partnershipFileName
				: "partnership" + File.separator + "partnership${id}.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param id Id of person
	 * @param lang Currently selected language
	 * @return Name of personal file for given id
	 */
	public String getPersonalFile(int id, String lang) {
		return getPersonalFileName(lang).replace("${id}", id + "");
	}

	/**
	 * @param lang Language
	 * @return the personalFileName
	 */
	public String getPersonalFileName(String lang) {
		String fileName = (this.personalFileName != null && this.personalFileName.contains("${id}")) ? this.personalFileName : "person"
				+ File.separator + "person${id}.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param id Id of photo
	 * @param lang Currently selected language
	 * @return Name of file for given photo id
	 */
	public String getPhotoFileName(int id, String lang) {
		return getPhotoFileName(lang).replace("${id}", id + "");
	}

	/**
	 * @param lang Language
	 * @return the photoFileName
	 */
	public String getPhotoFileName(String lang) {
		String fileName = this.photoFileName != null && this.photoFileName.contains("${id}") ? this.photoFileName : "photo" + File.separator
				+ "photo${id}.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return the photosListFileName
	 */
	public String getPhotosListFileName(String lang) {
		String fileName = this.photosListFileName != null && this.photosListFileName.trim().length() > 0 ? this.photosListFileName : "list"
				+ File.separator + "photos.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return the placesListFileName
	 */
	public String getPlacesListFileName(String lang) {
		String fileName = this.placesListFileName != null && this.placesListFileName.trim().length() > 0 ? this.placesListFileName : "list"
				+ File.separator + "places.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return the projectFile
	 */
	public File getProjectFile() {
		return this.projectFile;
	}

	/**
	 * @return Encoding of properties files
	 */
	public String getPropertiesEncoding() {
		return this.propertiesEncoding;
	}

	/**
	 * @param lang Language
	 * @return Statistics file name
	 */
	public String getStatisticFileName(String lang) {
		String fileName = this.statisticsFileName != null && this.statisticsFileName.trim().length() > 0 ? this.statisticsFileName : "list"
				+ File.separator + "stats.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @param lang Language
	 * @return the tableFileName
	 */
	public String getTableFileName(String lang) {
		String fileName = this.tableFileName != null && this.tableFileName.trim().length() > 0 ? this.tableFileName : "list" + File.separator
				+ "table.htm";
		return lang == null ? fileName : Tools.getLanguageFileName(lang, fileName);
	}

	/**
	 * @return the templateEncoding
	 */
	public String getTemplateEncoding() {
		return this.templateEncoding;
	}

	/**
	 * @return Template properties file
	 */
	public File getTemplatePropertiesFile() {
		return this.templatePropertiesFile;
	}

	/**
	 * @return the toPrefix
	 */
	public String getToPrefix() {
		return this.toPrefix;
	}

	/**
	 * @return the townPositionInAddress
	 */
	public int getTownPositionInAddress() {
		return this.townPositionInAddress;
	}

	/**
	 * @return the townPositionInBirth
	 */
	public int getTownPositionInBirth() {
		return this.townPositionInBirth;
	}

	/**
	 * @param properties Properties to get groups from
	 */
	private void initGroups(MyProperties properties) {
		this.groupsOnly.clear();
		for (String g : properties.getProperty(PROPERTY_GROUPS_ONLY, "").split(";")) {
			if (g.length() > 0) {
				this.groupsOnly.add(g);
			}
		}

		this.groupsHide.clear();
		for (String g : properties.getProperty(PROPERTY_GROUPS_HIDE, "").split(";")) {
			if (g.length() > 0) {
				this.groupsHide.add(g);
			}
		}
	}

	/**
	 * Sets collator for given locale code
	 * 
	 * @param localeCode Code of locale (e.g. cs_CZ)
	 */
	public void initLocaleDependentConfiguration(String localeCode) {
		log.debug("locale=" + localeCode);

		if (localeCode == null) {
			this.collator = Collator.getInstance();
			this.locale = new Locale(null);
		} else {
			String[] code = localeCode.split("_");
			switch (code.length) {
			case 1:
				this.locale = new Locale(code[0]);
				break;
			case 2:
				this.locale = new Locale(code[0], code[1]);
				break;
			case 3:
				this.locale = new Locale(code[0], code[1], code[2]);
			}

			this.collator = Collator.getInstance(this.locale);
			// Create a new Collator object with additional rules
			String addRules = "&'\u017d,\u017e'<'?'";
			try {
				RuleBasedCollator oldCollator = (RuleBasedCollator) this.collator;
				log.error(oldCollator.getRules());
				this.collator = new RuleBasedCollator(oldCollator.getRules() + addRules);
			} catch (ParseException e) {
				log.error("Creating RuleBasedCollator failed", e);
			}
		}
	}

	/**
	 * @param properties Properties to get last used map url from
	 */
	private void initMapsUrl(MyProperties properties) {
		try {
			Properties mapsProperties = new Properties();
			mapsProperties.load(new InputStreamReader(new FileInputStream(new File(".." + File.separator + "etc" + File.separator
					+ "maps.properties")), "UTF-8"));
			this.mapUrl = properties.getProperty(PROPERTY_MAPS, "0");
			log.debug(PROPERTY_MAPS + "=" + this.mapUrl);
			this.mapUrl = mapsProperties.getProperty("map.url." + this.mapUrl);
		} catch (Exception ex) {
			log.warn("Can not get url of maps.", ex);
		}
	}

	/**
	 * @param properties Properties to get max size of images from
	 */
	private void initPicturesSize(MyProperties properties) {
		String size;
		String[] widthAndHeight;

		size = properties.getProperty(PROPERTY_MAX_SIZE_PHOTO, "800x600");

		widthAndHeight = size.split("x");
		this.maxSizePhotoX = Tools.pictureWidth(widthAndHeight);
		this.maxSizePhotoY = Tools.pictureHeight(widthAndHeight);

		size = properties.getProperty(PROPERTY_MAX_SIZE_MINI_PHOTO, "-x150");

		widthAndHeight = size.split("x");
		this.maxSizeMiniPhotoX = Tools.pictureWidth(widthAndHeight);
		this.maxSizeMiniPhotoY = Tools.pictureHeight(widthAndHeight);

		size = properties.getProperty(PROPERTY_MAX_SIZE_HEAD, "150x200");

		widthAndHeight = size.split("x");
		this.maxSizeHeadX = Tools.pictureWidth(widthAndHeight);
		this.maxSizeHeadY = Tools.pictureHeight(widthAndHeight);

		size = properties.getProperty(PROPERTY_MAX_SIZE_MINI_HEAD, "60x80");

		widthAndHeight = size.split("x");
		this.maxSizeMiniHeadX = Tools.pictureWidth(widthAndHeight);
		this.maxSizeMiniHeadY = Tools.pictureHeight(widthAndHeight);
	}

	/**
	 * @return the blockHidden
	 */
	public boolean isBlockHidden() {
		return this.blockHidden;
	}

	/**
	 * 
	 * @param live Is the current person living?
	 * @return the hideAdditionalInfo
	 */
	public boolean isHideAdditionalInfo(boolean live) {
		return this.hideAdditionalInfo.equals("all") || (this.hideAdditionalInfo.equals("live") && live) ? true : false;
	}

	/**
	 * @return Hide additional info on index page
	 */
	public boolean isHideAdditionalInfoOnIndex() {
		return this.hideAdditionalInfoOnIndex;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideDates
	 */
	public boolean isHideBirthData(boolean live) {
		return this.hideBirthData.equals("all") || (this.hideBirthData.equals("live") && live) ? true : false;
	}

	/**
	 * @return the hideContact
	 */
	public boolean isHideContact() {
		return this.hideContact.equals("all") ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideCredentials
	 */
	public boolean isHideCredentials(boolean live) {
		return this.hideCredentials.equals("all") || (this.hideCredentials.equals("live") && live) ? true : false;
	}

	/**
	 * @return the hideDates
	 */
	public boolean isHideDeathData() {
		return this.hideDeathData.equals("all");
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideDocuments
	 */
	public boolean isHideDocuments(boolean live) {
		return this.hideDocuments.equals("all") || (this.hideDocuments.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideEducation
	 */
	public boolean isHideEducation(boolean live) {
		return this.hideEducation.equals("all") || (this.hideEducation.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return True if head can be hide, false otherwise
	 */
	public boolean isHideHead(boolean live) {
		return this.hideHeads.equals("all") || (this.hideHeads.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideJobs
	 */
	public boolean isHideJobs(boolean live) {
		return this.hideJobs.equals("all") || (this.hideJobs.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideLivePlaces
	 */
	public boolean isHideLivePlace(boolean live) {
		return this.hideLivePlaces.equals("all") || (this.hideLivePlaces.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideMarriageDate
	 */
	public boolean isHideMarriageDate(boolean live) {
		return this.hideMarriageDate.equals("all") || (this.hideMarriageDate.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideMarriagePlace
	 */
	public boolean isHideMarriagePlace(boolean live) {
		return this.hideMarriagePlace.equals("all") || (this.hideMarriagePlace.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideNationality
	 */
	public boolean isHideNationality(boolean live) {
		return this.hideNationality.equals("all") || (this.hideNationality.equals("live") && live) ? true : false;
	}

	/**
	 * @return Hide path between current person and main person
	 */
	public boolean isHidePathToMain() {
		return this.hidePathToMain;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hidePhotos
	 */
	public boolean isHidePhotos(boolean live) {
		return this.hidePhotos.equals("all") || (this.hidePhotos.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideRegistryInfo
	 */
	public boolean isHideRegistryInfo(boolean live) {
		return this.hideRegistryInfo.equals("all") || (this.hideRegistryInfo.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideReligion
	 */
	public boolean isHideReligion(boolean live) {
		return this.hideReligion.equals("all") || (this.hideReligion.equals("live") && live) ? true : false;
	}

	/**
	 * @param live Is the current person living?
	 * @return the hideUserDefined
	 */
	public boolean isHideUserDefined(boolean live) {
		return this.hideUserDefined.equals("all") || (this.hideUserDefined.equals("live") && live) ? true : false;
	}

	/**
	 * @return the hideWeb
	 */
	public boolean isHideWeb() {
		return this.hideWeb.equals("all");
	}

	/**
	 * @return If project uses new xml format
	 */
	public boolean isProjectXml() {
		return this.projectFile.getName().endsWith(".rodx");
	}

	/**
	 * @return If project uses new xml zipped format
	 */
	public boolean isProjectZipped() {
		return this.projectFile.getName().endsWith(".rodz") || this.projectFile.getName().endsWith(".zip");
	}

	/**
	 * @return the showBirthInLists
	 */
	public boolean isShowBirthInLists() {
		return this.showBirthInLists;
	}

	/**
	 * @return the showDeathInLists
	 */
	public boolean isShowDeathInLists() {
		return this.showDeathInLists;
	}

	/**
	 * @return the showId
	 */
	public boolean isShowId() {
		return this.showId;
	}

	/**
	 * @return the showIdInLists
	 */
	public boolean isShowIdInLists() {
		return this.showIdInLists;
	}

	/**
	 * Checks if is show main person.
	 * 
	 * @return true, if is show main person
	 */
	public boolean isShowMainPerson() {
		return this.showMainPerson;
	}

	/**
	 * @return Show list of allowed groups on index page
	 */
	public boolean isShowInfoAllowed() {
		return this.showInfoAllowed;
	}

	/**
	 * @return Show list of blocked/hidden groups on index page
	 */
	public boolean isShowInfoBlocked() {
		return this.showInfoBlocked;
	}

	/**
	 * @return the showShortDescInLists
	 */
	public boolean isShowShortDescInLists() {
		return this.showShortDescInLists;
	}

	/**
	 * @return the showWeddingNameInLists
	 */
	public boolean isShowWeddingNameInLists() {
		return this.showWeddingNameInLists;
	}

	/**
	 * @return the useBurialPlace
	 */
	public boolean isUseBurialPlace() {
		return this.useBurialPlace;
	}

	/**
	 * @return the useMaidenName
	 */
	public boolean isUseMaidenName() {
		return this.useMaidenName;
	}

	/**
	 * Process configuration read from file and read from command line and get as properties from external modules
	 * 
	 * @param cmd Parsed command line arguments
	 * @param externalProperties Properties from external module
	 * @throws AppException In case anything failed while processing configuration
	 */
	public void processConfiguration(CommandLine cmd, Properties externalProperties) throws AppException {
		File basePathOfApplication = Tools.getBasePath();
		MyProperties properties = new MyProperties();

		if (cmd.hasOption(OPTION_CONFIG.getOpt())) {
			readFromFile(properties, Tools.getAbsolutePath(basePathOfApplication, cmd.getOptionValue(OPTION_CONFIG.getOpt())));
		}

		readPropertiesFromCmd(properties, basePathOfApplication, cmd);

		readFromGuiProperties(properties, externalProperties);

		processProperties(properties);

		log.info("Configuration successfully processed.");
		Main.status.printOutputMessage("progress.configurationDone");
	}

	/**
	 * @param properties Properties to set to this Config object
	 * @throws AppException In case processing failed
	 */
	public void processProperties(MyProperties properties) throws AppException {
		Main.status.setProcessStatus(Config.class, "Config1");

		if (properties.getProperty(PROPERTY_FILE) != null) {
			this.projectFile = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_FILE));
			log.debug(PROPERTY_FILE + "=" + this.projectFile);
		}

		if (properties.getProperty(PROPERTY_OUTPUT_DIR) != null) {
			this.outputDirectory = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_OUTPUT_DIR));
			log.debug(PROPERTY_OUTPUT_DIR + "=" + this.outputDirectory);
		}

		if (properties.getProperty(PROPERTY_OUTPUT_IMG_DIR) != null && properties.getProperty(PROPERTY_OUTPUT_IMG_DIR).trim().length() > 0) {
			this.outputImgDir = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_OUTPUT_IMG_DIR));
		} else {
			this.outputImgDir = new File(getOutputDirectory(), "img");
		}
		log.debug(PROPERTY_OUTPUT_DIR + "=" + this.outputImgDir);

		if (properties.getProperty(PROPERTY_OUTPUT_CSS_DIR) != null && properties.getProperty(PROPERTY_OUTPUT_CSS_DIR).trim().length() > 0) {
			this.outputCssDir = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_OUTPUT_CSS_DIR));
		} else {
			this.outputCssDir = new File(getOutputDirectory(), "css");
		}
		log.debug(PROPERTY_OUTPUT_CSS_DIR + "=" + this.outputCssDir);

		if (properties.getProperty(PROPERTY_TEMPLATES_DIR) != null) {
			this.inputTemplateDirectory = Tools
					.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_TEMPLATES_DIR));
			log.debug(PROPERTY_TEMPLATES_DIR + "=" + this.inputTemplateDirectory);
		}

		if (properties.getProperty(PROPERTY_INPUT_IMG_DIR) != null && properties.getProperty(PROPERTY_INPUT_IMG_DIR).trim().length() > 0) {
			this.inputImgDir = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_INPUT_IMG_DIR));
		} else {
			this.inputImgDir = new File(getInputTemplateDirectory(), "img");
		}
		log.debug(PROPERTY_INPUT_IMG_DIR + "=" + this.inputImgDir);

		if (properties.getProperty(PROPERTY_INPUT_JS_DIR) != null && properties.getProperty(PROPERTY_INPUT_JS_DIR).trim().length() > 0) {
			this.inputJsDir = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_INPUT_JS_DIR));
		} else {
			this.inputJsDir = new File(getInputTemplateDirectory(), "js");
		}
		log.debug(PROPERTY_INPUT_JS_DIR + "=" + this.inputJsDir);

		if (properties.getProperty(PROPERTY_INPUT_CSS_DIR) != null && properties.getProperty(PROPERTY_INPUT_CSS_DIR).trim().length() > 0) {
			this.inputCssDir = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties.getPathProperty(PROPERTY_INPUT_CSS_DIR));
		} else {
			this.inputCssDir = new File(getInputTemplateDirectory(), "css");
		}
		log.debug(PROPERTY_INPUT_CSS_DIR + "=" + this.inputCssDir);

		if (properties.getProperty(PROPERTY_TEMPLATE_PROPERTIES_FILE) != null
				&& properties.getProperty(PROPERTY_TEMPLATE_PROPERTIES_FILE).trim().length() > 0) {
			this.templatePropertiesFile = Tools.getAbsolutePath(this.basePathOfPropertiesFile, properties
					.getPathProperty(PROPERTY_TEMPLATE_PROPERTIES_FILE));
			log.debug(PROPERTY_TEMPLATE_PROPERTIES_FILE + "=" + this.templatePropertiesFile);
		}

		Main.status.setProcessStatus(Config.class, "Config2");

		this.templateEncoding = properties.getProperty(PROPERTY_TEMPLATE_ENCODING);
		this.outputEncoding = properties.getProperty(PROPERTY_OUTPUT_ENCODING);

		this.personalFileName = properties.getPathProperty(PROPERTY_PERSONAL_FILE_NAME);
		this.partnershipFileName = properties.getPathProperty(PROPERTY_PARTNERSHIP_FILE_NAME);
		this.photoFileName = properties.getPathProperty(PROPERTY_PHOTO_FILE_NAME);
		this.indexFileName = properties.getPathProperty(PROPERTY_INDEX_FILE_NAME);
		this.listFileName = properties.getPathProperty(PROPERTY_LIST_FILE_NAME);
		this.tableFileName = properties.getPathProperty(PROPERTY_TABLE_FILE_NAME);
		this.placesListFileName = properties.getPathProperty(PROPERTY_PLACES_LIST_FILE_NAME);
		this.listHierarchyFileName = properties.getPathProperty(PROPERTY_LIST_HIERARCHY_FILE_NAME);
		this.documentFileName = properties.getPathProperty(PROPERTY_DOCUMENT_FILE_NAME);
		this.credentialFileName = properties.getPathProperty(PROPERTY_CREDENTIAL_FILE_NAME);
		this.statisticsFileName = properties.getPathProperty(PROPERTY_STATISTICS_FILE_NAME);
		this.photosListFileName = properties.getPathProperty(PROPERTY_PHOTOS_FILE_NAME);
		this.documentsListFileName = properties.getPathProperty(PROPERTY_DOCUMENTS_FILE_NAME);
		this.credentialsListFileName = properties.getPathProperty(PROPERTY_CREDENTIALS_FILE_NAME);
		this.javascriptFileName = properties.getPathProperty(PROPERTY_JAVASCRIPT_FILE_NAME);

		Main.status.setProcessStatus(Config.class, "Config3");

		this.hideBirthData = properties.getProperty(PROPERTY_HIDE_BIRTH_DATA, "no");
		this.hideDeathData = properties.getProperty(PROPERTY_HIDE_DEATH_DATA, "no");
		this.hideLivePlaces = properties.getProperty(PROPERTY_HIDE_LIVE_PLACES, "no");
		this.hideJobs = properties.getProperty(PROPERTY_HIDE_JOBS, "no");
		this.hideEducation = properties.getProperty(PROPERTY_HIDE_EDUCATION, "no");
		this.hideContact = properties.getProperty(PROPERTY_HIDE_CONTACT, "no");
		this.hideWeb = properties.getProperty(PROPERTY_HIDE_WEB, "no");
		this.hideReligion = properties.getProperty(PROPERTY_HIDE_RELIGION, "no");
		this.hideNationality = properties.getProperty(PROPERTY_HIDE_NATIONALITY, "no");
		this.hideUserDefined = properties.getProperty(PROPERTY_HIDE_USER_DEFINED, "no");
		this.hideRegistryInfo = properties.getProperty(PROPERTY_HIDE_REGISTRY_INFO, "no");
		this.hideHeads = properties.getProperty(PROPERTY_HIDE_HEADS, "no");
		this.hidePhotos = properties.getProperty(PROPERTY_HIDE_PHOTOS, "no");
		this.hideDocuments = properties.getProperty(PROPERTY_HIDE_DOCUMENTS, "no");
		this.hideCredentials = properties.getProperty(PROPERTY_HIDE_CREDENTIALS, "no");
		this.hideMarriageDate = properties.getProperty(PROPERTY_HIDE_MARRIAGE_DATA, "no");
		this.hideMarriagePlace = properties.getProperty(PROPERTY_HIDE_MARRIAGE_PLACE, "no");
		this.hideAdditionalInfo = properties.getProperty(PROPERTY_HIDE_ADDITIONAL_INFO, "no");

		this.blockHidden = properties.getBooleanProperty(PROPERTY_BLOCK_HIDDEN);
		this.showId = properties.getBooleanProperty(PROPERTY_SHOW_ID);
		this.showInfoBlocked = properties.getBooleanProperty(PROPERTY_SHOW_INFO_BLOCKED);
		this.showInfoAllowed = properties.getBooleanProperty(PROPERTY_SHOW_INFO_ALLOWED);
		this.showIdInLists = properties.getBooleanProperty(PROPERTY_SHOW_ID_IN_LISTS);
		this.showMainPerson = properties.getBooleanProperty(PROPERTY_SHOW_MAIN_PERSON);
		this.showBirthInLists = properties.getBooleanProperty(PROPERTY_SHOW_BIRTH_IN_LISTS);
		this.showDeathInLists = properties.getBooleanProperty(PROPERTY_SHOW_DEATH_IN_LISTS);
		this.showShortDescInLists = properties.getBooleanProperty(PROPERTY_SHOW_SHORT_DESC_IN_LISTS);
		this.showWeddingNameInLists = properties.getBooleanProperty(PROPERTY_SHOW_WEDDING_NAME_IN_LISTS);
		this.useBurialPlace = properties.getBooleanProperty(PROPERTY_USE_BURIAL_PLACE);
		this.useMaidenName = properties.getBooleanProperty(PROPERTY_USE_MAIDEN_NAME);
		this.hidePathToMain = properties.getBooleanProperty(PROPERTY_HIDE_PATH_TO_MAIN_PERSON);
		this.hideAdditionalInfoOnIndex = properties.getBooleanProperty(PROPERTY_HIDE_ADDITIONAL_INFO_ON_INDEX);

		this.birthDeathSplitter = properties.getProperty(PROPERTY_BIRTH_DEATH_SPLITTER, ", ");
		this.fromToSplitter = properties.getProperty(PROPERTY_FROM_TO_SPLITTER, "-");
		this.toPrefix = properties.getProperty(PROPERTY_TO_PREFIX, "?-");
		this.addressSplitter = properties.getProperty(PROPERTY_ADDRESS_SPLITTER, ",");
		this.townPositionInAddress = properties.getIntProperty(PROPERTY_TOWN_IN_ADDRESS, 2) - 1;
		this.townPositionInBirth = properties.getIntProperty(PROPERTY_TOWN_IN_BIRTH, 1) - 1;
		this.mainPersonIdent = properties.getProperty(PROPERTY_MAIN_PERSON_ID, "0");

		this.languages = Arrays.asList(properties.getProperty(PROPERTY_LANGUAGES, "cz;en").split(";"));
		this.defaultLanguage = properties.getProperty(PROPERTY_DEFAULT_LANGUAGE, "cz");

		initPicturesSize(properties);

		initMapsUrl(properties);

		initGroups(properties);

		Main.status.setProcessStatus(Config.class, "Config4");
	}

	/**
	 * Reads configuration from file
	 * 
	 * @param properties Properties object, new properties are added to it
	 * @param confFile File to read
	 * @throws AppException In case file not found or reading failed
	 */
	public void readFromFile(Properties properties, File confFile) throws AppException {
		log.info("Reading properties from file...");

		File confFileNormalized = Tools.getCanonicalFile(confFile);

		this.basePathOfPropertiesFile = confFileNormalized.getParentFile();

		try {
			String defaultEncoding = new InputStreamReader(new ByteArrayInputStream(new byte[0])).getEncoding();

			properties.load(new InputStreamReader(new FileInputStream(confFileNormalized), defaultEncoding));

			this.propertiesEncoding = properties.getProperty("propertiesEncoding");
			if (this.propertiesEncoding != null && this.propertiesEncoding.trim().length() > 0) {
				properties.load(new InputStreamReader(new FileInputStream(confFileNormalized), this.propertiesEncoding));
			} else {
				this.propertiesEncoding = defaultEncoding;
			}
		} catch (FileNotFoundException e) {
			throw new AppException("Can not found file with configuration - " + confFileNormalized.getName(), e);
		} catch (IOException e) {
			throw new AppException("Reading configuration failed - " + confFileNormalized.getName(), e);
		}

		log.info("Reading properties from file done.");
	}

	/**
	 * Reads properties sened from gui form
	 * 
	 * @param properties Properties object, new properties are added to it
	 * @param externalProperties Properties from external module, will be copied to the properties parameter
	 */
	public void readFromGuiProperties(Properties properties, Properties externalProperties) {
		if (externalProperties == null) {
			return;
		}

		log.debug("Processing data from gui properties ...");

		for (Object key : externalProperties.keySet()) {
			properties.setProperty((String) key, externalProperties.getProperty((String) key));
			log.debug("Read: " + key + "=" + externalProperties.getProperty((String) key));
		}

		log.info("Reading properties from gui properties done.");
	}

	/**
	 * Reads properties from command line arguments
	 * 
	 * @param properties Properties object, new properties are added to it
	 * @param basePath Base path for using for relative paths
	 * @param cmd Object that stores parameters of command line
	 * @throws AppException In case processing cmd parameters failed
	 */
	public void readPropertiesFromCmd(Properties properties, File basePath, CommandLine cmd) throws AppException {
		log.info("Reading arguments values...");

		if (cmd.hasOption(OPTION_FILENAME.getOpt())) {
			properties
					.setProperty(PROPERTY_FILE, Tools.getAbsolutePath(basePath, cmd.getOptionValue(OPTION_FILENAME.getOpt())).getAbsolutePath());
		}

		if (cmd.hasOption(OPTION_HTML_DIR.getOpt())) {
			properties.setProperty(PROPERTY_OUTPUT_DIR, Tools.getAbsolutePath(basePath, cmd.getOptionValue(OPTION_HTML_DIR.getOpt()))
					.getAbsolutePath());
		}

		if (cmd.hasOption(OPTION_IMGS_DIR.getOpt())) {
			properties.setProperty(PROPERTY_OUTPUT_IMG_DIR, Tools.getAbsolutePath(basePath, cmd.getOptionValue(OPTION_IMGS_DIR.getOpt()))
					.getAbsolutePath());
		}

		if (cmd.hasOption(OPTION_CSS_DIR.getOpt())) {
			properties.setProperty(PROPERTY_OUTPUT_CSS_DIR, Tools.getAbsolutePath(basePath, cmd.getOptionValue(OPTION_CSS_DIR.getOpt()))
					.getAbsolutePath());
		}

		if (cmd.hasOption(OPTION_TEMPLATES_DIR.getOpt())) {
			properties.setProperty(PROPERTY_TEMPLATES_DIR, Tools.getAbsolutePath(basePath, cmd.getOptionValue(OPTION_TEMPLATES_DIR.getOpt()))
					.getAbsolutePath());
		}

		if (cmd.hasOption(OPTION_TEMPLATE_PROPERTIES.getOpt())) {
			properties.setProperty(PROPERTY_TEMPLATE_PROPERTIES_FILE, Tools.getAbsolutePath(basePath,
					cmd.getOptionValue(OPTION_TEMPLATE_PROPERTIES.getOpt())).getAbsolutePath());
		}

		log.info("Reading arguments values done");
	}

	/**
	 * @param extractDir the extractDir to set
	 */
	public void setExtractDir(File extractDir) {
		this.extractDir = extractDir;
	}

	/**
	 * @param fileNamePrefix the fileNamePrefix to set
	 */
	public void setFileNamePrefix(String fileNamePrefix) {
		this.fileNamePrefix = fileNamePrefix;
	}

	/**
	 * @param mainPersonId Id of main person
	 */
	public void setMainPersonId(String mainPersonId) {
		this.mainPersonIdent = mainPersonId;
	}

	/**
	 * @param projectFile the projectFile to set
	 */
	public void setProjectFile(File projectFile) {
		this.projectFile = projectFile;
	}
}