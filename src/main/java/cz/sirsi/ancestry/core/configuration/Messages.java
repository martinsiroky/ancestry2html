package cz.sirsi.ancestry.core.configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.ChoiceFormat;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.tools.Tools;

/**
 * Stores messages for localization loaded from file
 * 
 * @author msiroky
 */
public class Messages {
	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(Messages.class);

	private String currentLanguage;

	private File i18nFile;

	private Map<String, Map<String, String>> languagesMessagesMap;

	/**
	 * Last modification time of localization file (for reloading)
	 */
	private long lastModified = 0;

	/**
	 * Creates new instance for given localization file
	 * 
	 * @param i18nFile Localization file
	 */
	public Messages(File i18nFile) {
		this.i18nFile = i18nFile;
	}

	/**
	 * @return Set of available language codes
	 */
	public Set<String> getAvailableLanguages() {
		this.initMessagesIfNeeded();
		return this.languagesMessagesMap.keySet();
	}

	/**
	 * @return the currentLanguage
	 */
	public String getCurrentLanguage() {
		return this.currentLanguage;
	}

	/**
	 * Gets message for currently set language
	 * 
	 * @param args List of parameters - first item is key, the next are parameters of the message
	 * @return Translated message
	 */
	public String getMessage(List<Object> args) {
		return getMessage((String) args.get(0), null, args.subList(1, args.size()).toArray(), true);
	}

	/**
	 * Gets message for currently set language
	 * 
	 * @param key Key of message
	 * @param defaultValue Default value to use if message not found for key
	 * @param params Parameters of message
	 * @param tryTransformParams Tries to convert parameters to Integer (in case it is called from ftl then all parameters are Strings, so for
	 *          chooseFormat we need to convert numbers to Integer)
	 * @return Translated message
	 */
	public String getMessage(final String key, String defaultValue, Object[] params, boolean tryTransformParams) {
		String message = this.languagesMessagesMap.get(this.currentLanguage).get(key);
		String defaultVal = defaultValue;
		if (defaultVal == null) {
			defaultVal = "??" + key + "??";
		}

		if (message == null) {
			message = defaultVal;
		}

		MessageFormat format = null;
		try {
			format = new MessageFormat(message);
		} catch (Exception e) {
			log.error("Wrong message \"" + message + "\" for formating. Key=\"" + key + "\"");
			return message;
		}

		if (params != null) {

			if (tryTransformParams) {
				for (int i = 0; i < params.length; i++) {
					try {
						Integer number = Integer.parseInt(params[i].toString());
						params[i] = number;
						if (!(format.getFormatsByArgumentIndex()[i] instanceof ChoiceFormat)) {
							format.setFormatByArgumentIndex(i, new DecimalFormat("####"));
						}
					} catch (NumberFormatException e) {
						// nothing
					}
				}
			}

			message = format.format(params);
		}

		return message;
	}

	/**
	 * Initializes messages (read them from localization file)
	 */
	private void initMessages() {
		this.languagesMessagesMap = new HashMap<String, Map<String, String>>();

		BufferedReader reader = null;
		reader = initMessagesReader(reader);
		if (reader == null) {
			return;
		}

		try {
			List<Map<String, String>> languagesMessagesList = readMessagesHeaderWithLanguages(reader);

			StringTokenizer st;
			String line;

			// load all messages
			while ((line = reader.readLine()) != null) {
				// skip empty line
				if (line.trim().length() == 0) {
					continue;
				}

				line = line.replace("\";\"", "\"\t\"").replaceFirst(";$", "");
				st = new StringTokenizer(line, "\t");

				// read key
				String key = Tools.removeQuotas(st.nextToken());
				int index = 0;

				// read all messages for the key
				while (st.hasMoreTokens()) {
					if (index == languagesMessagesList.size()) {
						break;
					}
					String message = Tools.removeQuotas(st.nextToken());
					languagesMessagesList.get(index).put(key, message);
					index++;
				}
			}
		} catch (IOException e) {
			log.error("Error reading messages file", e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	/**
	 * Initializes messages map only if needed (localization file has changed)
	 */
	public void initMessagesIfNeeded() {
		if (this.i18nFile.lastModified() != this.lastModified) {
			initMessages();
			this.lastModified = this.i18nFile.lastModified();
		}
	}

	private BufferedReader initMessagesReader(BufferedReader reader) {
		BufferedReader messagesReader = reader;
		try {
			messagesReader = new BufferedReader(new InputStreamReader(new FileInputStream(this.i18nFile), "UTF-8"));

		} catch (FileNotFoundException e) {
			log.error("Can not found file with messages.", e);
		} catch (UnsupportedEncodingException e) {
			log.error("Can not read file with messages.", e);
		}
		return messagesReader;
	}

	/**
	 * 
	 * @param reader Reader to read messages from
	 * @return
	 * @throws IOException
	 */
	public List<Map<String, String>> readMessagesHeaderWithLanguages(BufferedReader reader) throws IOException {
		List<Map<String, String>> languagesMessagesList = new ArrayList<Map<String, String>>();

		// read table header
		String line = reader.readLine();
		line = line.replace("\";\"", "\"\t\"").replaceFirst(";$", "");
		StringTokenizer st = new StringTokenizer(line, "\t");
		// skip first cell
		st.nextElement();

		// load all languages
		while (st.hasMoreTokens()) {
			String language = Tools.removeQuotas(st.nextToken());
			Map<String, String> messagesMap = new HashMap<String, String>();
			this.languagesMessagesMap.put(language, messagesMap);
			languagesMessagesList.add(messagesMap);
		}

		return languagesMessagesList;
	}

	/**
	 * @param currentLanguage the currentLanguage to set
	 */
	public void setCurrentLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}
}
