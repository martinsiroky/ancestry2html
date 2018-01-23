/**
 * 
 */
package cz.sirsi.ancestry.core.load.rod;

/**
 * @author msiroky
 */
public enum PersonTagEnum {
	/**
	 * Tag that stores identifier of person
	 */
	IDENT("[identifikator]", "setIdent"),
	/**
	 * Tag that stores front title of person
	 */
	TITLE_FRONT("[titulPred]", "setTitleFront"),
	/**
	 * Tag that stores back title of person
	 */
	TITLE_BACK("[titulZa]", "setTitleBack"),
	/**
	 * Tag that stores first name of person
	 */
	FIRSTNAME("[jmeno]", "setFirstname"),
	/**
	 * Tag that stores last name of person
	 */
	LASTNAME("[prijmeni]", "setLastname"),
	/**
	 * Tag that stores maiden name of person
	 */
	MAIDENNAME("[rodnePrijmeni]", "setMaidenname"),
	/**
	 * Tag that stores birth date of person
	 */
	BIRTH_DATE("[narozText]", "setBirthDate"),
	/**
	 * Tag that stores birth place of person
	 */
	BIRTH_PLACE("[narozMisto]", "setBirthPlace"),
	/**
	 * Tag that stores death date of person
	 */
	DEATH_DATE("[smrtText]", "setDeathDate"),
	/**
	 * Tag that stores death place of person
	 */
	DEATH_PLACE("[smrtMisto]", "setDeathPlace"),
	/**
	 * Tag that stores id of father of person
	 */
	FATHERS_ID("[otec]", "setFathersId"),
	/**
	 * Tag that stores id of mother of person
	 */
	MOTHERS_ID("[matka]", "setMothersId"),
	/**
	 * Tag that stores insert date of person
	 */
	INSERT_DATE("[datumVlozeni]", "setInsertDate"),
	/**
	 * Tag that stores change date of person
	 */
	CHANGE_DATE("[posledniZmena]", "setChangeDate"),
	/**
	 * Tag that stores gender of person
	 */
	GENDER("[pohlavi]", "setGender"),
	/**
	 * Tag that stores if person is live
	 */
	LIVE("[zije]", "setLive"),
	/**
	 * Tag that stores head (true/false) of person
	 */
	HEAD("[hlava]", "setHead"),
	/**
	 * Tag that stores head file (in case of external head)
	 */
	HEAD_FILE("[hlavaOdkaz]", "setHeadFile"),
	/**
	 * Tag that stores burial place of person
	 */
	BURIAL_PLACE("[smrtPohreb]", "setBurialPlace"),
	/**
	 * Tag that stores cause of death of person
	 */
	DEATH_CAUSE("[smrtPricina]", "setDeathCause"),
	/**
	 * Tag that stores religion of person
	 */
	RELIGION("[vyznani]", "setReligion"),
	/**
	 * Tag that stores christening of person
	 */
	CHRISTENING("[krest]", "setChristening"),
	/**
	 * Tag that stores first godparent of person
	 */
	GOD_PARENT1("[kmotr1]", "setGodparent1"),
	/**
	 * Tag that stores second godparent of person
	 */
	GOD_PARENT2("[kmotr2]", "setGodparent2"),
	/**
	 * Tag that stores nationality of person
	 */
	NATIONALITY("[narodnost]", "setNationality"),

	/**
	 * Tag that stores citizenship of person
	 */
	CITIZENSHIP("[statniPrislusnost]", "setCitizenship"),
	/**
	 * Tag that stores first email of person
	 */
	EMAIL1("[email1]", "setEmail1"),
	/**
	 * Tag that stores second email of person
	 */
	EMAIL2("[email2]", "setEmail2"),
	/**
	 * Tag that stores first phone of person
	 */
	PHONE1("[telefon1]", "setPhone1"),
	/**
	 * Tag that stores second phone of person
	 */
	PHONE2("[telefon2]", "setPhone2"),
	/**
	 * Tag that stores first web page of person
	 */
	WEB1("[www1]", "setWeb1"),
	/**
	 * Tag that stores second web page of person
	 */
	WEB2("[www2]", "setWeb2"),
	/**
	 * Tag that stores first user defined item of person
	 */
	USER1("[user1]", "setUserDef1"),
	/**
	 * Tag that stores second user defined item of person
	 */
	USER2("[user2]", "setUserDef2"),
	/**
	 * Tag that stores third user defined item of person
	 */
	USER3("[user3]", "setUserDef3"),
	/**
	 * Tag that stores fourth user defined item of person
	 */
	USER4("[user4]", "setUserDef4"),
	/**
	 * Tag that stores fifth user defined item of person
	 */
	USER5("[user5]", "setUserDef5");

	private String rodTag;

	private String setterMethod;

	private PersonTagEnum(String rodTag, String setterMethod) {
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

	/**
	 * @param rodTag RodTag name
	 * @return Corresponding item of enumeration
	 */
	public static PersonTagEnum getForRodTag(String rodTag) {
		for (PersonTagEnum item : PersonTagEnum.values()) {
			if (item.getRodTag().equalsIgnoreCase(rodTag)) {
				return item;
			}
		}

		return null;
	}
}
