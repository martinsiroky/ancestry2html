package cz.sirsi.ancestry.core;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Filter for getting files by regexp
 * 
 * @author msiroky
 */
public class MyFileFilter implements FilenameFilter {

	private String regexp;

	/**
	 * Creates instance of filter
	 * 
	 * @param regexp
	 */
	public MyFileFilter(String regexp) {
		this.regexp = regexp;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String file) {
		if (new File(dir, file).isDirectory()) {
			return false;
		}

		return file.matches(this.regexp);
	}
}
