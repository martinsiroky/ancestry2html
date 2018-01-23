package cz.sirsi.ancestry.gui.tools;

import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.MyProperties;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.gui.bean.MapsProviderItemBean;

/**
 * Tools methods for GuiForm
 * 
 * @author msiroky
 */
public class GuiTools {
	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(GuiTools.class);

	private static final String ANCESTRY2HTML_WORK_FOLDER = ".ancestry2html";

	private static MyProperties iniProperties;
	private static String lang = null;
	private static boolean languageNotSet = false;

	private static List<MapsProviderItemBean> maps;

	private static Properties mapsProperties;
	private static String rodSendFromAncestry = null;

	/**
	 * @return Array of all available languages for combobox (get from messages csv file header)
	 */
	public static String[] getAvailableLanguages() {
		String[] languages = MessagesTools.getWebMessages().getAvailableLanguages().toArray(new String[0]);
		Arrays.sort(languages);
		return languages;
	}

	/**
	 * @return Current language of gui window
	 */
	public static String getLang() {
		return lang;
	}

	/**
	 * Gets last used value of property
	 * 
	 * @param key Property key
	 * @param defaultValue Default value in case last used value is not found
	 * @return Last used value of property
	 */
	public static String getLastUsed(String key, String defaultValue) {
		return iniProperties.getProperty(key, defaultValue);
	}

	/**
	 * Gets last used value of property as boolean
	 * 
	 * @param key Property key
	 * @param defaultValue Default value in case last used value is not found
	 * @return Last used value as boolean
	 */
	public static boolean getLastUsedBoolean(String key, boolean defaultValue) {
		return "true".equalsIgnoreCase(getLastUsed(key, Boolean.toString(defaultValue)));
	}

	/**
	 * @return Last used output directory
	 */
	// TODO maybe remove
	public static String getLastUsedOutput() {
		return iniProperties.getProperty("outputDir", "");
	}

	/**
	 * @return Last used rod file
	 */
	// TODO maybe remove
	public static String getLastUsedRod() {
		return rodSendFromAncestry != null ? rodSendFromAncestry : iniProperties.getProperty("rodFile", "");
	}

	/**
	 * @return Last used town position in address
	 */
	public static int getLastUsedTownPositionInAddress() {
		return iniProperties.getIntProperty("townPositionInAddress", 2);
	}

	/**
	 * @return Last used town position in birth
	 */
	public static int getLastUsedTownPositionInBirth() {
		return iniProperties.getIntProperty("townPositionInBirth", 1);
	}

	/**
	 * @param key Key
	 * @return Localized message by key
	 */
	public static String getLocalized(final String key) {
		return getLocalized(key, null, null);
	}

	/**
	 * @param key Localization key
	 * @param defaultValue DEfault value in case localization can not be found
	 * @param params Parameters to fill in into the message
	 * @return Localized message by key, in case it is not found return defaultValue
	 */
	public static String getLocalized(final String key, String defaultValue, String[] params) {
		return MessagesTools.getGuiMessages().getMessage(key, defaultValue, params, false);
	}

	/**
	 * @return the maps
	 */
	public static List<MapsProviderItemBean> getMaps() {
		return maps;
	}

	/**
	 * @return the paramRod
	 */
	public static boolean isRodSendFromAncestry() {
		return rodSendFromAncestry != null;
	}

	/**
	 * Gets list of selected languages as one semiclon separated string
	 * 
	 * @param selectedValues Array of selected languages
	 * @return list of selected languages as one semiclon separated string
	 */
	public static String getSelectedLanguagesAsString(Object[] selectedValues) {
		StringBuffer sb = new StringBuffer();
		for (Object selected : selectedValues) {
			sb.append((String) selected);
			sb.append(";");
		}
		sb.setLength(sb.length() - 1);
		return sb.toString();
	}

	/**
	 * Initializes logger to INFO or DEBUG
	 */
	public static void initializeLogger() {
		Logger root = Logger.getRootLogger();

		Appender appender = null;
		if ("true".equals(GuiTools.getLastUsed(Constants.PROPERTY_ENABLE_DEBUG, "false"))) {
			root.setLevel(Level.DEBUG);
			appender = root.getAppender("debugAppender");
		} else {
			root.setLevel(Level.INFO);
			appender = root.getAppender("infoAppender");
		}

		@SuppressWarnings("unchecked")
		Enumeration<Appender> allAppenders = root.getAllAppenders();
		while (allAppenders.hasMoreElements()) {
			Appender app = allAppenders.nextElement();
			if (!app.equals(appender)) {
				root.removeAppender(app);
			}
		}
	}

	/**
	 * Loads localization file
	 */
	public static void initLang() {
		if (lang == null) {
			lang = GuiTools.getLastUsed(Constants.PROPERTY_GUI_LANGUAGE, null);
			if (lang == null) {
				lang = "cz";
				languageNotSet = true;
			}
		}

		MessagesTools.getGuiMessages().initMessagesIfNeeded();
		MessagesTools.getGuiMessages().setCurrentLanguage(lang);
	}

	/**
	 * In case gui language was not set before (on first use of application)
	 * 
	 * @return True if language was not selected before
	 */
	public static boolean isLanguageNotSet() {
		return languageNotSet;
	}

	/**
	 * Localizes checkbox
	 * 
	 * @param checkbox Checkbox to localize
	 * @param key Localization key
	 */
	public static void localizeCheckbox(JCheckBox checkbox, String key) {
		// $hide>>$
		checkbox.setText(getLocalized(key));

		String tip = getLocalized(key + ".tip", "", null);
		if (tip == null || tip.length() == 0) {
			checkbox.setFont(checkbox.getFont().deriveFont(Font.PLAIN));
			return;
		}

		checkbox.setFont(checkbox.getFont().deriveFont(Font.ITALIC));
		checkbox.setToolTipText(tip);
		// $hide<<$
	}

	/**
	 * Localizes label
	 * 
	 * @param label Label to lacalize
	 * @param key Localization key
	 */
	public static void localizeLabel(JLabel label, String key) {
		// $hide>>$
		label.setText(getLocalized(key));

		String tip = getLocalized(key + ".tip", "", null);
		if (tip == null || tip.length() == 0) {
			label.setFont(label.getFont().deriveFont(Font.PLAIN));
			return;
		}

		label.setFont(label.getFont().deriveFont(Font.ITALIC));
		label.setToolTipText(tip);
		// $hide<<$
	}

	/**
	 * Reads initialization properties (last used values) on start of application
	 * 
	 * @param path Path to ini properties file, if null use default user home directory
	 */
	public static void readIniProperties(String path) {
		try {
			String pathToIni = null;
			File file = null;
			if (path != null) {
				pathToIni = path;
				file = new File(pathToIni, ".Ancestry2html.properties");
			} else {
				pathToIni = System.getProperty("user.home") + File.separator + ANCESTRY2HTML_WORK_FOLDER;
				file = new File(pathToIni, ".Ancestry2html.properties");

				// old path, used for back compatibility
				if (!file.exists()) {
					pathToIni = System.getProperty("user.home");
					file = new File(pathToIni, ".Ancestry2html.properties");
					file.deleteOnExit();
				}
			}

			iniProperties = new MyProperties();
			iniProperties.load(new FileInputStream(file));
		} catch (Throwable t) {
			log.debug("Can not load last used values. Used default.", t);
		}
	}

	/**
	 * Loads list of templates from file
	 */
	public static void readMapsProperties() {
		mapsProperties = new Properties();

		try {
			mapsProperties.load(new InputStreamReader(new FileInputStream(new File(".." + File.separator + "etc" + File.separator
					+ "maps.properties")), "UTF-8"));

			maps = new ArrayList<MapsProviderItemBean>();
			int i = 1;
			while (true) {
				String name = mapsProperties.getProperty("map.name." + i);
				String url = mapsProperties.getProperty("map.url." + i);
				if (name == null || url == null) {
					break;
				}

				maps.add(new MapsProviderItemBean(i, name, url));
				i++;
			}
		} catch (Throwable t) {
			log.error("Can not load list of templates", t);
		}
	}

	/**
	 * Stores initialization properties for next running of application
	 * 
	 * @param path Path to ini properties file, if null use default user home directory
	 */
	public static void saveIniProperties(String path) {
		try {
			String pathToIni = path != null ? path : System.getProperty("user.home") + File.separator + ANCESTRY2HTML_WORK_FOLDER;
			(new File(pathToIni)).mkdirs();
			iniProperties.store(new FileOutputStream(new File(pathToIni, ".Ancestry2html.properties")), "");
		} catch (Throwable t) {
			log.error("Can not store last used values", t);
		}
	}

	/**
	 * @param lang Sets the language
	 */
	public static void setLang(String lang) {
		GuiTools.lang = lang;
	}

	/**
	 * Sets used value of key for the next use
	 * 
	 * @param key Key
	 * @param value Value
	 */
	public static void setLastUsed(String key, String value) {
		iniProperties.setProperty(key, value);
	}

	/**
	 * Stores boolean value to of the property to properties for nwxt use
	 * 
	 * @param properties Stores all properties for next use
	 * @param property Property key
	 * @param value Value to store
	 */
	public static void storeProperty(Properties properties, String property, boolean value) {
		storeProperty(properties, property, Boolean.toString(value));
	}

	/**
	 * Stores string value to of the property to properties for nwxt use
	 * 
	 * @param properties Stores all properties for next use
	 * @param property Property key
	 * @param value Value to store
	 */
	public static void storeProperty(Properties properties, String property, String value) {
		setLastUsed(property, value);
		properties.setProperty(property, value);
	}

	/**
	 * Loads last used value of checkbox
	 * 
	 * @param checkBox Checkbox instance
	 * @param property Property key
	 * @param defaultValue DEfault value (if not found in configuration)
	 */
	public static void loadLastBooleanValue(JCheckBox checkBox, String property, boolean defaultValue) {
		checkBox.setSelected(GuiTools.getLastUsedBoolean(property, defaultValue));
	}

	/**
	 * @param rodSendFromAncestry Rod file that was send from Ancestry as parameter
	 */
	public static void setRodSendFromAncestry(String rodSendFromAncestry) {
		GuiTools.rodSendFromAncestry = rodSendFromAncestry;
	}
}
