package cz.sirsi.ancestry.gui.bean;

import javax.swing.JComboBox;

/**
 * Bean for storing all data of privacy combo box
 * 
 * @author msiroky
 */
public class PrivacyComboBean {

	private JComboBox comboBox;

	private String propertyKey;

	/**
	 * Instantiates a new privacy combo bean.
	 * 
	 * @param comboBox The combo box
	 * @param propertyKey The key of property to store/load status
	 */
	public PrivacyComboBean(JComboBox comboBox, String propertyKey) {
		super();
		this.comboBox = comboBox;
		this.propertyKey = propertyKey;
	}

	/**
	 * Gets the combo box.
	 * 
	 * @return the combo box
	 */
	public JComboBox getComboBox() {
		return this.comboBox;
	}

	/**
	 * Gets the property key.
	 * 
	 * @return the property key
	 */
	public String getPropertyKey() {
		return this.propertyKey;
	}
}
