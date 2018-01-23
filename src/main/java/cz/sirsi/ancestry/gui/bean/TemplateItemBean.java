package cz.sirsi.ancestry.gui.bean;

import java.io.File;

/**
 * Item of combo with templates
 * 
 * @author msiroky
 */
public class TemplateItemBean {

	private String name;

	private String templatePropertiesFile;

	private String previewImage;

	/**
	 * The Constructor.
	 * 
	 * @param name Name of the template
	 * @param templatePropertiesFile Configuration file for this template
	 * @param previewImage Preview image of this template
	 */
	public TemplateItemBean(String name, String templatePropertiesFile, String previewImage) {
		super();
		this.name = name;
		this.templatePropertiesFile = templatePropertiesFile.replace('/', File.separatorChar).replace('\\',
				File.separatorChar);
		this.previewImage = previewImage.replace('/', File.separatorChar).replace('\\', File.separatorChar);
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
	 * Gets the preview image.
	 * 
	 * @return the previewImage
	 */
	public String getPreviewImage() {
		return this.previewImage;
	}

	/**
	 * Gets the template properties file.
	 * 
	 * @return the templatePropertiesFile
	 */
	public String getTemplatePropertiesFile() {
		return this.templatePropertiesFile;
	}
}
