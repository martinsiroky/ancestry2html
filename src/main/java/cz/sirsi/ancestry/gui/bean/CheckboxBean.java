package cz.sirsi.ancestry.gui.bean;

import javax.swing.JCheckBox;

/**
 * Bean for storing all data of check box
 * 
 * @author msiroky
 */
public class CheckboxBean {

	private JCheckBox checkBox;

	private String propertyKey;

	private String localizationKey;

	private boolean defaultValue;

	/**
	 * Instantiates a new checkbox bean.
	 * 
	 * @param checkBox the checkbox instance
	 * @param propertyKey the property key to load/store values for previous/next use
	 * @param localizationKey the localization key
	 * @param defaultValue the default value of checkbox
	 */
	public CheckboxBean(JCheckBox checkBox, String propertyKey, String localizationKey, boolean defaultValue) {
		super();
		this.checkBox = checkBox;
		this.propertyKey = propertyKey;
		this.localizationKey = localizationKey;
		this.defaultValue = defaultValue;
	}

	/**
	 * Gets the check box.
	 * 
	 * @return the check box
	 */
	public JCheckBox getCheckBox() {
		return this.checkBox;
	}

	/**
	 * Gets the localization key.
	 * 
	 * @return the localization key
	 */
	public String getLocalizationKey() {
		return this.localizationKey;
	}

	/**
	 * Gets the property key.
	 * 
	 * @return the property key
	 */
	public String getPropertyKey() {
		return this.propertyKey;
	}

	/**
	 * Gets the default value.
	 * 
	 * @return the default value
	 */
	public boolean getDefaultValue() {
		return this.defaultValue;
	}
}
