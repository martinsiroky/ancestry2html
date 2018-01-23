package cz.sirsi.ancestry.gui.bean;

/**
 * Item of combo with map providers
 * 
 * @author msiroky
 */
public class MapsProviderItemBean {

	private Integer id;

	private String name;

	private String url;

	/**
	 * The Constructor.
	 * 
	 * @param id Identifier (used for storing last used item)
	 * @param name Name shown in combo (Seznam, Google etc.)
	 * @param url Url of provider of maps
	 */
	public MapsProviderItemBean(int id, String name, String url) {
		super();
		this.id = id;
		this.name = name;
		this.url = url;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
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
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public String getUrl() {
		return this.url;
	}
}
