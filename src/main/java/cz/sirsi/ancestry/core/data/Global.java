// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for storing global data of rod project
 * 
 * @author msiroky
 */
public class Global {
	/**
	 * Singleton instance
	 */
	private static Global globalInstance;

	/**
	 * @return Singleton instance of Global
	 */
	public static Global getInstance() {
		if (globalInstance != null) {
			return globalInstance;
		}

		globalInstance = new Global();

		return globalInstance;
	}

	private String active;
	private String author;
	private String cooperated;
	private String created;
	private List<Group> groups = new ArrayList<Group>();
	private String additionalInfo;
	private String profileName;
	private String rodUser1;
	private String rodUser2;
	private String rodUser3;
	private String rodUser4;
	private String rodUser5;
	private String templateName;
	private String title;

	/**
	 * Creates instance of Global
	 */
	private Global() {
		// nothing
	}

	/**
	 * Adds new group to list of groups
	 * 
	 * @param group Group to add to list of groups
	 */
	public void addGroup(Group group) {
		this.groups.add(group);
	}

	/**
	 * @return Id of active person (last shown before save of rod file)
	 */
	public String getActive() {
		return this.active;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * @return the cooperated
	 */
	public String getCooperated() {
		return this.cooperated;
	}

	/**
	 * @return the created
	 */
	public String getCreated() {
		return this.created;
	}

	/**
	 * @return the groups
	 */
	public List<Group> getGroups() {
		return this.groups;
	}

	/**
	 * @return Additional info of family tre
	 */
	public String getAdditionalInfo() {
		return this.additionalInfo;
	}

	/**
	 * @return Selected profile name
	 */
	public String getProfileName() {
		return this.profileName;
	}

	/**
	 * @return the rodUser1
	 */
	public String getRodUser1() {
		return this.rodUser1;
	}

	/**
	 * @return the rodUser2
	 */
	public String getRodUser2() {
		return this.rodUser2;
	}

	/**
	 * @return the rodUser3
	 */
	public String getRodUser3() {
		return this.rodUser3;
	}

	/**
	 * @return the rodUser4
	 */
	public String getRodUser4() {
		return this.rodUser4;
	}

	/**
	 * @return the rodUser5
	 */
	public String getRodUser5() {
		return this.rodUser5;
	}

	/**
	 * @return Name of selected template
	 */
	public String getTemplateName() {
		return this.templateName;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets active person @see {@link Global#getActive()}
	 * 
	 * @param active Id of active person
	 */
	public void setActive(String active) {
		this.active = active;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @param cooperated the cooperated to set
	 */
	public void setCooperated(String cooperated) {
		this.cooperated = cooperated;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * @param otherInfo Additional info about family tree
	 */
	public void setAdditionalInfo(String otherInfo) {
		this.additionalInfo = otherInfo;
	}

	/**
	 * @param profileName Name of selected profile
	 */
	public void setProfileName(String profileName) {
		this.profileName = profileName;
	}

	/**
	 * @param rodUser1 the rodUser1 to set
	 */
	public void setRodUser1(String rodUser1) {
		this.rodUser1 = rodUser1;
	}

	/**
	 * @param rodUser2 the rodUser2 to set
	 */
	public void setRodUser2(String rodUser2) {
		this.rodUser2 = rodUser2;
	}

	/**
	 * @param rodUser3 the rodUser3 to set
	 */
	public void setRodUser3(String rodUser3) {
		this.rodUser3 = rodUser3;
	}

	/**
	 * @param rodUser4 the rodUser4 to set
	 */
	public void setRodUser4(String rodUser4) {
		this.rodUser4 = rodUser4;
	}

	/**
	 * @param rodUser5 the rodUser5 to set
	 */
	public void setRodUser5(String rodUser5) {
		this.rodUser5 = rodUser5;
	}

	/**
	 * @param templateName Name of selected template
	 */
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Resets list of all groups to empty
	 */
	public void reset() {
		this.groups = new ArrayList<Group>();
	}
}
