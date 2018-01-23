package cz.sirsi.ancestry.core.main;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.core.data.AdditionalFile;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.data.Group;
import cz.sirsi.ancestry.core.data.Partnership;
import cz.sirsi.ancestry.core.data.Person;
import cz.sirsi.ancestry.core.data.Place;
import cz.sirsi.ancestry.core.data.Relation;
import cz.sirsi.ancestry.core.data.comparator.PersonComparator;
import cz.sirsi.ancestry.core.tools.Tools;

/**
 * Object for storing and processing tree data
 * 
 * @author msiroky
 */
public class Tree {
	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(Tree.class);

	public static Group LIVE_GROUP;

	public static Group DEAD_GROUP;

	/**
	 * Singleton instance
	 */
	private static Tree treeInstance;

	/**
	 * @return Singleton instance of Tree
	 */
	public static Tree getInstance() {
		if (treeInstance != null) {
			return treeInstance;
		}

		treeInstance = new Tree();

		return treeInstance;
	}

	private String ancestryVersion;

	private int countOfVisibleCredentials;

	private int countOfVisibleDocuments;

	private int countOfVisiblePhotos;

	/**
	 * Map of all credentials
	 */
	private Map<Integer, AdditionalFile> credentialsMap;

	/**
	 * Map of all documents
	 */
	private Map<Integer, AdditionalFile> documentsMap;

	/**
	 * List of first chars of people
	 */
	private List<String> firstLetters;

	private Person mainPerson;

	/**
	 * Maximal hierarchy of person
	 */
	private int maxHierarchy;

	/**
	 * Minimal hierarchy of person
	 */
	private int minHierarchy;

	/**
	 * Mapping of partnership id to "firstPartnerId-secondPartnerId" format used in partnership map
	 */
	private Map<Integer, String> partnershipKeysMapping;

	/**
	 * Map of all partnerships
	 */
	private Map<String, Partnership> partnershipMap;

	/**
	 * Map of all people
	 */
	private Map<Integer, Person> peopleMap;

	/**
	 * Map of all photos
	 */
	private Map<Integer, AdditionalFile> photosMap;

	/**
	 * List of all places of people
	 */
	private List<Place> placesList;

	/**
	 * Singleton constructor
	 */
	private Tree() {
		// nothing
	}

	/**
	 * Fill in childrenList and siblingsList
	 */
	public void computeAditionalData() {
		Main.status.printOutputMessage("progress.computeAditionalData");
		log.info("Computing additional data of people");

		int counter = 0;

		this.mainPerson = null;

		for (Person person : this.peopleMap.values()) {
			person.reset();
		}

		for (Person person : this.peopleMap.values()) {
			if (counter++ % 50 == 0) {
				Main.status.setProcessStatus(Tree.class, ".computePerson." + counter);
				log.debug("Compute aditional data for person " + counter);
				Main.status.printOutputMessage("progress.computeAditionalData.person", Integer.toString(counter - 1));
			}

			for (Person searched : this.peopleMap.values()) {
				if (person == searched) {
					continue;
				}
				boolean father = searched.getFathersId() != null ? searched.getFathersId().equals(person.getFathersId())
						: false;
				boolean mother = searched.getMothersId() != null ? searched.getMothersId().equals(person.getMothersId())
						: false;
				if (father && mother) {
					person.addRightSibling(searched.getId());
				} else if (father || mother) {
					person.addHalfSibling(searched.getId());
				}
			}

			for (Person searched : this.peopleMap.values()) {
				if (person == searched) {
					continue;
				}
				if ((person.isMale() && person.getId().equals(searched.getFathersId()))
						|| (person.isFemale() && person.getId().equals(searched.getMothersId()))) {
					person.addChild(searched.getId());
				}
			}
		}

		Main.status.setProcessStatus(Tree.class, ".photos");
		this.countOfVisiblePhotos = 0;
		for (AdditionalFile photo : this.photosMap.values()) {
			boolean visible = true;

			if (photo.getAllPeople().size() == 0) {
				visible = false;
			}

			int people = 0;
			for (Person person : photo.getAllPeople()) {

				if (person.isHidden() && Config.getInstance().isBlockHidden()) {
					visible = false;
					break;
				}
				if (Config.getInstance().isHidePhotos(person.isLive())) {
					visible = false;
					break;
				}
				if (!person.isHidden()) {
					people++;
				}
			}

			if (people == 0) {
				visible = false;
			}

			if (visible) {
				this.countOfVisiblePhotos++;
			}
			photo.setVisible(visible);
		}

		this.countOfVisibleDocuments = 0;
		for (AdditionalFile document : this.documentsMap.values()) {
			boolean visible = true;

			if (document.getAllPeople().size() == 0) {
				visible = false;
			}

			int people = 0;
			for (Person person : document.getAllPeople()) {
				if (person.isHidden() && Config.getInstance().isBlockHidden()) {
					visible = false;
					break;
				}
				if (Config.getInstance().isHideDocuments(person.isLive())) {
					visible = false;
					break;
				}
				if (!person.isHidden()) {
					people++;
				}
			}

			if (people == 0) {
				visible = false;
			}

			if (visible) {
				this.countOfVisibleDocuments++;
			}
			document.setVisible(visible);
		}

		this.countOfVisibleCredentials = 0;
		for (AdditionalFile credential : this.credentialsMap.values()) {
			boolean visible = true;

			if (credential.getAllPeople().size() == 0) {
				visible = false;
			}

			int people = 0;
			for (Person person : credential.getAllPeople()) {
				if (person.isHidden() && Config.getInstance().isBlockHidden()) {
					visible = false;
					break;
				}
				if (Config.getInstance().isHideCredentials(person.isLive())) {
					visible = false;
					break;
				}
				if (!person.isHidden()) {
					people++;
				}
			}

			if (people == 0) {
				visible = false;
			}

			if (visible) {
				this.countOfVisibleCredentials++;
			}
			credential.setVisible(visible);
		}

		computeRelations();
		computeHierarchyOfAllPeople();

		Main.status.printOutputMessage("progress.computeAditionalDataDone");
		log.info("Computing data done.");
	}

	public void computeFirstLetters() {
		int counter;
		Person[] people = this.peopleMap.values().toArray(new Person[0]);
		Arrays.sort(people, new PersonComparator(PersonComparator.SECOND, PersonComparator.FIRST));
		String lastLetter = "";
		log.debug("Compute first letters for people");
		counter = 0;
		this.firstLetters.clear();

		for (Person person : people) {
			if (person.isHidden()) {
				continue;
			}

			if (counter++ % 50 == 0) {
				Main.status.setProcessStatus(Tree.class, ".firstLetters-" + counter);
			}

			String letter = Tools.getFirstLetter(person.getName());
			// for example � and c has the same position in alphabet, so compare
			// first letters by collator
			Config.getInstance().getCollator().setStrength(Collator.PRIMARY);
			if (Config.getInstance().getCollator().compare(letter, lastLetter) != 0) {
				this.firstLetters.add(letter);
				lastLetter = letter;
			}
		}
	}

	/**
	 * Computes hierarchy for given person
	 * 
	 * @param person Person
	 * @param hierarchy Hierarchy to set
	 * @return 1 in case hierarchy was set, 0 in case person has set hierarchy still
	 */
	private int computeHierarchyForPerson(Person person, Integer hierarchy) {
		if (person != null && person.getHierarchy() == Integer.MIN_VALUE) {
			person.setHierarchy(hierarchy);
			if ((this.minHierarchy > hierarchy) && (!person.isHidden())) {
				this.minHierarchy = hierarchy;
			}
			if ((this.maxHierarchy < hierarchy) && (!person.isHidden())) {
				this.maxHierarchy = hierarchy;
			}
			return 1;
		}
		return 0;
	}

	/**
	 * Computes hierarchy of all people in tree
	 */
	private void computeHierarchyOfAllPeople() {
		Person firstPerson = getMainPerson();

		firstPerson.setHierarchy(0);

		int count = 1;
		int counter = 0;
		while (count > 0) {
			count = 0;
			if (counter++ % 50 == 0) {
				Main.status.setProcessStatus(Tree.class, ".hierarchy." + counter);
			}

			for (Person person : this.peopleMap.values()) {
				Integer hierarchy = person.getHierarchy();

				if (hierarchy != Integer.MIN_VALUE) {
					count += computeHierarchyForPerson(person.getMother(), hierarchy + 1);
					count += computeHierarchyForPerson(person.getFather(), hierarchy + 1);
					for (Person child : person.getChildren(false)) {
						count += computeHierarchyForPerson(child, hierarchy - 1);
					}
					for (Person child : person.getSiblings(false)) {
						count += computeHierarchyForPerson(child, hierarchy);
					}
					for (Person child : person.getPartners(false)) {
						count += computeHierarchyForPerson(child, hierarchy);
					}
				}
			}
		}
	}

	private int computePathToMainForPerson(Person person, Integer pathLength) {
		if (person != null && person.getTemp() == Integer.MIN_VALUE) {
			person.setTemp(pathLength);
			return 1;
		}
		return 0;
	}

	private void computeRelations() {
		for (Person person : this.peopleMap.values()) {
			person.setTemp(Integer.MIN_VALUE);
		}

		Person firstPerson = getMainPerson();

		// start of wave algoritm
		firstPerson.setTemp(0);

		int count = 1;

		while (count > 0) {
			count = 0;

			for (Person person : this.peopleMap.values()) {
				int pathLength = person.getTemp();

				if (pathLength != Integer.MIN_VALUE) {
					count += computePathToMainForPerson(person.getMother(), pathLength + 1);
					count += computePathToMainForPerson(person.getFather(), pathLength + 1);
					for (Person child : person.getChildren(false)) {
						count += computePathToMainForPerson(child, pathLength + 1);
					}
					for (Person child : person.getSiblings(false)) {
						count += computePathToMainForPerson(child, pathLength + 1);
					}
					for (Person child : person.getPartners(false)) {
						count += computePathToMainForPerson(child, pathLength + 1);
					}
				}
			}
		}
		// end of wave algoritm

		for (Person person : this.peopleMap.values()) {
			Person currentPerson = person;
			List<Relation> relations = person.getRelations();
			while (true) {
				boolean find = false;

				int pathLength = currentPerson.getTemp();
				if (pathLength <= 0) {
					break;
				}

				if (currentPerson.getMother() != null && currentPerson.getMother().getTemp() + 1 == pathLength) {
					relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.DAUGHTER
							: Relation.RelationEnum.SON));
					currentPerson = currentPerson.getMother();
					continue;
				}
				if (currentPerson.getFather() != null && currentPerson.getFather().getTemp() + 1 == pathLength) {
					relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.DAUGHTER
							: Relation.RelationEnum.SON));
					currentPerson = currentPerson.getFather();
					continue;
				}
				if (currentPerson.getPartners(false) != null) {
					for (Person partner : currentPerson.getPartners(false)) {
						if (partner.getTemp() + 1 == pathLength) {
							if (partner.getPartnership(currentPerson.getId()).isMarried()) {
								relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.WIFE
										: Relation.RelationEnum.HUSBAND));
							} else {
								relations
										.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.WOMAN_PARTNER
												: Relation.RelationEnum.MAN_PARTNER));
							}
							currentPerson = partner;
							find = true;
							break;
						}
					}
					if (find) {
						continue;
					}
				}
				if (currentPerson.getChildren(false) != null) {
					for (Person child : currentPerson.getChildren(false)) {
						if (child.getTemp() + 1 == pathLength) {
							relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.MOTHER
									: Relation.RelationEnum.FATHER));
							currentPerson = child;
							find = true;
							break;
						}
					}
					if (find) {
						continue;
					}
				}
				if (currentPerson.getSiblings(false) != null) {
					for (Person sibling : currentPerson.getRightSiblings()) {
						if (sibling.getTemp() + 1 == pathLength) {
							relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.SISTER
									: Relation.RelationEnum.BROTHER));
							currentPerson = sibling;
							find = true;
							break;
						}
					}
					if (find) {
						continue;
					}
				}
				if (currentPerson.getSiblings(false) != null) {
					for (Person sibling : currentPerson.getHalfSiblings()) {
						if (sibling.getTemp() + 1 == pathLength) {
							relations.add(new Relation(currentPerson, currentPerson.isFemale() ? Relation.RelationEnum.HALF_SISTER
									: Relation.RelationEnum.HALF_BROTHER));
							currentPerson = sibling;
							find = true;
							break;
						}
					}
					if (find) {
						continue;
					}
				}
			}

			relations.add(new Relation(currentPerson, Relation.RelationEnum.MAIN_PERSON));
		}
	}

	/**
	 * @return the ancestryVersion
	 */
	public String getAncestryVersion() {
		return this.ancestryVersion;
	}

	/**
	 * @return the visibleCredentials
	 */
	public int getCountOfVisibleCredentials() {
		return this.countOfVisibleCredentials;
	}

	/**
	 * @return the visibleDocuments
	 */
	public int getCountOfVisibleDocuments() {
		return this.countOfVisibleDocuments;
	}

	/**
	 * @return the visiblePhotos
	 */
	public int getCountOfVisiblePhotos() {
		return this.countOfVisiblePhotos;
	}

	/**
	 * @return the credentialsMap
	 */
	public Map<Integer, AdditionalFile> getCredentialsMap() {
		return this.credentialsMap;
	}

	/**
	 * @return the additionalFilesMap
	 */
	public Map<Integer, AdditionalFile> getDocumentsMap() {
		return this.documentsMap;
	}

	/**
	 * @return List of first letters of names
	 */
	public List<String> getFirstLetters() {
		return this.firstLetters;
	}

	/**
	 * Gets main person (by the users configuration or active person in rod file). Computes main person if needed (on
	 * first call) included recompute of mainPersonId in Config
	 * 
	 * @return Main person
	 */
	private Person getMainPerson() {
		if (this.mainPerson != null) {
			return this.mainPerson;
		}

		for (Person person : this.peopleMap.values()) {
			if (Config.getInstance().getMainPersonId().equals(person.getIdent())) {
				this.mainPerson = person;
			}
		}
		if (this.mainPerson == null) {
			this.mainPerson = this.peopleMap.get(Tools.parseInt(Global.getInstance().getActive(), -1));
		}
		if (this.mainPerson == null) {
			this.mainPerson = this.peopleMap.values().iterator().next();
		}

		Config.getInstance().setMainPersonId(this.mainPerson.getIdent());
		return this.mainPerson;
	}

	/**
	 * @return the maxHierarchy
	 */
	public int getMaxHierarchy() {
		return this.maxHierarchy;
	}

	/**
	 * @return the minHierarchy
	 */
	public int getMinHierarchy() {
		return this.minHierarchy;
	}

	/**
	 * @return the partnershipKeysMapping
	 */
	public Map<Integer, String> getPartnershipKeysMapping() {
		return this.partnershipKeysMapping;
	}

	/**
	 * @return the partnerMap
	 */
	public Map<String, Partnership> getPartnershipMap() {
		return this.partnershipMap;
	}

	/**
	 * @return the peopleMap
	 */
	public Map<Integer, Person> getPeopleMap() {
		return this.peopleMap;
	}

	/**
	 * @return the photosMap
	 */
	public Map<Integer, AdditionalFile> getPhotosMap() {
		return this.photosMap;
	}

	/**
	 * @return the placesList
	 */
	public List<Place> getPlacesList() {
		return this.placesList;
	}

	/**
	 * Resets all properties to default on start of generating
	 */
	public void reset() {
		this.placesList = new ArrayList<Place>();
		this.credentialsMap = new HashMap<Integer, AdditionalFile>();
		this.documentsMap = new HashMap<Integer, AdditionalFile>();
		this.firstLetters = new ArrayList<String>();
		this.maxHierarchy = Integer.MIN_VALUE;
		this.minHierarchy = Integer.MAX_VALUE;
		this.photosMap = new HashMap<Integer, AdditionalFile>();
		this.peopleMap = new HashMap<Integer, Person>();
		this.partnershipMap = new HashMap<String, Partnership>();
		this.partnershipKeysMapping = new HashMap<Integer, String>();
	}

	/**
	 * Adds special groups to list of groups
	 */
	public void addSpecialGroups() {
		LIVE_GROUP = new cz.sirsi.ancestry.core.data.Group(MessagesTools.getGuiMessages().getMessage(
				"group.special.living", "_living", null, false));
		Global.getInstance().addGroup(LIVE_GROUP);
		DEAD_GROUP = new cz.sirsi.ancestry.core.data.Group(MessagesTools.getGuiMessages().getMessage("group.special.dead",
				"_dead", null, false));
		Global.getInstance().addGroup(DEAD_GROUP);
	}

	/**
	 * @param ancestryVersion the ancestryVersion to set
	 */
	public void setAncestryVersion(String ancestryVersion) {
		this.ancestryVersion = ancestryVersion;
	}
}