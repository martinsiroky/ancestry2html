package cz.sirsi.ancestry.gui;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Filter used in open-dialog. Each filter has its description and allowed extension.
 * 
 * @author msiroky
 */
public class OpenDialogFileFilter extends FileFilter {

	private String description;

	private String ext;

	/**
	 * Construct instance of filter
	 * 
	 * @param ext Extension
	 * @param description Description of file type
	 */
	public OpenDialogFileFilter(final String ext, final String description) {
		this.description = description;
		this.ext = ext;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean accept(final File f) {
		if (f.isDirectory()) {
			return true;
		}

		String s = f.getName();
		int i = s.lastIndexOf('.');

		if ((i > 0) && (i < (s.length() - 1))) {
			if (s.substring(i + 1).toLowerCase().equals(this.ext)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return this.description;
	}
}
