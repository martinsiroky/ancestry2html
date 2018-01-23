package cz.sirsi.ancestry.core.freemarker;

import java.text.Collator;
import java.util.concurrent.locks.ReentrantLock;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.Partnership;
import cz.sirsi.ancestry.core.data.Person;
import cz.sirsi.ancestry.core.main.Tree;
import cz.sirsi.ancestry.core.tools.Tools;

/**
 * Class used for calling Tools methods from templates
 * 
 * @author msiroky
 */
public class UtilsMethods {

	private static UtilsMethods utils;
	private static ReentrantLock utilsLock = new ReentrantLock();

	/**
	 * @return Singleton instance of Tree
	 */
	public static UtilsMethods getInstance() {
		if (utils != null) {
			return utils;
		}

		try {
			utilsLock.lock();
			if (utils == null) {
				utils = new UtilsMethods();
			}
		} finally {
			utilsLock.unlock();
		}

		return utils;
	}

	private UtilsMethods() {
		// nothing
	}

	/**
	 * @param id Id of credential to get file name for
	 * @return Name of credential file for given id
	 */
	public String getCredentialFile(int id) {
		return Tools.transformSeparators(Config.getInstance().getCredentialFileName(id, Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of credentials list file
	 */
	public String getCredentialsFile() {
		return Tools.transformSeparators(Config.getInstance().getCredentialsListFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @param id Id of document to get file name for
	 * @return Name of document file for given id
	 */
	public String getDocumentFile(int id) {
		return Tools.transformSeparators(Config.getInstance().getDocumentFileName(id, Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of documents list file
	 */
	public String getDocumentsFile() {
		return Tools.transformSeparators(Config.getInstance().getDocumentsListFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @param word Word to get first letter for
	 * @return First letter of the word (may be more chars, e.g. 'ch' in czech)
	 */
	public String getFirstLetter(String word) {
		return Tools.getFirstLetter(word);
	}

	/**
	 * @return Name of hierarchical list of people file
	 */
	public String getHierarchicalListFile() {
		return Tools.transformSeparators(Config.getInstance().getListHierarchyFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of index file
	 */
	public String getIndexFile() {
		return Tools.transformSeparators(Config.getInstance().getIndexFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of javascript file
	 */
	public String getJavascriptFile() {
		return Tools.transformSeparators(Config.getInstance().getJavascriptFileName(Config.getInstance().getFileNamePrefix()));
	}

	// DOCUMENT ME
	public String getLanguageFileName(String lang, String baseFileName) {
		return Tools.transformSeparators(Tools.getLanguageFileName(lang, baseFileName));
	}

	/**
	 * @return Name of list of people file
	 */
	public String getListFile() {
		return Tools.transformSeparators(Config.getInstance().getListFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Main person id (used for hierarchical list)
	 */
	public String getMainPerson() {
		return Config.getInstance().getMainPersonId();
	}

	/**
	 * @param place Place to search
	 * @param lang Language of web maps user interface
	 * @return Url for maps service for given place and language
	 */
	public String getMapUrl(String place, String lang) {
		return Config.getInstance().getMapUrl().replace("{0}", place).replace("{1}", lang);
	}

	/**
	 * @param id Id of partnership to get file name for
	 * @return Name of partners file for given id
	 */
	public String getPartnersFile(int id) {
		return Tools.transformSeparators(Config.getInstance().getPartnersFile(id, Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * 
	 * @param id Id of partnership to get object for
	 * @return Partnership object for given partnership id
	 */
	public Partnership getPartnershipForId(int id) {
		Tree tree = Tree.getInstance();
		String stringId = tree.getPartnershipKeysMapping().get(id);
		if (stringId == null) {
			return null;
		}

		return tree.getPartnershipMap().get(stringId);
	}

	/**
	 * @param id
	 * @return Name of personal file for given id
	 */
	public String getPersonalFile(int id) {
		return Tools.transformSeparators(Config.getInstance().getPersonalFile(id, Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @param id Person id
	 * @return Person instance for given id
	 */
	public Person getPersonForId(int id) {
		return Tree.getInstance().getPeopleMap().get(id);
	}

	/**
	 * @param id
	 * @return Name of photo file for given id
	 */
	public String getPhotoFile(int id) {
		return Tools.transformSeparators(Config.getInstance().getPhotoFileName(id, Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of photo list file
	 */
	public String getPhotosFile() {
		return Tools.transformSeparators(Config.getInstance().getPhotosListFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of places list file
	 */
	public String getPlacesListFile() {
		return Tools.transformSeparators(Config.getInstance().getPlacesListFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return Name of statistics file
	 */
	public String getStatisticsFile() {
		return Tools.transformSeparators(Config.getInstance().getStatisticFileName(Config.getInstance().getFileNamePrefix()));
	}

	/**
	 * @return True in case some visible credentials are in the list
	 */
	public boolean isCredentials() {
		return Tree.getInstance().getCountOfVisibleCredentials() > 0;
	}

	/**
	 * @return True in case some visible documents are in the list
	 */
	public boolean isDocuments() {
		return Tree.getInstance().getCountOfVisibleDocuments() > 0;
	}

	/**
	 * Checks if the first letter of the word is the same as used before (given as parameter)
	 * 
	 * @param word Word
	 * @param lastLetter Last used first letter
	 * @return True in case the first letter is different than last used, false otherwise
	 */
	public boolean isNewLetter(String word, String lastLetter) {
		if (lastLetter == null) {
			return true;
		}
		String letter = Tools.getFirstLetter(word);
		// for example c(c s hackem) and c has the same position in alphabet, so compare first letters by collator
		Config.getInstance().getCollator().setStrength(Collator.PRIMARY);
		boolean isNew = Config.getInstance().getCollator().compare(letter, lastLetter) != 0;
		return isNew;
	}

	/**
	 * @return True in case some visible photos are in the list
	 */
	public boolean isPhotos() {
		return Tree.getInstance().getCountOfVisiblePhotos() > 0;
	}

	/**
	 * In case text is longer than limit cuts it and add three dots to the end
	 * 
	 * @param text Text
	 * @param limit Limit of chars
	 * @return Modified text
	 */
	public String shortText(String text, int limit) {
		if (text.length() > limit) {
			return text.substring(0, limit - 3).trim() + "...";
		}
		return text;
	}
}