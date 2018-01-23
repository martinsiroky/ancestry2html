package cz.sirsi.ancestry.gui.main;

import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListCellRenderer;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.gui.bean.TemplateItemBean;
import cz.sirsi.ancestry.gui.renderer.ComboBoxTemplateRenderer;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * Manager for templates components and actions
 * 
 * @author msiroky
 */
public class TemplatesManager {
	/**
	 * Initializes logger for this class
	 **/
	private static Logger log = Logger.getLogger(TemplatesManager.class);

	private GuiForm mainForm;

	private List<TemplateItemBean> templates;

	private Properties templatesProfilesProperties;

	/**
	 * Instantiates a new templates manager.
	 * 
	 * @param mainForm the main form
	 */
	public TemplatesManager(GuiForm mainForm) {
		super();
		this.mainForm = mainForm;
	}

	/**
	 * Change selected template action
	 * 
	 * @param evt Event that invoked this method
	 */
	public void comboTemplateItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			this.repaintPreview();
		}
	}

	/**
	 * Initializes combobox with available templates
	 */
	public void initTemplatesCombo() {
		ListCellRenderer comboTemplateRenderer = new ComboBoxTemplateRenderer();
		ComboBoxModel comboTemplateModel = new DefaultComboBoxModel(getTemplates());
		this.mainForm.comboTemplate.setModel(comboTemplateModel);
		this.mainForm.comboTemplate.setRenderer(comboTemplateRenderer);

		for (int i = 0; i < this.mainForm.comboTemplate.getItemCount(); i++) {
			if (((TemplateItemBean) this.mainForm.comboTemplate.getItemAt(i)).getTemplatePropertiesFile().equals(
					GuiTools.getLastUsed("template", ""))) {
				this.mainForm.comboTemplate.setSelectedIndex(i);
				break;
			}
		}
	}

	/**
	 * Repaints preview when template changed
	 */
	public void repaintPreview() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File(".." + File.separator
					+ ((TemplateItemBean) this.mainForm.comboTemplate.getSelectedItem()).getPreviewImage()));
		} catch (Throwable e) {
			log.warn("Can not load preview image for selected template", e);
			// nothing, use default image
		}

		try {
			if (image == null) {
				image = ImageIO.read(GuiTools.class.getResource("/img/nopreview.jpg"));
			}
			this.mainForm.canvasPreview.setImage(image);
			this.mainForm.canvasPreview.repaint();
		} catch (IllegalArgumentException e) {
			// ignore
		} catch (Throwable e) {
			log.warn("Can not load default preview image for selected template", e);
			// nothing, remove image
		}
	}

	/**
	 * Loads list of templates from file
	 */
	public void readListOfTemplates() {
		this.templatesProfilesProperties = new Properties();

		try {
			this.templatesProfilesProperties.load(new InputStreamReader(new FileInputStream(new File(".." + File.separator + "etc"
					+ File.separator + "templates.properties")), "UTF-8"));

			this.templates = new ArrayList<TemplateItemBean>();
			int i = 1;
			while (true) {
				String templateName = this.templatesProfilesProperties.getProperty("templates." + i);
				String propertiesFile = this.templatesProfilesProperties.getProperty("properties." + i);
				String preview = this.templatesProfilesProperties.getProperty("previews." + i);
				if (templateName == null || propertiesFile == null || preview == null) {
					break;
				}

				this.templates.add(new TemplateItemBean(templateName, propertiesFile, preview));
				i++;
			}
		} catch (Throwable t) {
			log.error("Can not load list of templates", t);
		}
	}

	/**
	 * @return the templates
	 */
	public TemplateItemBean[] getTemplates() {
		return this.templates.toArray(new TemplateItemBean[0]);
	}
}
