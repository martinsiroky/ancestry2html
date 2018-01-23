//Last revision 27.2.09
package cz.sirsi.ancestry.core.data.comparator;

import java.util.Comparator;

import cz.sirsi.ancestry.core.data.AdditionalFile;

/**
 * Comparator for {@link AdditionalFile}
 * 
 * @author msiroky
 */
public class AdditionalFileComparator implements Comparator<AdditionalFile> {
	/**
	 * Compares two objects by year
	 * 
	 * @param firstFile First file
	 * @param secondFile Second file
	 * @return negative, positive or zero
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(AdditionalFile firstFile, AdditionalFile secondFile) {
		if ((firstFile == null || firstFile.getYear() == null) && (secondFile == null || secondFile.getYear() == null)) {
			return 0;
		}
		if (firstFile == null || firstFile.getYear() == null) {
			return 1;
		}
		if (secondFile == null || secondFile.getYear() == null) {
			return -1;
		}
		return firstFile.getYear().compareTo(secondFile.getYear());
	}

}
