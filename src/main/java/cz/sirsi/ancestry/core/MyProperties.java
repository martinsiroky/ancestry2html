// Last revision 27.2.09
package cz.sirsi.ancestry.core;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Adds some useful methods to standard Properties
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class MyProperties extends Properties {
	/**
	 * Logger
	 */
	private static Logger log = Logger.getLogger(MyProperties.class);

	/**
	 * Gets integer value of key from properties
	 * 
	 * @param key Key of property
	 * @param defaultValue Default value
	 * @return Value as int (or default value if value for key not found)
	 */
	public int getIntProperty(String key, int defaultValue) {
		try {
			return Integer.parseInt(getProperty(key));
		} catch (Throwable t) {
			return defaultValue;
		}
	}

	/**
	 * Gets boolean value of key from properties
	 * 
	 * @param key Key of property
	 * @return Value as boolean (or true if value for key not found)
	 */
	public boolean getBooleanProperty(String key) {
		try {
			return Boolean.parseBoolean(getProperty(key));
		} catch (Throwable t) {
			return true;
		}
	}

	/**
	 * Gets property that specifies some path. Replaces all occurrences of \ and / to File.separatorChar
	 * 
	 * @param key Key of property
	 * @return Converted path
	 */
	public String getPathProperty(String key) {
		String path = getProperty(key);
		if (path == null) {
			return null;
		}

		// TODO on linux \ is allowed in file name
		return path.replace('/', File.separatorChar).replace('\\', File.separatorChar);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.util.Properties#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String key) {
		String value = super.getProperty(key);
		if (log.isDebugEnabled()) {
			log.debug("read property: " + key + "=" + value);
		}
		return value;
	}
}
