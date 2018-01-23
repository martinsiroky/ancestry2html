// Last revision 27.2.09
package cz.sirsi.ancestry.core;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Holds all warnings produced while generating
 * 
 * @author msiroky
 */
public class Warnings {

	/**
	 * Singleton creation lock
	 */
	private static ReentrantLock singletonLock = new ReentrantLock();

	/**
	 * Singleton instance
	 */
	private static Warnings warningsInstance;

	/**
	 * @return Singleton instance of Tree
	 */
	public static Warnings getInstance() {
		if (warningsInstance != null) {
			return warningsInstance;
		}

		try {
			singletonLock.lock();
			if (warningsInstance == null) {
				warningsInstance = new Warnings();
			}
		} finally {
			singletonLock.unlock();
		}

		return warningsInstance;
	}

	/**
	 * List of warnings which occurred
	 */
	private List<String> warnings = new ArrayList<String>();

	private Warnings() {
		// nothing
	}

	/**
	 * @param warning the warning to add
	 */
	public void addWarning(String warning) {
		if (!this.warnings.contains(warning)) {
			this.warnings.add(warning);
		}
	}

	/**
	 * @return the warnings
	 */
	public List<String> getWarnings() {
		return this.warnings;
	}
}
