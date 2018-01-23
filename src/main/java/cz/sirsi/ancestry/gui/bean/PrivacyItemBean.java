package cz.sirsi.ancestry.gui.bean;

/**
 * Item of combobox with settings of privacy detail
 * 
 * @author msiroky
 */
public class PrivacyItemBean {

	private String key;

	private String text;

	/**
	 * The Constructor.
	 * 
	 * @param text Textual representation of item (Yes, No etc.)
	 * @param key Key of item - used to storing for next use or for sending parameter value to generator
	 */
	public PrivacyItemBean(String text, String key) {
		super();
		this.text = text;
		this.key = key;
	}

	/**
	 * Gets the key.
	 * 
	 * @return the key
	 */
	public String getKey() {
		return this.key;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
}
