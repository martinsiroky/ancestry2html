package cz.sirsi.ancestry.core.data.comparator;

import java.text.Collator;
import java.util.Comparator;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.Person;
import cz.sirsi.ancestry.core.tools.DateUtils;

/**
 * Comparator for comparing two objects of Person
 * 
 * @author msiroky
 */
public class PersonComparator implements Comparator<Person> {

	/**
	 * Constant for sorting by birth date
	 */
	public static final int BIRTH = 6;

	/**
	 * Constant for sorting by first name
	 */
	public static final int FIRST = 2;

	/**
	 * Constant for sorting by hierarchy
	 */
	public static final int HIERARCHY = 5;

	/**
	 * Constant for sorting by id
	 */
	public static final int ID = 7;

	/**
	 * Constant for sorting by last name
	 */
	public static final int LAST = 3;

	/**
	 * Constant for sorting by display name
	 */
	public static final int NAME = 1;

	/**
	 * Constant for sorting by maiden name
	 */
	public static final int SECOND = 4;

	/**
	 * Constant for sorting by temp value - used for partners sorting
	 */
	public static final int TEMP_ORDER = 8;

	/**
	 * Order of sorting priorities
	 */
	private int[] order;

	/**
	 * Construct new comparator. Parameter(s) specifies sorting priority (e.g. PersonComparator(LAST,-FIRST) means sort by
	 * last name ascending with priority 1 and by first name descending with priority 2. Positive constant means
	 * ascending, negative descending)
	 * 
	 * @param order
	 */
	public PersonComparator(int... order) {
		this.order = order;
	}

	/**
	 * Compares two instances of User
	 * 
	 * @param person1 First object
	 * @param person2 Second object
	 * 
	 * @return -1 in case o1 is less than o2, 1 in opposite case and 0 in case both objects are the same
	 */
	public int compare(Person person1, Person person2) {
		Config.getInstance().getCollator().setStrength(Collator.TERTIARY);
		Collator col = Config.getInstance().getCollator();

		int result = 0;

		for (final int field : this.order) {
			switch (Math.abs(field)) {
			case LAST:
				result = field * col.compare(person1.getLastname(), person2.getLastname());

				break;
			case SECOND:
				result = field
						* col.compare(person1.getSecondname() != null ? person1.getSecondname() : "",
								person2.getSecondname() != null ? person2.getSecondname() : "");

				break;

			case FIRST:
				result = field
						* col.compare(person1.getFirstname() != null ? person1.getFirstname() : "",
								person2.getFirstname() != null ? person2.getFirstname() : "");

				break;
			case NAME:
				result = field * col.compare(person1.getName(), person2.getName());

				break;
			case ID:
				result = field * person1.getId().compareTo(person2.getId());

				break;
			case HIERARCHY:
				result = field
						* (person1.getHierarchy() < person2.getHierarchy() ? -1
								: person1.getHierarchy() > person2.getHierarchy() ? 1 : 0);

				break;
			case BIRTH:
				result = field * (DateUtils.compareTwoDates(person1.getBirthDateObj(), person2.getBirthDateObj()));

				break;
			case TEMP_ORDER:
				result = field * (person1.getTemp() < person2.getTemp() ? -1 : person1.getTemp() > person2.getTemp() ? 1 : 0);

				break;
			}

			if (result != 0) {
				return result;
			}
		}

		return person1.getId().compareTo(person2.getId());
	}
}
