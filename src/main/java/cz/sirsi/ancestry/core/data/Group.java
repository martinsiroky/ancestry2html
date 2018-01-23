//Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of people
 * 
 * @author msiroky
 */
public class Group {
	private List<Integer> members = new ArrayList<Integer>();

	private String name;

	/**
	 * Creates new instance of Group
	 * 
	 * @param name Name of group
	 */
	public Group(String name) {
		this.name = name;
	}

	/**
	 * Adds new member to this group
	 * 
	 * @param id Id of person to add
	 */
	public void addMember(Integer id) {
		this.members.add(id);
	}

	/**
	 * @return the members
	 */
	public List<Integer> getMembers() {
		return this.members;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

}
