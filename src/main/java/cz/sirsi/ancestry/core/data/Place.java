// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

/**
 * Stores person and one of its place. The place can be birth, live, death or burial place of person (or any combination
 * of them).
 * 
 * @author msiroky
 */
public class Place {
	/**
	 * Constant that specifies type of place. It is birth place.
	 */
	public static final int BIRTH = 2;
	/**
	 * Constant that specifies type of place. It is burial place.
	 */
	public static final int BURIAL = 4;
	/**
	 * Constant that specifies type of place. It is death place.
	 */
	public static final int DEATH = 3;
	/**
	 * Constant that specifies type of place. It is live place.
	 */
	public static final int LIVE = 1;

	private boolean birthPlace = false;
	private boolean burialPlace = false;
	private boolean deathPlace = false;
	private boolean livePlace = false;
	private Person person;
	private String town;

	/**
	 * @param town Town
	 * @param person Person
	 * @param type Type of place (birth, live, death, burial)
	 */
	public Place(String town, Person person, int type) {
		this.town = town.trim();
		this.person = person;
		this.livePlace = type == LIVE;
		this.birthPlace = type == BIRTH;
		this.deathPlace = type == DEATH;
		this.burialPlace = type == BURIAL;
	}

	/**
	 * Adds new type to existing Place object (with the same person and town)
	 * 
	 * @param type Type to add
	 */
	public void addType(int type) {
		switch (type) {
		case BIRTH:
			this.birthPlace = true;
			break;
		case LIVE:
			this.livePlace = true;
			break;
		case DEATH:
			this.deathPlace = true;
			break;
		case BURIAL:
			this.burialPlace = true;
			break;
		}
	}

	/**
	 * Check equality of object with other object. Checks only town and person (ignores place type).
	 */
	@Override
	public boolean equals(Object obj) {
		Place other = (Place) obj;

		try {
			return (this.town.equals(other.getTown()) && (this.person.getId().equals(other.getPerson().getId())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(this.person + " ; " + other.getPerson());
			return false;
		}
	}

	/**
	 * @return the person
	 */
	public Person getPerson() {
		return this.person;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return this.town;
	}

	/**
	 * @return the birthPlace
	 */
	public boolean isBirthPlace() {
		return this.birthPlace;
	}

	/**
	 * @return the burialPlace
	 */
	public boolean isBurialPlace() {
		return this.burialPlace;
	}

	/**
	 * @return the deathPlace
	 */
	public boolean isDeathPlace() {
		return this.deathPlace;
	}

	/**
	 * @return the livePlace
	 */
	public boolean isLivePlace() {
		return this.livePlace;
	}
}
