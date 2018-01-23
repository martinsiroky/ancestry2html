package cz.sirsi.ancestry.gui.bean;

/**
 * Item of privacy profile combo (with names of profiles)
 * 
 * @author msiroky
 */
public class PrivacyProfileItemBean {

	private String name;

	private String privacyPropertiesFile;

	/**
	 * The Constructor.
	 * 
	 * @param name Name of profile
	 * @param privacyPropertiesFile File where details of profile are stored
	 */
	public PrivacyProfileItemBean(String name, String privacyPropertiesFile) {
		super();
		this.name = name;
		this.privacyPropertiesFile = privacyPropertiesFile;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the privacy properties file.
	 * 
	 * @return the privacyPropertiesFile
	 */
	public String getPrivacyPropertiesFile() {
		return this.privacyPropertiesFile;
	}
}
