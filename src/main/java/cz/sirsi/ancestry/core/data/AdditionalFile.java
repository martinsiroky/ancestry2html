// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import cz.sirsi.ancestry.core.configuration.Config;

/**
 * Data of additional file (photo, credential or document) with list of people associated with it.
 * 
 * @author msiroky
 */
public class AdditionalFile implements Comparable<AdditionalFile> {
	private String description;
	private String extension; 
	private String externalFileName;
	private int id;
	private String name;
	private List<Person> people = new ArrayList<Person>();
	private boolean visible;
	private String year; 
 
	/**
	 * Create instance of AdditionalDocument object
	 *  
	 * @param id Id of file (from rod file)
	 * @param name Short name
	 * @param year Year of file
	 * @param description Description of file
	 * @param externalFileName Path to external file or null in case file is internal
	 * @param extension Extension of file (jpg, doc ...)
	 */
	public AdditionalFile(int id, String name, String year, String description, String externalFileName, String extension) {
		super();
		this.id = id;
		this.externalFileName = externalFileName!=null && externalFileName.length() > 0 ? externalFileName : null;
		this.name = name != null && name.length() > 0 ? name : null;
		this.year = year != null && year.length() > 0 ? year : null;
		this.extension = extension;
		this.description = description != null && description.length() > 0 ? description : null;
	}

	/**
	 * Adds person to this photo
	 * 
	 * @param person Person to add to photo
	 */
	public void addPerson(Person person) {
		this.people.add(person);
	}

	/**
	 * @param other AdditionalFile to compare with this object
	 * @return Positive or negative number or 0
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(AdditionalFile other) {
		Config.getInstance().getCollator().setStrength(Collator.TERTIARY);
		Collator col = Config.getInstance().getCollator();

		int result = col.compare(this.getName(), other.getName());

		return result;
	}

	/**
	 * @return the people
	 */
	public List<Person> getAllPeople() {
		return this.people;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return this.extension;
	}

	/**
	 * @return the externalFileName
	 */
	public String getExternalFileName() {
		return this.externalFileName;
	}

	/**
	 * @return the number
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name != null && this.name.trim().length() > 0 ? this.name : this.id + "";
	}

	/**
	 * @return List of visible (non hidden) people
	 */
	public List<Person> getPeople() {
		List<Person> visiblePeople = new ArrayList<Person>();
		for (Person person : this.people) {
			if (!person.isHidden()) {
				visiblePeople.add(person);
			}
		}
		return visiblePeople;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return this.year;
	}

	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return this.visible;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/**
	 * @param year the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}
}
