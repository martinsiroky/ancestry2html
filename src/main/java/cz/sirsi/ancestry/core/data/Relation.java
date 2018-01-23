//Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

/**
 * Describes relation between two people (first one is the owner (holder) of this object, second one is person set in
 * constructor).
 * 
 * @author msiroky
 */
public class Relation {
	/**
	 * Possible relations
	 * 
	 * @author msiroky
	 */
	public enum RelationEnum {
		/**
		 * Brother
		 */
		BROTHER("brother"),
		/**
		 * Daughter
		 */
		DAUGHTER("daughter"),
		/**
		 * Father
		 */
		FATHER("father"),
		/**
		 * Half brother
		 */
		HALF_BROTHER("halfBrother"),
		/**
		 * Half sister
		 */
		HALF_SISTER("halfSister"),
		/**
		 * Husband
		 */
		HUSBAND("husband"),
		/**
		 * Related person is main person (in case chain of Relation - from current person to main person - then main person
		 * is end element)
		 */
		MAIN_PERSON("mainPerson"),
		/**
		 * Partner (man) but not husband
		 */
		MAN_PARTNER("manPartner"),
		/**
		 * Mother
		 */
		MOTHER("mother"),
		/**
		 * Sister
		 */
		SISTER("sister"),
		/**
		 * Son
		 */
		SON("son"),
		/**
		 *Wife
		 */
		WIFE("wife"),
		/**
		 * Partner (woman) but not wife
		 */
		WOMAN_PARTNER("womanPartner");

		private String key;

		private RelationEnum(String key) {
			this.key = key;
		}

		/**
		 * @return Key of relation
		 */
		public String getKey() {
			return this.key;
		}
	}

	private final Person person;

	private final RelationEnum relation;

	/**
	 * Creates new Relation by given person and its relation to holder
	 * 
	 * @param person Person
	 * @param relation Relation to holder of person
	 */
	public Relation(Person person, RelationEnum relation) {
		super();
		this.person = person;
		this.relation = relation;

	}

	/**
	 * @return Person
	 */
	public Person getPerson() {
		return this.person;
	}

	/**
	 * @return Relation
	 */
	public RelationEnum getRelation() {
		return this.relation;
	}

}
