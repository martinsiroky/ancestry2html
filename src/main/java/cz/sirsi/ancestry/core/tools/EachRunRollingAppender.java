/**
 * 
 */
package cz.sirsi.ancestry.core.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.LogLog;

/**
 * Creates new log file on each run of application (on each initialization of the appender)
 * 
 * @author msiroky
 */
public class EachRunRollingAppender extends RollingFileAppender {

	private String prefix;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	/**
	 * Initializes deleting of old log files. TODO should be maybe moved to other method that is called after all setters (from configuration)
	 */
	@Override
	protected OutputStreamWriter createWriter(OutputStream os) {

		deleteOldest(this.prefix);

		return super.createWriter(os);
	}

	/**
	 * Deletes the oldest backup log file. The oldest backup file is the file that starts with the specified file name
	 * 
	 * @param prefixFilePath Prefix of log file load from log4j configuration (included path - e.g. "C:\tmp\info_")
	 */
	private void deleteOldest(final String prefixFilePath) {
		final File prefixFile = new File(prefixFilePath);

		File dir = prefixFile.getAbsoluteFile().getParentFile();

		File[] backupFiles = dir.listFiles(new FileFilter() {
			public boolean accept(File filePath) {
				return filePath.getName().startsWith(prefixFile.getName());
			}
		});

		if (backupFiles.length > this.maxBackupIndex) {
			// sort the backup files by their last-modified timestamp in ascending order
			Arrays.sort(backupFiles, new Comparator<File>() {
				public int compare(File file1, File file2) {
					long lastMod1 = file1.lastModified();
					long lastMod2 = file2.lastModified();

					if (lastMod1 == lastMod2) {
						return 0;
					}

					return lastMod1 < lastMod2 ? -1 : 1;
				}
			});

			// delete the oldest backup file(s)
			for (int i = 0; i <= backupFiles.length - this.maxBackupIndex; i++) {
				LogLog.debug("Deleting backup file " + backupFiles[i]);
				backupFiles[i].delete();
			}
		}
	}

	/**
	 * Sets given file with added date-time
	 */
	@Override
	public void setFile(String file) {
		this.prefix = file;

		String newName = file + this.sdf.format(new Date()) + ".log";

		super.setFile(newName);
	}

}
