package cz.sirsi.ancestry.core.tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.main.Main;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * Tools methods for unzipping file (project *.rodz)
 * 
 * @author msiroky
 */
public class Unzip {
	/**
	 * Initializes logger for this class
	 **/
	private static Logger log = Logger.getLogger(Unzip.class);

	/**
	 * Copy inputStream to outputStream
	 * 
	 * @param in Input stream
	 * @param out Output stream
	 * @throws IOException In case copying failed
	 */
	public static final void copyInputStream(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}

		in.close();
		out.close();
	}

	/**
	 * Unzip given file to given directory by external application that can manage passwords in zip.
	 * 
	 * @param file File that extract
	 * @param extractDir Directory to extract to
	 * @throws AppException In case extracting failed
	 */
	// TODO find pure java solution.
	private static void extractExternallyWithPassword(File file, File extractDir) throws AppException {
		JPasswordField jpf = new JPasswordField(30);
		JPanel messagePanel = new JPanel();
		messagePanel.setLayout(new BoxLayout(messagePanel, BoxLayout.Y_AXIS));
		messagePanel.add(new JLabel(GuiTools.getLocalized("passwordRequired")));
		messagePanel.add(jpf);
		int result = JOptionPane.showConfirmDialog(null, messagePanel, GuiTools.getLocalized("passwordRequired.title"),
				JOptionPane.DEFAULT_OPTION);

		if (result != 0) {
			throw new AppException("Password canceled. File can not be opened.");
		}

		String password = new String(jpf.getPassword());

		try {
			Process process = Runtime.getRuntime().exec(
					"\"" + new File(".." + File.separator + "lib" + File.separator + "unrodz.exe").getAbsolutePath() + "\" \""
							+ file.getAbsolutePath() + "\" \"" + extractDir.getAbsolutePath() + "\" " + password);
			process.waitFor();

			if (process.exitValue() == 2) {
				log.error("Wrong password. Can not extract rodz.");
				throw new AppException("Error opennig file. Wrong password was typed.");
			}
			if (process.exitValue() == 1) {
				log.error("File " + file.getName() + " does not exist.");
				throw new AppException("Error opennig file. It is corrupted or missing.");
			}

		} catch (IOException e) {
			log.error("Unexpected error by running unpacker unrodz.", e);
			throw new AppException("Error opennig file. It is corrupted or wrong password was typed.", e);
		} catch (InterruptedException e) {
			log.error("Error waiting for external unpacker unrodz.", e);
			throw new AppException("Error opennig file. It is corrupted or wrong password was typed.", e);
		}
	}

	/**
	 * Unzip given file to given directory. !!! Has problem with special czech chars in zipped files.
	 * 
	 * @param file File that extract
	 * @param extractDir Directory to extract to
	 * @throws AppException In case extracting failed
	 */
	public static final void unzip(File file, File extractDir) throws AppException {
		log.info("Start unzipping rodz");
		Main.status.printOutputMessage("unzipping.rodz");
		if (!extractDir.exists()) {
			extractDir.mkdirs();
		}

		Enumeration<? extends ZipEntry> entries;
		ZipFile zipFile;

		try {
			zipFile = new ZipFile(file);

			entries = zipFile.entries();

			while (entries.hasMoreElements()) {
				ZipEntry entry = entries.nextElement();

				File outputFile = new File(extractDir, entry.getName());

				if (entry.isDirectory()) {
					continue;
				}

				log.info("Extracting file: " + entry.getName());
				if (!outputFile.getParentFile().exists()) {
					outputFile.getParentFile().mkdirs();
				}
				copyInputStream(zipFile.getInputStream(entry), new BufferedOutputStream(new FileOutputStream(outputFile)));
			}

			zipFile.close();
			log.info("Unzipping rodz done");
		} catch (ZipException e) {
			extractExternallyWithPassword(file, extractDir);
		} catch (Throwable e) {
			log.error("Error unzipping file " + file.getAbsolutePath(), e);
			throw new AppException("Error unzipping file " + file.getAbsolutePath(), e);
		}
	}

	/**
	 * Unzip given file to given directory. Has not problem with special czech chars in zipped files.
	 * 
	 * @param file File that extract
	 * @param extractDir Directory to extract to
	 * @throws AppException In case extracting failed
	 */
	public static final void unzipNew(File file, File extractDir) throws AppException {
		// log.info("Start unzipping rodz");
		// Main.status.setProcessStatus("unzip.rodz");
		// Main.status.printOutputMessageDirectly("progress.unzipping.rodz");
		//		
		// if (!extractDir.exists()) {
		// extractDir.mkdirs();
		// }
		//
		// // TODO parametrized encoding
		// ArchiveDetector detector = new DefaultArchiveDetector(ArchiveDetector.NULL, new Object[] { "rodz",
		// new de.schlichtherle.io.archive.zip.CheckedZip32Driver("Cp1250") });
		// de.schlichtherle.io.File file2 = new de.schlichtherle.io.File(file, detector);
		//
		// boolean ok = file2.copyAllTo(new de.schlichtherle.io.File(extractDir));
		//
		// if (!ok) {
		// extractExternallyWithPassword(file, extractDir);
		// }
		//
		// Main.status.setProcessStatus("unzip.rodz.done");
		// log.info("Unzipping rodz done");
	}
}
