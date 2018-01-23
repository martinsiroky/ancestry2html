/**
 * 
 */
package cz.sirsi.ancestry.core;

/**
 * @author msiroky
 */
public enum PersonIntervalTypeEnum {
	/**
	 * DOCUMENT ME!
	 */
	ALL_GRANDCHILD_DAYS("ALL_GRANDCHILD_DAYS"),
	/**
	 * DOCUMENT ME!
	 */
	ALL_GREATGRANDCHILD_DAYS("ALL_GREATGRANDCHILD_DAYS"),
	/**
	 * For computing youngest/oldes mother/father
	 */
	ALL_CHILD_DAYS("ALL_CHILD_DAYS"),
	/**
	 * For computing youngest/oldes nev�sta/�enich
	 */
	ALL_WEDDING_DAYS("ALL_WEDDING_DAYS"),
	/**
	 * DOCUMENT ME!
	 */
	FIRST_GRANDCHILD_DAY("COMPUTE_FIRST_GRANDCHILD_DAY"),
	/**
	 * DOCUMENT ME!
	 */
	FIRST_GREATGRANDCHILD_DAY("FIRST_GREATGRANDCHILD_DAY"),
	/**
	 * For computing oldest mother/father on first child
	 */
	FIRST_CHILD_DAY("COMPUTE_FIRST_CHILD_DAY"),
	/**
	 * For computing average age of nev�sta/�enich
	 */
	FIRST_WEDDING_DAY("COMPUTE_FIRST_WEDDING_DAY"),
	/**
	 * For computing length of live
	 */
	LIVE_LENGTH("LIVE_LENGTH");

	private String name;

	private PersonIntervalTypeEnum(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
}
