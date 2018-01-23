//Last revision 27.2.09
package cz.sirsi.ancestry.core.data.comparator;

import java.text.Collator;
import java.util.Comparator;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.Place;

/**
 * Comparator for Place object
 * 
 * @author msiroky
 */
public class PlacesComparator implements Comparator<Place> {

	/**
	 * Compares two objects by town firstly and by persons name secondly
	 * 
	 * @param firstPlace First place
	 * @param secondPlace Second place
	 * @return negative, positive or zero
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Place firstPlace, Place secondPlace) {
		Config.getInstance().getCollator().setStrength(Collator.TERTIARY);
		Collator col = Config.getInstance().getCollator();

		int result = col.compare(firstPlace.getTown(), secondPlace.getTown());
		if (result != 0) {
			return result;
		}

		PersonComparator comparator = new PersonComparator(PersonComparator.SECOND, PersonComparator.FIRST);
		return comparator.compare(firstPlace.getPerson(), secondPlace.getPerson());
	}

}
