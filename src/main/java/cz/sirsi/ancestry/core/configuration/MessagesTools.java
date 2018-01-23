// Last revision 20.4.08
package cz.sirsi.ancestry.core.configuration;

import java.io.File;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author msiroky
 * 
 */
public class MessagesTools {
	/**
	 * Messages for gui
	 */
	private static Messages guiMessages;

	private static final File LANGUAGE_FILE_GUI = new File(".." + File.separator + "lang" + File.separator
			+ "guiMessages.csv");

	private static final File LANGUAGE_FILE_HTML = new File(".." + File.separator + "lang" + File.separator
			+ "htmlMessages.csv");

	/**
	 * Lock for initializing messages
	 */
	private static ReentrantLock lock = new ReentrantLock();

	/**
	 * Messages for web
	 */
	private static Messages webMessages;

	/**
	 * @return instance of Messages for gui
	 */
	public static Messages getGuiMessages() {
		if (guiMessages != null) {
			return guiMessages;
		}

		try {
			lock.lock();
			if (guiMessages == null) {
				guiMessages = new Messages(LANGUAGE_FILE_GUI);
			}
		} finally {
			lock.unlock();
		}

		return guiMessages;
	}

	/**
	 * @return instance of Messages for web
	 */
	public static Messages getWebMessages() {
		if (webMessages != null) {
			return webMessages;
		}

		try {
			lock.lock();
			if (webMessages == null) {
				webMessages = new Messages(LANGUAGE_FILE_HTML);
			}
		} finally {
			lock.unlock();
		}

		return webMessages;
	}
}
