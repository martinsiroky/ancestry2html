package cz.sirsi.ancestry.core.load.rod;

/**
 * @author msiroky
 */
public enum GlobalTagEnum {

	/**
	 * Tag that stores id of active person of the document
	 */
	ACTIVE("[aktivni]", "setActive"),
	/**
	 * Tag that stores author of document
	 */
	AUTHOR("[autor]", "setAuthor"),
	/**
	 * Tag that stores cooperated people of document
	 */
	COOPERATED("[spolupracovali]", "setCooperated"),
	/**
	 * Tag that stores created date of document
	 */
	CREATED("[vytvoreno]", "setCreated"),
	/**
	 * Tag that stores name of the document
	 */
	NAME("[nazev]", "setTitle"),
	/**
	 * Tag that stores name of user defined item used in document
	 */
	ROD_USER_1("[rodUser1]", "setRodUser1"),
	/**
	 * Tag that stores name of user defined item used in document
	 */
	ROD_USER_2("[rodUser2]", "setRodUser2"),
	/**
	 * Tag that stores name of user defined item used in document
	 */
	ROD_USER_3("[rodUser3]", "setRodUser3"),
	/**
	 * Tag that stores name of user defined item used in document
	 */
	ROD_USER_4("[rodUser4]", "setRodUser4"),
	/**
	 * Tag that stores name of user defined item used in document
	 */
	ROD_USER_5("[rodUser5]", "setRodUser5"),
	/**
	 * Tag that stores additional info
	 */
	ADDITIONAL_INFO("[dalsiUdaje]", "setAdditionalInfo");

	/**
	 * @param rodTag RodTag name
	 * @return Corresponding item of enumeration
	 */
	public static GlobalTagEnum getForRodTag(String rodTag) {
		for (GlobalTagEnum item : GlobalTagEnum.values()) {
			if (item.getRodTag().equalsIgnoreCase(rodTag)) {
				return item;
			}
		}

		return null;
	}

	private String rodTag;

	private String setterMethod;

	private GlobalTagEnum(String rodTag, String setterMethod) {
		this.rodTag = rodTag;
		this.setterMethod = setterMethod;
	}

	/**
	 * @return the rodTag
	 */
	public String getRodTag() {
		return this.rodTag;
	}

	/**
	 * @return the setterMethod
	 */
	public String getSetterMethod() {
		return this.setterMethod;
	}
}
