//Last revision 19.4.08
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.PersonIntervalTypeEnum;
import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.core.data.comparator.AdditionalFileComparator;
import cz.sirsi.ancestry.core.data.comparator.PersonComparator;
import cz.sirsi.ancestry.core.main.Tree;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object for storing data of person
 * 
 * @author msiroky
 */
public class Person implements Comparable<Person> {

	private ArrayList<String> additionalInfo = new ArrayList<String>();

	private MyCalendar birthDate;
	private String birthPlace;
	private String burialPlace;
	private String citizenship;
	private List<Integer> credentials = new ArrayList<Integer>();
	private String deathCause;
	private MyCalendar deathDate;
	private String deathPlace;
	private List<Integer> documents = new ArrayList<Integer>();
	private List<Education> educations = new ArrayList<Education>();
	private String email1;
	private String email2;
	private Integer fathersId;
	private String firstname;
	private boolean gender;
	private String godparent1;
	private String godparent2;
	private List<String> groups = new ArrayList<String>();
	private List<Integer> halfSiblingsIds = new ArrayList<Integer>();
	private boolean head = false;
	private String headFile;
	private boolean hidden = false;
	private int hierarchy = Integer.MIN_VALUE;
	private MyCalendar changeDate;
	private List<Integer> childrenIds = new ArrayList<Integer>();
	private String christening;
	private Integer id;
	private String ident;
	private MyCalendar insertDate;
	private Map<String, MyDateInterval> intervals = new HashMap<String, MyDateInterval>();
	private List<Job> jobs = new ArrayList<Job>();
	private String lastname;
	private boolean live = false;
	private String maidenname;
	private Integer mothersId;
	private String nationality;
	private List<Integer> partnersIds = new ArrayList<Integer>();
	private String phone1;
	private String phone2;
	private List<Integer> photos = new ArrayList<Integer>();
	private List<LivePlace> places = new ArrayList<LivePlace>();
	private String registryInfo;
	private List<Relation> relations = new ArrayList<Relation>();
	private String religion;
	private List<Integer> rightSiblingsIds = new ArrayList<Integer>();
	private List<Integer> siblingsIds = new ArrayList<Integer>();
	// used for sorting by order in partners list
	private int temp;
	private String titleBack;
	private String titleFront;
	private String userDef1;
	private String userDef2;
	private String userDef3;
	private String userDef4;
	private String userDef5;
	private String web1;
	private String web2;

	/**
	 * Create instance of person
	 */
	public Person() {
		// nothing
	}

	/**
	 * Creates new instance of Person with given gender.
	 * 
	 * @param man True = man, false = woman
	 */
	public Person(boolean man) {
		this.gender = man;
	}

	/**
	 * Creates man or woman person with special id (e.g. -2 for unknown father)
	 * 
	 * @param man True = man, false = woman
	 * @param id Id of person to create
	 */
	public Person(boolean man, int id) {
		this.gender = man;
		this.id = id;
	}

	/**
	 * Adds additional text for person
	 * 
	 * @param text Text to add
	 */
	public void addAdditionalText(String text) {
		this.additionalInfo.add(text);
	}

	/**
	 * Adds credential document to list of credential files
	 * 
	 * @param fileNumber Id of credential to add
	 */
	public void addCredential(Integer fileNumber) {
		this.credentials.add(fileNumber);
		Tree.getInstance().getCredentialsMap().get(fileNumber).addPerson(this);
	}

	/**
	 * Adds document to list of document files
	 * 
	 * @param fileNumber Id of file (document) to add
	 */
	public void addDocument(Integer fileNumber) {
		this.documents.add(fileNumber);
		Tree.getInstance().getDocumentsMap().get(fileNumber).addPerson(this);
	}

	/**
	 * Adds education to list of jobs
	 * 
	 * @param education Education to add
	 */
	public void addEducation(Education education) {
		this.educations.add(education);
	}

	/**
	 * Adds group to the group list
	 * 
	 * @param groupname Name of group to add
	 */
	public void addGroup(String groupname) {
		this.groups.add(groupname);
	}

	/**
	 * Adds half-sibling to the list
	 * 
	 * @param halfSiblingId Id of half sibling to add
	 */
	public void addHalfSibling(Integer halfSiblingId) {
		this.halfSiblingsIds.add(halfSiblingId);
		this.siblingsIds.add(halfSiblingId);
	}

	/**
	 * Adds children to the list
	 * 
	 * @param childrenId Id of children to add
	 */
	public void addChild(Integer childrenId) {
		this.childrenIds.add(childrenId);
	}

	/**
	 * Adds job to list of jobs
	 * 
	 * @param job Job to add
	 */
	public void addJob(Job job) {
		this.jobs.add(job);
	}

	/**
	 * Adds partner to partners list
	 * 
	 * @param partnersId Id of partner
	 */
	public void addPartnersId(Integer partnersId) {
		this.partnersIds.add(partnersId);
	}

	/**
	 * Adds photo to list of photos
	 * 
	 * @param photoNumber Id of photo to add
	 */
	public void addPhoto(Integer photoNumber) {
		this.photos.add(photoNumber);
		Tree.getInstance().getPhotosMap().get(photoNumber).addPerson(this);
	}

	/**
	 * Adds place to list of liveplaces
	 * 
	 * @param livePlace Live place to add
	 */
	public void addPlace(LivePlace livePlace) {
		this.places.add(livePlace);

		if (!Config.getInstance().isHideLivePlace(this.isLive())) {
			addPlaceToList(livePlace.getAddress(), Place.LIVE);
		}
	}

	/**
	 * Adds place to list of places (by name of place and type)
	 * 
	 * @param nameOfPlace Name of place to add
	 * @param typeOfPlace Type of place to add (live, birth, death etc.)
	 */
	private void addPlaceToList(String nameOfPlace, int typeOfPlace) {
		if (nameOfPlace == null) {
			return;
		}
		String[] splitted = nameOfPlace.split(Config.getInstance().getAddressSplitter());

		int position = typeOfPlace == Place.LIVE ? Config.getInstance().getTownPositionInAddress() : Config.getInstance()
				.getTownPositionInBirth();
		if (splitted.length <= position) {
			return;
		}
		String town = splitted[position];

		Place place = new Place(town, this, typeOfPlace);
		List<Place> allTreePlaces = Tree.getInstance().getPlacesList();
		int indexOf = allTreePlaces.indexOf(place);
		if (indexOf > -1) {
			allTreePlaces.get(indexOf).addType(typeOfPlace);
			return;
		}

		Tree.getInstance().getPlacesList().add(place);
	}

	/**
	 * Adds right sibling to the list
	 * 
	 * @param rightSiblingId Id of right sibling to add
	 */
	public void addRightSibling(Integer rightSiblingId) {
		this.rightSiblingsIds.add(rightSiblingId);
		this.siblingsIds.add(rightSiblingId);
	}

	/**
	 * Compares object to other instance of Person by id
	 * 
	 * @param o Other instance of Person
	 * @return positive, negative number or zero
	 */
	public int compareTo(Person o) {
		return this.getId().compareTo(o.getId());
	}

	/**
	 * @return Additional texts
	 */
	public List<String> getAdditionalInfo() {
		return Config.getInstance().isHideAdditionalInfo(isLive()) ? null : this.additionalInfo;
	}

	/**
	 * @return the birth date and place
	 */
	public String getBirth() {
		String date = getBirthDate() != null ? getBirthDate() : "";
		String place = getBirthPlace() != null ? getBirthPlace() : "";

		return date + (date.length() != 0 && place.length() != 0 ? Config.getInstance().getBirthDeathSplitter() : "") + place;
	}

	/**
	 * @return the birthDate formated as string
	 */
	public String getBirthDate() {
		return getBirthDateObj() != null ? getBirthDateObj().format() : null;
	}

	/**
	 * @return Birth date
	 */
	public MyCalendar getBirthDateObj() {
		return Config.getInstance().isHideBirthData(isLive()) ? null : this.birthDate;
	}

	/**
	 * @return the birthPlace
	 */
	public String getBirthPlace() {
		return Config.getInstance().isHideBirthData(isLive()) ? null : this.birthPlace;
	}

	/**
	 * @return the burialPlace
	 */
	public String getBurialPlace() {
		return Config.getInstance().isHideDeathData() ? null : this.burialPlace;
	}

	/**
	 * @return the citizenship
	 */
	public String getCitizenship() {
		return Config.getInstance().isHideNationality(isLive()) ? null : this.citizenship;
	}

	/**
	 * @return the credentials
	 */
	public List<AdditionalFile> getCredentials() {
		List<AdditionalFile> credentialsList = new ArrayList<AdditionalFile>();

		if (!Config.getInstance().isHideCredentials(this.isLive())) {
			for (Integer credentialId : this.credentials) {
				AdditionalFile credential = Tree.getInstance().getCredentialsMap().get(credentialId);
				if (credential.isVisible()) {
					credentialsList.add(credential);
				}
			}
		}

		Collections.sort(credentialsList, new AdditionalFileComparator());

		return credentialsList;
	}

	/**
	 * @return the death date and place
	 */
	public String getDeath() {
		String date = getDeathDate() != null ? getDeathDate() : "";
		String place = getDeathPlace() != null ? getDeathPlace() : "";

		return date + (date.length() != 0 && place.length() != 0 ? Config.getInstance().getBirthDeathSplitter() : "") + place;
	}

	/**
	 * @return the deathCause
	 */
	public String getDeathCause() {
		return Config.getInstance().isHideDeathData() ? null : this.deathCause;
	}

	/**
	 * @return the deathDate
	 */
	public String getDeathDate() {
		return getDeathDateObj() != null ? this.deathDate.format() : null;
	}

	/**
	 * @return the deathDate
	 */
	public MyCalendar getDeathDateObj() {
		return Config.getInstance().isHideDeathData() ? null : this.deathDate;
	}

	/**
	 * @return the deathPlace
	 */
	public String getDeathPlace() {
		return Config.getInstance().isHideDeathData() ? null : this.deathPlace;
	}

	/**
	 * @return the documents
	 */
	public List<AdditionalFile> getDocuments() {
		List<AdditionalFile> documentsList = new ArrayList<AdditionalFile>();

		if (!Config.getInstance().isHideDocuments(this.isLive())) {
			for (Integer documentId : this.documents) {
				AdditionalFile document = Tree.getInstance().getDocumentsMap().get(documentId);
				if (document.isVisible()) {
					documentsList.add(document);
				}
			}
		}

		Collections.sort(documentsList, new AdditionalFileComparator());

		return documentsList;
	}

	/**
	 * @return the education list
	 */
	public List<Education> getEducation() {
		return Config.getInstance().isHideEducation(isLive()) ? new ArrayList<Education>() : this.educations;
	}

	/**
	 * @return the email1
	 */
	public String getEmail1() {
		return Config.getInstance().isHideContact() ? null : this.email1;
	}

	/**
	 * @return the email2
	 */
	public String getEmail2() {
		return Config.getInstance().isHideContact() ? null : this.email2;
	}

	/**
	 * @return father Person
	 */
	public Person getFather() {
		return getFathersId() != null ? getFathersId().intValue() == -2 ? Constants.UNKNOWN_MAN_PERSON : Tree.getInstance().getPeopleMap().get(
				getFathersId()) : null;
	}

	/**
	 * @return the fathersId
	 */
	public Integer getFathersId() {
		return this.fathersId;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * @return Full name with titles and last name
	 */
	public String getFullName() {
		String first = getFirstname();
		if (first == null) {
			first = "?";
		}
		String maiden = getMaidenname();
		if (maiden == null) {
			maiden = "?";
		}
		String second = getSecondname();
		if (second == null) {
			second = "?";
		}
		String last = getLastname();
		if (last == null) {
			last = "?";
		}

		return (getTitleFront() != null ? getTitleFront() : "")
				+ " "
				+ last
				+ " "
				+ first
				+ " "
				+ (getTitleBack() != null ? getTitleBack() : "")
				+ (!maiden.equals(last) ? "("
						+ MessagesTools.getWebMessages().getMessage("person." + (isMale() ? "man" : "woman") + ".nee", null, null, false) + " "
						+ maiden + ")" : "");
	}

	/**
	 * @return the godparent1
	 */
	public String getGodparent1() {
		return Config.getInstance().isHideReligion(isLive()) ? null : this.godparent1;
	}

	/**
	 * @return the godparent2
	 */
	public String getGodparent2() {
		return Config.getInstance().isHideReligion(isLive()) ? null : this.godparent2;
	}

	/**
	 * @return List of all grandchild sorted by birth date
	 */
	private List<Person> getGrandChildsSorted() {
		List<Person> childList;
		List<Person> grandChildList = new ArrayList<Person>();

		childList = this.getChildren();

		if (childList.size() == 0) {
			return null;
		}

		for (Person child : childList) {
			grandChildList.addAll(child.getChildren());
		}

		Collections.sort(grandChildList, new PersonComparator(PersonComparator.BIRTH, PersonComparator.ID));
		return grandChildList;
	}

	/**
	 * @return List of all greatgrandchilds sorted by birth date
	 */
	private List<Person> getGreatGrandChildsSorted() {
		List<Person> childList;
		List<Person> grandChildList = new ArrayList<Person>();
		List<Person> grandGrandChildList = new ArrayList<Person>();
		childList = this.getChildren();

		if (childList.size() == 0) {
			return null;
		}

		for (Person child : childList) {
			grandChildList.addAll(child.getChildren());
		}

		for (Person grandChild : grandChildList) {
			grandGrandChildList.addAll(grandChild.getChildren());
		}

		Collections.sort(grandGrandChildList, new PersonComparator(PersonComparator.BIRTH, PersonComparator.ID));
		return grandGrandChildList;
	}

	/**
	 * @return the groups
	 */
	public List<String> getGroups() {
		return this.groups;
	}

	/**
	 * @return halfSiblings Person list
	 */
	public List<Person> getHalfSiblings() {
		List<Person> halfSiblings = new ArrayList<Person>();
		for (int halfSiblingId : getHalfSiblingsIds()) {
			halfSiblings.add(Tree.getInstance().getPeopleMap().get(halfSiblingId));
		}
		return halfSiblings;
	}

	/**
	 * @return the halfSiblingsIds
	 */
	public List<Integer> getHalfSiblingsIds() {
		return this.halfSiblingsIds;
	}

	/**
	 * @return the headFile
	 */
	public String getHeadFile() {
		return this.headFile;
	}

	/**
	 * @return Hierarchy of person
	 */
	public int getHierarchy() {
		return this.hierarchy;
	}

	/**
	 * @return the changeDate
	 */
	public String getChangeDate() {
		return this.changeDate != null ? this.changeDate.format() : null;
	}

	/**
	 * @return children Person list (sorted by birth date)
	 */
	public List<Person> getChildren() {
		return getChildren(true);
	}

	/**
	 * @param sort If sort list of all children or not
	 * @return children Person list
	 */
	public List<Person> getChildren(boolean sort) {
		List<Person> children = new ArrayList<Person>();
		for (int childrenId : getChildrenIds()) {
			children.add(Tree.getInstance().getPeopleMap().get(childrenId));
		}

		if (sort) {
			Collections.sort(children, new PersonComparator(PersonComparator.BIRTH, PersonComparator.ID));
		}
		return children;
	}

	/**
	 * @return the childrenIds
	 */
	public List<Integer> getChildrenIds() {
		return this.childrenIds;
	}

	/**
	 * @return the christening
	 */
	public String getChristening() {
		return Config.getInstance().isHideReligion(isLive()) ? null : this.christening;
	}

	/**
	 * @return the id
	 */
	public Integer getId() {
		return this.id;
	}

	/**
	 * @return the ident
	 */
	public String getIdent() {
		return this.ident;
	}

	/**
	 * @return the insertDate
	 */
	public String getInsertDate() {
		return this.insertDate != null ? this.insertDate.format() : null;
	}

	/**
	 * @return the jobs
	 */
	public List<Job> getJobs() {
		return Config.getInstance().isHideJobs(isLive()) ? new ArrayList<Job>() : this.jobs;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return this.lastname;
	}

	/**
	 * Computes interval between birth date and some other date that is specified by type parameter.
	 * 
	 * @param intervalName Unique name of interval for caching in map
	 * @param roundingType How to round not exact intervals
	 * @param type What second date of interval to use
	 * @return interval between two dates
	 */
	public MyDateInterval getLiveInterval(String intervalName, int roundingType, PersonIntervalTypeEnum type) {
		if (this.intervals.containsKey(intervalName)) {
			return this.intervals.get(intervalName);
		}

		MyCalendar first = this.getBirthDateObj();
		MyCalendar second = null;

		switch (type) {
		case LIVE_LENGTH:
			second = this.isLive() ? new MyCalendar() : this.getDeathDateObj();
			break;
		case FIRST_WEDDING_DAY:
			List<Person> partners = this.getPartners(true);
			if (partners.size() == 0) {
				return null;
			}
			Person firstPartner = partners.get(0);
			Partnership partnership = this.getPartnership(firstPartner.getId());
			if (partnership == null || partnership.getMarriageDateObj() == null) {
				return null;
			}

			second = partnership.getMarriageDateObj();
			break;
		case FIRST_CHILD_DAY:
			if (this.getDeathDateObj() == null && this.isDead() && this.isMale()) {
				return null;
			}
			List<Person> childList = this.getChildren(true);
			if (childList.size() == 0) {
				return null;
			}
			Person firstChild = childList.get(0);
			if (firstChild == null
					|| firstChild.getBirthDateObj() == null
					|| (this.isDead() && this.isMale() && firstChild.getBirthDateObj() != null && this.getDeathDateObj().before(
							firstChild.getBirthDateObj()))) {
				return null;
			}

			second = firstChild.getBirthDateObj();
			break;
		case FIRST_GRANDCHILD_DAY:
			if (this.getDeathDateObj() == null && this.isDead()) {
				return null;
			}
			List<Person> grandChildList = getGrandChildsSorted();
			if (grandChildList == null || grandChildList.size() == 0) {
				return null;
			}

			Person firstGrandChild = grandChildList.get(0);
			if (firstGrandChild.getBirthDateObj() == null
					|| (this.isDead() && firstGrandChild.getBirthDateObj() != null && this.getDeathDateObj()
							.before(firstGrandChild.getBirthDateObj()))) {
				return null;
			}

			second = firstGrandChild.getBirthDateObj();
			break;
		case ALL_GRANDCHILD_DAYS:
			if (this.getDeathDateObj() == null && this.isDead()) {
				return null;
			}
			grandChildList = getGrandChildsSorted();
			if (grandChildList == null || grandChildList.size() == 0) {
				return null;
			}

			if (roundingType == DateUtils.DISTANCE_MINIMAL) {
				Collections.reverse(grandChildList);
			}

			for (Person grandChild : grandChildList) {
				if (grandChild.getBirthDateObj() == null
						|| (this.isDead() && grandChild.getBirthDateObj() != null && this.getDeathDateObj().before(grandChild.getBirthDateObj()))) {
					continue;
				}

				second = grandChild.getBirthDateObj();
				break;
			}
			break;
		case ALL_WEDDING_DAYS:
			partners = this.getPartners(true);
			if (roundingType == DateUtils.DISTANCE_MINIMAL) {
				Collections.reverse(partners);
			}

			for (Person partner : partners) {
				partnership = this.getPartnership(partner.getId());
				if (partnership == null || partnership.getMarriageDateObj() == null) {
					continue;
				}

				second = partnership.getMarriageDateObj();
				break;
			}
			break;
		case ALL_CHILD_DAYS:
			if (this.getDeathDateObj() == null && this.isDead() && this.isMale()) {
				return null;
			}
			childList = this.getChildren(true);
			if (roundingType == DateUtils.DISTANCE_MINIMAL) {
				Collections.reverse(childList);
			}

			for (Person grandChild : childList) {
				if (grandChild.getBirthDateObj() == null
						|| (this.isDead() && this.isMale() && grandChild.getBirthDateObj() != null && this.getDeathDateObj().before(
								grandChild.getBirthDateObj()))) {
					continue;
				}

				second = grandChild.getBirthDateObj();
				break;
			}
			break;
		case FIRST_GREATGRANDCHILD_DAY:
			if (this.getDeathDateObj() == null && this.isDead()) {
				return null;
			}
			List<Person> grandGrandChildList = getGreatGrandChildsSorted();
			if (grandGrandChildList == null || grandGrandChildList.size() == 0) {
				return null;
			}

			Person firstGrandGrandChild = grandGrandChildList.get(0);
			if (firstGrandGrandChild.getBirthDateObj() == null
					|| (this.isDead() && firstGrandGrandChild.getBirthDateObj() != null && this.getDeathDateObj().before(
							firstGrandGrandChild.getBirthDateObj()))) {
				return null;
			}

			second = firstGrandGrandChild.getBirthDateObj();
			break;
		case ALL_GREATGRANDCHILD_DAYS:
			if (this.getDeathDateObj() == null && this.isDead()) {
				return null;
			}
			grandGrandChildList = getGreatGrandChildsSorted();
			if (grandGrandChildList == null || grandGrandChildList.size() == 0) {
				return null;
			}

			if (roundingType == DateUtils.DISTANCE_MINIMAL) {
				Collections.reverse(grandGrandChildList);
			}

			for (Person grandGrandChild : grandGrandChildList) {
				if (grandGrandChild.getBirthDateObj() == null
						|| (this.isDead() && grandGrandChild.getBirthDateObj() != null && this.getDeathDateObj().before(
								grandGrandChild.getBirthDateObj()))) {
					continue;
				}

				second = grandGrandChild.getBirthDateObj();
				break;
			}
			break;
		}

		MyDateInterval interval = DateUtils.distance(first, second, roundingType, this.id);
		this.intervals.put(intervalName, interval);
		return interval;
	}

	/**
	 * @return Length of live (in the highest possible precision)
	 */
	public MyDateInterval getLiveLength() {
		return getLiveInterval("getLiveLength", DateUtils.DISTANCE_AVERAGE, PersonIntervalTypeEnum.LIVE_LENGTH);
	}

	/**
	 * @return the maidenname
	 */
	public String getMaidenname() {
		// TODO Rework working with names - use last name in case maiden is null?
		return this.maidenname != null && this.maidenname.length() != 0 ? this.maidenname
				: (this.isMale() ? (this.lastname != null ? this.lastname : "?") : "?");
	}

	/**
	 * @return mother Mother of person
	 */
	public Person getMother() {
		return getMothersId() != null ? getMothersId().intValue() == -2 ? Constants.UNKNOWN_WOMAN_PERSON : Tree.getInstance().getPeopleMap()
				.get(getMothersId()) : null;
	}

	/**
	 * @return the mothersId
	 */
	public Integer getMothersId() {
		return this.mothersId;
	}

	/**
	 * @return Second name + first name
	 */
	// TODO Rework working with names
	public String getName() {
		String first = getFirstname();
		if (first == null) {
			first = "?";
		}
		String second = getSecondname();
		if (second == null) {
			second = "?";
		}
		return second + " " + first;
	}

	/**
	 * @return the nationality
	 */
	public String getNationality() {
		return Config.getInstance().isHideNationality(isLive()) ? null : this.nationality;
	}

	/**
	 * @return partner Person list
	 */
	public List<Person> getPartners() {
		return getPartners(true);
	}

	/**
	 * @param sort Sorted or not
	 * @return partner Person list
	 */
	public List<Person> getPartners(boolean sort) {
		List<Person> partners = new ArrayList<Person>();
		for (int partnerId : getPartnersIds()) {
			Person partner = Tree.getInstance().getPeopleMap().get(partnerId);

			partners.add(partner);

			if (sort) {
				if (partner.isMale()) {
					partner.setTemp(this.getPartnership(partnerId).getManSortOrder());
				} else {
					partner.setTemp(this.getPartnership(partnerId).getWomanSortOrder());
				}
			}
		}

		if (sort && partners.size() > 1) {
			Collections.sort(partners, new PersonComparator(PersonComparator.TEMP_ORDER));
		}

		return partners;
	}

	/**
	 * @param partnerId Partner id
	 * @return Partnership object for current person and person given as parameter
	 */
	public Partnership getPartnership(Integer partnerId) {
		String key = this.getId().compareTo(partnerId) > 0 ? this.getId() + "-" + partnerId : partnerId + "-" + this.getId();
		return Tree.getInstance().getPartnershipMap().get(key);
	}

	/**
	 * @return the partnersId
	 */
	public List<Integer> getPartnersIds() {
		return this.partnersIds;
	}

	/**
	 * @return the phone1
	 */
	public String getPhone1() {
		return Config.getInstance().isHideContact() ? null : this.phone1;
	}

	/**
	 * @return the phone2
	 */
	public String getPhone2() {
		return Config.getInstance().isHideContact() ? null : this.phone2;
	}

	/**
	 * @return the photos
	 */
	public List<AdditionalFile> getPhotos() {
		List<AdditionalFile> photoList = new ArrayList<AdditionalFile>();

		if (!Config.getInstance().isHidePhotos(this.isLive())) {
			for (Integer photoName : this.photos) {
				AdditionalFile photo = Tree.getInstance().getPhotosMap().get(photoName);
				if (photo.isVisible()) {
					photoList.add(photo);
				}
			}
		}

		Collections.sort(photoList, new AdditionalFileComparator());

		return photoList;
	}

	/**
	 * @return the places
	 */
	public List<LivePlace> getPlaces() {
		return Config.getInstance().isHideLivePlace(isLive()) ? new ArrayList<LivePlace>() : this.places;
	}

	/**
	 * @return the registryInfo
	 */
	public String getRegistryInfo() {
		return Config.getInstance().isHideRegistryInfo(isLive()) ? null : (this.registryInfo != null ? this.registryInfo.replaceAll("\n",
				"<br>") : null);
	}

	/**
	 * @return List of realtions (path to main person)
	 */
	public List<Relation> getRelations() {
		return this.relations;
	}

	/**
	 * @return the religion
	 */
	public String getReligion() {
		return Config.getInstance().isHideReligion(isLive()) ? null : this.religion;
	}

	/**
	 * @return right siblings Person list
	 */
	public List<Person> getRightSiblings() {
		List<Person> rightSiblings = new ArrayList<Person>();
		for (int rightSiblingId : getRightSiblingsIds()) {
			rightSiblings.add(Tree.getInstance().getPeopleMap().get(rightSiblingId));
		}
		return rightSiblings;
	}

	/**
	 * @return the right siblingsIds
	 */
	public List<Integer> getRightSiblingsIds() {
		return this.rightSiblingsIds;
	}

	/**
	 * @return Last name or maiden name (due to Config.getInstance()uration)
	 */
	// TODO
	public String getSecondname() {
		return Config.getInstance().isUseMaidenName() ? getMaidenname() : (getLastname() != null ? getLastname() : "?")
				+ (getMaidenname().equals(getLastname()) ? "" : " (" + getMaidenname() + ")");
	}

	/**
	 * @return right siblings Person list
	 */
	public List<Person> getSiblings() {
		return getSiblings(true);
	}

	/**
	 * @param sort Sorted or not
	 * @return right siblings Person list
	 */
	public List<Person> getSiblings(boolean sort) {
		List<Person> siblings = new ArrayList<Person>();
		for (int siblingId : getSiblingsIds()) {
			siblings.add(Tree.getInstance().getPeopleMap().get(siblingId));
		}

		if (sort) {
			Collections.sort(siblings, new PersonComparator(PersonComparator.BIRTH, PersonComparator.ID));
		}

		return siblings;
	}

	/**
	 * @return the siblingsIds
	 */
	public List<Integer> getSiblingsIds() {
		return this.siblingsIds;
	}

	/**
	 * @return Gets temporary set value (used for sorting by special attributes)
	 */
	public int getTemp() {
		return this.temp;
	}

	/**
	 * @return the titleBack
	 */
	public String getTitleBack() {
		return this.titleBack;
	}

	/**
	 * @return the titleFront
	 */
	public String getTitleFront() {
		return this.titleFront;
	}

	/**
	 * @return the userDef1
	 */
	public String getUserDef1() {
		return Config.getInstance().isHideUserDefined(isLive()) ? null : this.userDef1;
	}

	/**
	 * @return the userDef2
	 */
	public String getUserDef2() {
		return Config.getInstance().isHideUserDefined(isLive()) ? null : this.userDef2;
	}

	/**
	 * @return the userDef3
	 */
	public String getUserDef3() {
		return Config.getInstance().isHideUserDefined(isLive()) ? null : this.userDef3;
	}

	/**
	 * @return the userDef4
	 */
	public String getUserDef4() {
		return Config.getInstance().isHideUserDefined(isLive()) ? null : this.userDef4;
	}

	/**
	 * @return the userDef5
	 */
	public String getUserDef5() {
		return Config.getInstance().isHideUserDefined(isLive()) ? null : this.userDef5;
	}

	/**
	 * @return the web1
	 */
	public String getWeb1() {
		return Config.getInstance().isHideWeb() ? null : this.web1;
	}

	/**
	 * @return the web1 with http://
	 */
	public String getWeb1http() {
		return Config.getInstance().isHideWeb() ? null : (this.web1.startsWith("http") ? this.web1 : "http://" + this.web1);
	}

	/**
	 * @return the web2
	 */
	public String getWeb2() {
		return Config.getInstance().isHideWeb() ? null : this.web2;
	}

	/**
	 * @return the web2 with http://
	 */
	public String getWeb2http() {
		return Config.getInstance().isHideWeb() ? null : (this.web2.startsWith("http") ? this.web2 : "http://" + this.web2);
	}

	/**
	 * @return the Not live
	 */
	public boolean isDead() {
		return !this.live;
	}

	/**
	 * @return if the person is female
	 */
	public boolean isFemale() {
		return !this.gender;
	}

	/**
	 * @return the head
	 */
	public boolean isHead() {
		return Config.getInstance().isHideHead(isLive()) ? false : this.head;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return this.hidden;
	}

	/**
	 * @return the live
	 */
	public boolean isLive() {
		return this.live;
	}

	/**
	 * @return if the person is male
	 */
	public boolean isMale() {
		return this.gender;
	}

	/**
	 * Reset stored data before regenerating. e.g. list of relations
	 */
	public void reset() {
		this.relations = new ArrayList<Relation>();
	}

	/**
	 * @param birthDate the birthDate to set
	 */
	public void setBirthDate(String birthDate) {
		this.birthDate = DateUtils.parse(birthDate);
	}

	/**
	 * @param birthPlace the birthPlace to set
	 */
	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;

		if (!Config.getInstance().isHideBirthData(this.isLive())) {
			addPlaceToList(birthPlace, Place.BIRTH);
		}
	}

	/**
	 * @param burialPlace the burialPlace to set
	 */
	public void setBurialPlace(String burialPlace) {
		this.burialPlace = burialPlace;

		if (Config.getInstance().isUseBurialPlace() && !Config.getInstance().isHideDeathData()) {
			addPlaceToList(burialPlace, Place.BURIAL);
		}
	}

	/**
	 * @param citizenship the citizenship to set
	 */
	public void setCitizenship(String citizenship) {
		this.citizenship = citizenship;
	}

	/**
	 * @param deathCause the deathCause to set
	 */
	public void setDeathCause(String deathCause) {
		this.deathCause = deathCause;
	}

	/**
	 * @param deathDate the deathDate to set
	 */
	public void setDeathDate(String deathDate) {
		this.deathDate = DateUtils.parse(deathDate);
	}

	/**
	 * @param deathPlace the deathPlace to set
	 */
	public void setDeathPlace(String deathPlace) {
		this.deathPlace = deathPlace;

		if (!Config.getInstance().isHideDeathData()) {
			addPlaceToList(deathPlace, Place.DEATH);
		}
	}

	/**
	 * @param email1 the email1 to set
	 */
	public void setEmail1(String email1) {
		this.email1 = email1;
	}

	/**
	 * @param email2 the email2 to set
	 */
	public void setEmail2(String email2) {
		this.email2 = email2;
	}

	/**
	 * @param fathersId the fathersId to set
	 */
	public void setFathersId(String fathersId) {
		this.fathersId = Integer.parseInt(fathersId);
	}

	/**
	 * Sets gender to female
	 */
	public void setFemale() {
		this.gender = false;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname != null ? firstname.trim() : null;
	}

	/**
	 * Set gender read from rod file
	 * 
	 * @param gender "1" if male, "0" if female
	 */
	public void setGender(String gender) {
		if ("1".equals(gender)) {
			setMale();
		} else {
			setFemale();
		}
	}

	/**
	 * @param godparent1 the godparent1 to set
	 */
	public void setGodparent1(String godparent1) {
		this.godparent1 = godparent1;
	}

	/**
	 * @param godparent2 the godparent2 to set
	 */
	public void setGodparent2(String godparent2) {
		this.godparent2 = godparent2;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(boolean head) {
		this.head = head;
	}

	/**
	 * @param head the head to set
	 */
	public void setHead(String head) {
		this.head = "1".equals(head);
	}

	/**
	 * @param headFile the headFile to set
	 */
	public void setHeadFile(String headFile) {
		this.headFile = headFile;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Sets hierarchy of person
	 * 
	 * @param hierarchy Hierarchy of person to set
	 */
	public void setHierarchy(int hierarchy) {
		this.hierarchy = hierarchy;
	}

	/**
	 * @param changeDate the changeDate to set
	 */
	public void setChangeDate(String changeDate) {
		this.changeDate = DateUtils.parse(changeDate);
	}

	/**
	 * @param christening the christening to set
	 */
	public void setChristening(String christening) {
		this.christening = christening;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = Integer.parseInt(id);
	}

	/**
	 * @param ident the ident to set
	 */
	public void setIdent(String ident) {
		this.ident = ident;
	}

	/**
	 * @param insertDate the insertDate to set
	 */
	public void setInsertDate(String insertDate) {
		this.insertDate = DateUtils.parse(insertDate);
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname != null ? lastname.trim() : null;
	}

	/**
	 * @param live the live to set
	 */
	public void setLive(boolean live) {
		this.live = live;
	}

	/**
	 * @param live the live to set
	 */
	public void setLive(String live) {
		this.live = "1".equals(live);
	}

	/**
	 * @param maidenname the maidenname to set
	 */
	public void setMaidenname(String maidenname) {
		this.maidenname = maidenname != null ? maidenname.trim() : null;
	}

	/**
	 * Sets gender to male
	 */
	public void setMale() {
		this.gender = true;
	}

	/**
	 * @param mothersId the mothersId to set
	 */
	public void setMothersId(String mothersId) {
		this.mothersId = Integer.parseInt(mothersId);
	}

	/**
	 * @param nationality the nationality to set
	 */
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	/**
	 * @param partnersIds the partnersIds to set
	 */
	public void setPartnersIds(List<Integer> partnersIds) {
		this.partnersIds = partnersIds;
	}

	/**
	 * @param phone1 the phone1 to set
	 */
	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	/**
	 * @param phone2 the phone2 to set
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * @param registryInfo the registryInfo to set
	 */
	public void setRegistryInfo(String registryInfo) {
		this.registryInfo = registryInfo;
	}

	/**
	 * Sets list of relations for this person
	 * 
	 * @param relations List of relations (path from this person to main person)
	 */
	public void setRelations(List<Relation> relations) {
		this.relations = relations;
	}

	/**
	 * @param religion the religion to set
	 */
	public void setReligion(String religion) {
		this.religion = religion;
	}

	/**
	 * Sets temp value of this person
	 * 
	 * @param temp Temp value used for sorting person by some complex criteria etc.
	 */
	public void setTemp(int temp) {
		this.temp = temp;
	}

	/**
	 * @param titleBack the titleBack to set
	 */
	public void setTitleBack(String titleBack) {
		this.titleBack = titleBack;
	}

	/**
	 * @param titleFront the titleFront to set
	 */
	public void setTitleFront(String titleFront) {
		this.titleFront = titleFront;
	}

	/**
	 * @param userDef1 the userDef1 to set
	 */
	public void setUserDef1(String userDef1) {
		this.userDef1 = userDef1;
	}

	/**
	 * @param userDef2 the userDef2 to set
	 */
	public void setUserDef2(String userDef2) {
		this.userDef2 = userDef2;
	}

	/**
	 * @param userDef3 the userDef3 to set
	 */
	public void setUserDef3(String userDef3) {
		this.userDef3 = userDef3;
	}

	/**
	 * @param userDef4 the userDef4 to set
	 */
	public void setUserDef4(String userDef4) {
		this.userDef4 = userDef4;
	}

	/**
	 * @param userDef5 the userDef5 to set
	 */
	public void setUserDef5(String userDef5) {
		this.userDef5 = userDef5;
	}

	/**
	 * @param web1 the web1 to set
	 */
	public void setWeb1(String web1) {
		this.web1 = web1;
	}

	/**
	 * @param web2 the web2 to set
	 */
	public void setWeb2(String web2) {
		this.web2 = web2;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append("(" + this.id + ") ");
		sb.append(this.firstname);
		sb.append(" ");
		sb.append(this.lastname);
		sb.append(" (");
		sb.append(this.maidenname);
		sb.append(")");

		return sb.toString();
	}
}
