package cz.sirsi.ancestry.gui.main;

import java.awt.event.ItemEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.gui.bean.PrivacyComboBean;
import cz.sirsi.ancestry.gui.bean.PrivacyItemBean;
import cz.sirsi.ancestry.gui.bean.PrivacyProfileItemBean;
import cz.sirsi.ancestry.gui.renderer.ComboBoxOptionRenderer;
import cz.sirsi.ancestry.gui.renderer.ComboBoxPrivacyRenderer;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * Manager for privacy profiles components and actions
 * 
 * @author msiroky
 */
public class PrivacyProfilesManager implements Constants {

	/**
	 * Initializes logger for this class
	 **/
	private static Logger log = Logger.getLogger(PrivacyProfilesManager.class);

	private static PrivacyItemBean OPTION_ALL;

	private static PrivacyItemBean OPTION_LIVE;

	private static PrivacyItemBean OPTION_NO;

	private GuiForm mainForm;

	private List<PrivacyProfileItemBean> privacyProfiles;

	private Properties privacyProfilesProperties;

	/**
	 * Instantiates a new privacy profiles manager.
	 * 
	 * @param mainForm the main form
	 */
	public PrivacyProfilesManager(GuiForm mainForm) {
		super();
		this.mainForm = mainForm;
	}

	/**
	 * Action when button to delete selected profile is pressed
	 */
	protected void buttonDeletePrivacyProfileActionPerformed() {
		deletePrivacyProfileProperties(this.mainForm.comboPrivacyProfiles.getSelectedIndex());

		ComboBoxModel comboPrivacyProfilesModel = new DefaultComboBoxModel(getPrivacyProfiles());
		this.mainForm.comboPrivacyProfiles.setModel(comboPrivacyProfilesModel);
		// select first profile
		this.mainForm.comboPrivacyProfiles.setSelectedIndex(0);
		// reload all privacy combos
		this.readAllPrivacyCombos();
	}

	/**
	 * Action when user press Save privacy profile button
	 */
	protected void buttonSavePrivacyProfileActionPerformed() {
		log.info("Saving user privacy profile");
		Properties privacyProperties = new Properties();
		storeAllPrivacyCombos(privacyProperties);

		String profileName = JOptionPane.showInputDialog(null, GuiTools.getLocalized("profile.add"),
				GuiTools.getLocalized("profile.add.title"), JOptionPane.QUESTION_MESSAGE);
		if (profileName.length() == 0) {
			log.info("Profile was not saved. Empty name.");
			return;
		}

		int count = addNewPrivacyProfileProperties(profileName, privacyProperties);

		ComboBoxModel comboPrivacyProfilesModel = new DefaultComboBoxModel(getPrivacyProfiles());
		this.mainForm.comboPrivacyProfiles.setModel(comboPrivacyProfilesModel);
		// select new created profile
		this.mainForm.comboPrivacyProfiles.setSelectedIndex(count - 2);

		log.info("New profile \"" + profileName + "\" added");
	}

	/**
	 * Change selected privacy profile action
	 * 
	 * @param evt Event that invoked this method
	 */
	public void comboPrivacyProfilesItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			readAllPrivacyCombos();
		}
	}

	/**
	 * Get values of all privacy combos and store it into Properties object
	 * 
	 * @param privacyProperties Properties with all privacy settings
	 */
	public void storeAllPrivacyCombos(Properties privacyProperties) {
		for (PrivacyComboBean combo : this.mainForm.privacyCombos) {
			privacyProperties.setProperty(combo.getPropertyKey(), ((PrivacyItemBean) combo.getComboBox().getSelectedItem()).getKey());
		}
	}

	/**
	 * Load from file all privacy settings and set its values into combos
	 */
	public void readAllPrivacyCombos() {
		Properties privacySettings = new Properties();
		String propertiesFile = ((PrivacyProfileItemBean) this.mainForm.comboPrivacyProfiles.getSelectedItem()).getPrivacyPropertiesFile();

		// disable delete profile button for default profiles
		this.mainForm.buttonDeleteProfile
				.setEnabled(this.mainForm.comboPrivacyProfiles.getSelectedIndex() >= Constants.DEFAULT_PRIVACY_PROFILES_COUNT);

		try {
			privacySettings.load(new FileInputStream(new File(".." + File.separator + "etc" + File.separator + propertiesFile)));
			loadAllPrivacyCombos(privacySettings);
		} catch (Throwable t) {
			log.error("Load privacy settings failed.", t);
		}

		// TODO why?
		loadAllPrivacyCombos(privacySettings);
	}

	/**
	 * Sets all privacy combos depends on given Properties
	 * 
	 * @param privacyProperties Privacy properies to set into combos
	 */
	private void loadAllPrivacyCombos(Properties privacyProperties) {
		for (PrivacyComboBean combo : this.mainForm.privacyCombos) {
			setCombo(combo.getComboBox(), privacyProperties.getProperty(combo.getPropertyKey()));
		}
	}

	/**
	 * Sets selected item of combo by key
	 * 
	 * @param combo Combo to set its value
	 * @param key Key to set selected
	 */
	private static void setCombo(JComboBox combo, String key) {
		if (key == null) {
			return;
		}
		for (int i = 0; i < combo.getItemCount(); i++) {
			if (key.equalsIgnoreCase(((PrivacyItemBean) combo.getItemAt(i)).getKey())) {
				combo.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Initializes combobox with privacy profiles
	 */
	public void initPrivacyProfilesCombo() {
		ListCellRenderer comboPrivacyProfileRenderer = new ComboBoxPrivacyRenderer();
		ComboBoxModel comboPrivacyProfilesModel = new DefaultComboBoxModel(this.getPrivacyProfiles());
		this.mainForm.comboPrivacyProfiles.setModel(comboPrivacyProfilesModel);
		this.mainForm.comboPrivacyProfiles.setRenderer(comboPrivacyProfileRenderer);

		for (int i = 0; i < this.mainForm.comboPrivacyProfiles.getItemCount(); i++) {
			if (((PrivacyProfileItemBean) this.mainForm.comboPrivacyProfiles.getItemAt(i)).getPrivacyPropertiesFile().equals(
					GuiTools.getLastUsed("privacy", ""))) {
				this.mainForm.comboPrivacyProfiles.setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Stores new privacy profile for next usage
	 * 
	 * @param privacyProfileName Name of profile
	 * @param privacyProperties Properties to store to file
	 * @return Index of new created privacy profile in combo (=count of privacy profiles)
	 */
	public int addNewPrivacyProfileProperties(String privacyProfileName, Properties privacyProperties) {
		String propertiesFile = ".." + File.separator + "etc" + File.separator + "profile" + (this.privacyProfiles.size() + 1) + ".properties";
		try {
			privacyProperties.store(new FileOutputStream(new File(propertiesFile)), null);
		} catch (Throwable t) {
			log.error("Can not store list of privacy profiles", t);
		}

		this.privacyProfiles.add(new PrivacyProfileItemBean(privacyProfileName, propertiesFile));

		return writeListOfPrivacyProfiles();
	}

	/**
	 * Deletes privacy profile from combo
	 * 
	 * @param toDelete Index of profile to delete
	 */
	public void deletePrivacyProfileProperties(int toDelete) {

		PrivacyProfileItemBean privacyProfileToDelete = this.privacyProfiles.get(toDelete);
		File profileFile = null;
		try {
			profileFile = new File(privacyProfileToDelete.getPrivacyPropertiesFile()).getCanonicalFile();
			profileFile.delete();
		} catch (IOException ex) {
			log.error("Can not delete profile file " + privacyProfileToDelete.getPrivacyPropertiesFile());
		}

		this.privacyProfiles.remove(toDelete);

		writeListOfPrivacyProfiles();
	}

	/**
	 * Loads list of privacy profiles from file
	 */
	public void readListOfPrivacyProfiles() {
		this.privacyProfilesProperties = new Properties();

		try {
			this.privacyProfilesProperties.load(new InputStreamReader(new FileInputStream(new File(Constants.PRIVACY_PROFILES_FILE_NAME)),
					"UTF-8"));

			this.privacyProfiles = new ArrayList<PrivacyProfileItemBean>();
			int i = 1;
			while (true) {
				String privacyProfileName = this.privacyProfilesProperties.getProperty("profileName." + i);
				String propertiesFile = this.privacyProfilesProperties.getProperty("properties." + i);
				if (privacyProfileName == null || propertiesFile == null) {
					break;
				}

				this.privacyProfiles.add(new PrivacyProfileItemBean(privacyProfileName, propertiesFile));
				i++;
			}
		} catch (Throwable t) {
			log.error("Can not load list of privacy profiles", t);
		}
	}

	/**
	 * Stores list of all privacy profiles into one file
	 * 
	 * @return Count of profiles
	 */
	private int writeListOfPrivacyProfiles() {
		int i = 1;
		this.privacyProfilesProperties.clear();
		try {
			for (PrivacyProfileItemBean profile : this.privacyProfiles) {
				this.privacyProfilesProperties.setProperty("profileName." + i, profile.getName());
				this.privacyProfilesProperties.setProperty("properties." + i, profile.getPrivacyPropertiesFile());
				i++;
			}
			this.privacyProfilesProperties.store(new OutputStreamWriter(new FileOutputStream(new File(Constants.PRIVACY_PROFILES_FILE_NAME)),
					"UTF-8"), null);
		} catch (Throwable t) {
			log.error("Can not store list of privacy profiles", t);
		}

		return i;
	}

	/**
	 * @return the privacyProfiles
	 */
	public PrivacyProfileItemBean[] getPrivacyProfiles() {
		return this.privacyProfiles.toArray(new PrivacyProfileItemBean[0]);
	}

	/**
	 * Initializes all privacy comboboxes
	 */
	public void initPrivacyCombos() {
		ListCellRenderer comboOptionRenderer = new ComboBoxOptionRenderer();

		OPTION_NO = new PrivacyItemBean(GuiTools.getLocalized("combo.no"), "no");
		OPTION_ALL = new PrivacyItemBean(GuiTools.getLocalized("combo.all"), "all");
		OPTION_LIVE = new PrivacyItemBean(GuiTools.getLocalized("combo.live"), "live");

		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO }, this.mainForm.comboHideBirth);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_NO }, this.mainForm.comboHideDeath);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO }, this.mainForm.comboHidePlaces);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO }, this.mainForm.comboHideJobs);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideEducation);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_NO }, this.mainForm.comboHideContact);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_NO }, this.mainForm.comboHideWebpages);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideReligion);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideNationality);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideUserDefined);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideRegistryInfo);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideMarriagePlace);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO }, this.mainForm.comboHideHeads);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO }, this.mainForm.comboHidePhotos);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideDocuments);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideCredentials);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideMarriageDate);
		this.initPrivacyCombo(comboOptionRenderer, new PrivacyItemBean[] { OPTION_ALL, OPTION_LIVE, OPTION_NO },
				this.mainForm.comboHideAdditionalInfo);
	}

	/**
	 * Initializes combobox by its options and renderer
	 * 
	 * @param comboOptionRenderer Renderer of combobox
	 * @param options Options to show
	 * @param comboBox Component to initialize
	 */
	private void initPrivacyCombo(ListCellRenderer comboOptionRenderer, PrivacyItemBean[] options, JComboBox comboBox) {
		ComboBoxModel comboAllLiveNoModel = new DefaultComboBoxModel(options);
		comboBox.setModel(comboAllLiveNoModel);
		comboBox.setRenderer(comboOptionRenderer);
	}

	/**
	 * Creates list of all privacy comboboxes
	 */
	public void prepareListOfAllPrivacyCombos() {
		this.mainForm.privacyCombos = new ArrayList<PrivacyComboBean>();
		addPrivacyComboBean(this.mainForm.comboHideBirth, PROPERTY_HIDE_BIRTH_DATA);
		addPrivacyComboBean(this.mainForm.comboHideDeath, PROPERTY_HIDE_DEATH_DATA);
		addPrivacyComboBean(this.mainForm.comboHidePlaces, PROPERTY_HIDE_LIVE_PLACES);
		addPrivacyComboBean(this.mainForm.comboHideJobs, PROPERTY_HIDE_JOBS);
		addPrivacyComboBean(this.mainForm.comboHideEducation, PROPERTY_HIDE_EDUCATION);
		addPrivacyComboBean(this.mainForm.comboHideContact, PROPERTY_HIDE_CONTACT);
		addPrivacyComboBean(this.mainForm.comboHideWebpages, PROPERTY_HIDE_WEB);
		addPrivacyComboBean(this.mainForm.comboHideReligion, PROPERTY_HIDE_RELIGION);
		addPrivacyComboBean(this.mainForm.comboHideNationality, PROPERTY_HIDE_NATIONALITY);
		addPrivacyComboBean(this.mainForm.comboHideUserDefined, PROPERTY_HIDE_USER_DEFINED);
		addPrivacyComboBean(this.mainForm.comboHideRegistryInfo, PROPERTY_HIDE_REGISTRY_INFO);
		addPrivacyComboBean(this.mainForm.comboHideHeads, PROPERTY_HIDE_HEADS);
		addPrivacyComboBean(this.mainForm.comboHidePhotos, PROPERTY_HIDE_PHOTOS);
		addPrivacyComboBean(this.mainForm.comboHideDocuments, PROPERTY_HIDE_DOCUMENTS);
		addPrivacyComboBean(this.mainForm.comboHideCredentials, PROPERTY_HIDE_CREDENTIALS);
		addPrivacyComboBean(this.mainForm.comboHideMarriageDate, PROPERTY_HIDE_MARRIAGE_DATA);
		addPrivacyComboBean(this.mainForm.comboHideMarriagePlace, PROPERTY_HIDE_MARRIAGE_PLACE);
		addPrivacyComboBean(this.mainForm.comboHideAdditionalInfo, PROPERTY_HIDE_ADDITIONAL_INFO);
	}

	/**
	 * Adds combobox to list of all privacy comboboxes
	 * 
	 * @param combo Combobox to add
	 * @param propertyKey Property key for storing selected combo value
	 */
	private void addPrivacyComboBean(JComboBox combo, String propertyKey) {
		this.mainForm.privacyCombos.add(new PrivacyComboBean(combo, propertyKey));
	}
}
