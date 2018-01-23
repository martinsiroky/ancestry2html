package cz.sirsi.ancestry.gui.bean;

/**
 * Object for combobox to setting checking new version period
 * 
 * @author msiroky
 */
public class CheckVersionItemBean {

	private int seconds;

	private String text;

	/**
	 * The Constructor.
	 * 
	 * @param text Text representation in combo
	 * @param seconds Number of second to wait between checking new version
	 */
	public CheckVersionItemBean(String text, int seconds) {
		super();
		this.seconds = seconds;
		this.text = text;
	}

	/**
	 * Gets the seconds.
	 * 
	 * @return Seconds betwen tho check
	 */
	public int getSeconds() {
		return this.seconds;
	}

	/**
	 * Gets the text.
	 * 
	 * @return Text to show in combo
	 */
	public String getText() {
		return this.text;
	}
}
