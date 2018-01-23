//Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.comparator.PersonComparator;
import cz.sirsi.ancestry.core.main.Tree;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object for storing data of partnership
 * 
 * @author msiroky
 */
public class Partnership {
	private boolean divorced;
	private MyCalendar divorceDate;
	private int id;
	private Person man;
	private int manSortOrder;
	private MyCalendar marriageDate;
	private String marriagePlace;
	private boolean married;
	private String priest;
	private String witness;
	private Person woman;
	private int womanSortOrder;

	/**
	 * Constructs new object of Partnership with id of this partnership
	 * 
	 * @param id Id of partnership
	 */
	public Partnership(int id) {
		this.id = id;
	}

	/**
	 * Gets list of all credentials belong to both partners
	 * 
	 * @return Credentials that belong to both partners
	 */
	public List<AdditionalFile> getCredentials() {
		List<AdditionalFile> credentials = new ArrayList<AdditionalFile>();
		List<AdditionalFile> womanCredentials = this.woman.getCredentials();

		for (AdditionalFile credential : this.man.getCredentials()) {
			if (womanCredentials.contains(credential)) {
				credentials.add(credential);
			}
		}

		return credentials;
	}

	/**
	 * @return the divorceDate
	 */
	public String getDivorceDate() {
		return getDivorceDateObj() != null ? getDivorceDateObj().format() : null;
	}

	/**
	 * @return the divorceDate
	 */
	public MyCalendar getDivorceDateObj() {
		if (getMan().isHidden() || getWoman().isHidden() || Config.getInstance().isHideMarriageDate(getMan().isLive())
				|| Config.getInstance().isHideMarriageDate(getWoman().isLive())) {
			return null;
		}

		return this.divorceDate;
	}

	/**
	 * Gets list of all documents belong to both partners
	 * 
	 * @return Documents that belong to both partners
	 */
	public List<AdditionalFile> getDocuments() {
		List<AdditionalFile> documents = new ArrayList<AdditionalFile>();
		List<AdditionalFile> womanDocuments = this.woman.getDocuments();

		for (AdditionalFile document : this.man.getDocuments()) {
			if (womanDocuments.contains(document)) {
				documents.add(document);
			}
		}

		return documents;
	}

	/**
	 * @return All children of this partnership
	 */
	public List<Person> getChildren() {
		List<Person> children = new ArrayList<Person>();
		List<Integer> womanChildrenIds = this.woman.getChildrenIds();

		for (Integer childrenId : this.man.getChildrenIds()) {
			if (womanChildrenIds.contains(childrenId)) {
				children.add(Tree.getInstance().getPeopleMap().get(childrenId));
			}
		}

		Collections.sort(children, new PersonComparator(PersonComparator.BIRTH));

		return children;
	}

	/**
	 * @return Id of partnership
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the man
	 */
	public Person getMan() {
		return this.man;
	}

	/**
	 * @return What sort order has this partnership for man from all his partnerships
	 */
	public int getManSortOrder() {
		return this.manSortOrder;
	}

	/**
	 * @return the marriageDate or null if it is hidden
	 */
	public String getMarriageDate() {
		return getMarriageDateObj() != null ? getMarriageDateObj().format() : null;
	}

	/**
	 * @return the marriageDate or null if it is hidden
	 */
	public MyCalendar getMarriageDateObj() {
		if (getMan().isHidden() || getWoman().isHidden() || Config.getInstance().isHideMarriageDate(getMan().isLive())
				|| Config.getInstance().isHideMarriageDate(getWoman().isLive())) {
			return null;
		}

		return this.marriageDate;
	}

	/**
	 * @return Marriage length
	 */
	public MyDateInterval getMarriageLength() {
		return getMarriageLengthForType(DateUtils.DISTANCE_AVERAGE);
	}

	/**
	 * @param type How to round. Possible values: round up {@link DateUtils#DISTANCE_MAXIMAL}, round down
	 *          {@link DateUtils#DISTANCE_MINIMAL}, round average {@link DateUtils#DISTANCE_AVERAGE}
	 * @return Length of marriage (or null in case this partnership is not marriage or dates are not known)
	 */
	public MyDateInterval getMarriageLengthForType(int type) {
		if (getMarriageDateObj() == null) {
			return null;
		}

		MyCalendar startDate = null;
		MyCalendar endDate = null;

		startDate = getMarriageDateObj();
		if (getDivorceDateObj() != null) {
			endDate = getDivorceDateObj();
		} else {
			MyCalendar manEndDate = null;
			MyCalendar womanEndDate = null;

			if (!DateUtils.isEmpty(getMan().getDeathDateObj())) {
				manEndDate = getMan().getDeathDateObj();
			} else {
				if (getMan().isDead()) {
					return null;
				}
			}

			if (!DateUtils.isEmpty(getWoman().getDeathDateObj())) {
				womanEndDate = getWoman().getDeathDateObj();
			} else {
				if (getWoman().isDead()) {
					return null;
				}
			}

			if (getMan().isLive() && getWoman().isLive()) {
				endDate = new MyCalendar();
			} else if (getMan().isLive() && womanEndDate != null) {
				endDate = womanEndDate;
			} else if (getWoman().isLive() && manEndDate != null) {
				endDate = manEndDate;
			} else if (manEndDate != null && womanEndDate != null) {
				endDate = manEndDate.before(womanEndDate) ? manEndDate : womanEndDate;
			}
		}

		return DateUtils.distance(startDate, endDate, type, this.id);
	}

	/**
	 * @return the marriagePlace or null if it is hidden
	 */
	public String getMarriagePlace() {
		if (getMan().isHidden() || getWoman().isHidden() || Config.getInstance().isHideMarriagePlace(getMan().isLive())
				|| Config.getInstance().isHideMarriagePlace(getWoman().isLive())) {
			return null;
		}

		return this.marriagePlace;
	}

	/**
	 * Gets list of all photos belong to both partners
	 * 
	 * @return Photos that belong to both partners
	 */
	public List<AdditionalFile> getPhotos() {
		List<AdditionalFile> photos = new ArrayList<AdditionalFile>();
		List<AdditionalFile> womanPhotos = this.woman.getPhotos();

		for (AdditionalFile photo : this.man.getPhotos()) {
			if (womanPhotos.contains(photo)) {
				photos.add(photo);
			}
		}

		return photos;
	}

	/**
	 * @return the priest
	 */
	public String getPriest() {
		return this.priest;
	}

	/**
	 * @return the witness
	 */
	public String getWitness() {
		return this.witness;
	}

	/**
	 * @return the woman
	 */
	public Person getWoman() {
		return this.woman;
	}

	/**
	 * @return What sort order has this partnership for woman from all her partnerships
	 */
	public int getWomanSortOrder() {
		return this.womanSortOrder;
	}

	/**
	 * @return True in case this partners were divorced, false otherwise
	 */
	public boolean isDivorced() {
		return this.divorced;
	}

	/**
	 * @return True in case this partnership is married, false otherwise
	 */
	public boolean isMarried() {
		return this.married;
	}

	/**
	 * Sets if this partners were divorced
	 * 
	 * @param divorced
	 */
	public void setDivorced(boolean divorced) {
		this.divorced = divorced;
	}

	/**
	 * @param divorceDate the divorceDate to set
	 */
	public void setDivorceDate(String divorceDate) {
		this.divorceDate = DateUtils.parse(divorceDate);
	}

	/**
	 * @param man the man to set
	 */
	public void setMan(Person man) {
		this.man = man;
	}

	/**
	 * @param manSortOrder Sort order to set @see {@link #getManSortOrder()}
	 */
	public void setManSortOrder(int manSortOrder) {
		this.manSortOrder = manSortOrder;
	}

	/**
	 * @param marriageDate the marriageDate to set
	 */
	public void setMarriageDate(String marriageDate) {
		this.marriageDate = DateUtils.parse(marriageDate);
	}

	/**
	 * @param marriagePlace the marriagePlace to set
	 */
	public void setMarriagePlace(String marriagePlace) {
		this.marriagePlace = marriagePlace;
	}

	/**
	 * Sets if this partnership is marriage
	 * 
	 * @param married
	 */
	public void setMarried(boolean married) {
		this.married = married;
	}

	/**
	 * @param priest the priest to set
	 */
	public void setPriest(String priest) {
		this.priest = priest;
	}

	/**
	 * @param witness the witness to set
	 */
	public void setWitness(String witness) {
		this.witness = witness;
	}

	/**
	 * @param woman the woman to set
	 */
	public void setWoman(Person woman) {
		this.woman = woman;
	}

	/**
	 * @param womanSortOrder Sort order to set @see {@link #getWomanSortOrder()}
	 */
	public void setWomanSortOrder(int womanSortOrder) {
		this.womanSortOrder = womanSortOrder;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return "(" + this.id + ") " + this.getMan() + "-" + this.getWoman();
	}
}
