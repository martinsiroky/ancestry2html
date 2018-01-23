package cz.sirsi.ancestry.core.load.rodx;

import static cz.sirsi.ancestry.core.tools.Tools.isInGroup;
import static cz.sirsi.ancestry.core.tools.Tools.trim;

import java.io.File;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.AdditionalFile;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.data.Job;
import cz.sirsi.ancestry.core.data.LivePlace;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.load.rod.RodLoader;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.Documents.Document;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.Groups.Group;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.Partnerships.Partnership;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Birth;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Death;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Documents;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Education;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Files;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Occupation;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Photos;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.People.Person.Residence;
import cz.sirsi.ancestry.core.load.rodx.Ancestry.Photos.Photo;
import cz.sirsi.ancestry.core.main.Main;
import cz.sirsi.ancestry.core.main.Tree;

/**
 * Used for loading data from new rodx format. Since version 1.0.21
 * 
 * @author msiroky
 */
public class RodxLoader {
	/**
	 * Singleton instance of RodxLoader
	 */
	private static RodxLoader instance;

	/**
	 * Initializes logger for this class
	 **/
	private static Logger log = Logger.getLogger(RodxLoader.class);

	/**
	 * @return Singleton instance of RodxLoader
	 */
	public static RodxLoader getInstance() {
		if (instance != null) {
			return instance;
		}

		instance = new RodxLoader();

		return instance;
	}

	/**
	 * Singleton constructor
	 */
	private RodxLoader() {
		// nothing
	}

	/**
	 * Load global data and peoples from file
	 * 
	 * @param projectFile File to load
	 * @throws AppException In case reading rod file failed
	 */
	public void loadProject(File projectFile) throws AppException {
		Main.status.printOutputMessage("progress.readingRodx");
		Main.status.setProcessStatus(RodxLoader.class, ".read");
		log.info("Reading rodx file: " + projectFile.getAbsolutePath());

		Tree tree = Tree.getInstance();

		try {
			JAXBContext jc = JAXBContext.newInstance("cz.sirsi.ancestry.core.load.rodx");

			Unmarshaller unmarshaller = jc.createUnmarshaller();

			Main.status.setProcessStatus(RodxLoader.class, "read.unmarshal");
			Ancestry xmlData = (Ancestry) unmarshaller.unmarshal(projectFile);
			Main.status.setProcessStatus(RodxLoader.class, "read.unmarshal.done");

			// TODO store version as is in XML to tree
			tree.setAncestryVersion(xmlData.getVersion().getMajorVersion() + "." + xmlData.getVersion().getMinorVersion()
					+ "." + xmlData.getVersion().getRelease() + "." + xmlData.getVersion().getBuild());

			transformCredentials(xmlData);
			transformDocuments(xmlData);
			transgormPhotos(xmlData);

			transformGlobal(xmlData);

			transformGroups(xmlData);

			Map<Integer, cz.sirsi.ancestry.core.data.Person> people = tree.getPeopleMap();
			for (Person personXmlData : xmlData.getPeople().getPerson()) {
				cz.sirsi.ancestry.core.data.Person person = new cz.sirsi.ancestry.core.data.Person();

				person.setId(Long.toString(personXmlData.getNumber()));

				Birth birth = personXmlData.getBirth();
				if (birth != null) {
					person.setBirthDate(birth.getDate());
					person.setBirthPlace(birth.getPlace());
				}

				Death death = personXmlData.getDeath();
				if (death != null) {
					person.setBurialPlace(death.getBurialPlace());
					person.setDeathCause(death.getCause());
					person.setDeathDate(death.getDate());
					person.setDeathPlace(death.getPlace());
				}

				if (personXmlData.getMother() != null) {
					person.setMothersId(Long.toString(personXmlData.getMother()));
				}

				if (personXmlData.getFather() != null) {
					person.setFathersId(Long.toString(personXmlData.getFather()));
				}

				if (personXmlData.getGender().equals(GenderType.M)) {
					person.setMale();
				} else {
					person.setFemale();
				}

				TextLines additionalText = personXmlData.getAdditionalText();
				if (additionalText != null) {
					for (String text : additionalText.getTextLine()) {
						person.addAdditionalText(text);
					}
				}

				transformAdditionalFiles(person, personXmlData);

				transformEducations(person, personXmlData);
				transformOccupations(person, personXmlData);
				transformLivePlaces(personXmlData, person);

				person.setChangeDate(personXmlData.getLastChange());
				person.setChristening(personXmlData.getChristening());
				person.setCitizenship(personXmlData.getCitizenship());
				person.setEmail1(personXmlData.getEmail1());
				person.setEmail2(personXmlData.getEmail2());
				person.setHead(personXmlData.isHead());
				person.setHeadFile(personXmlData.getHeadFileIfLinked());
				person.setFirstname(personXmlData.getFirstName());
				person.setGodparent1(personXmlData.getGodparent1());
				person.setGodparent2(personXmlData.getGodparent2());
				person.setHeadFile(personXmlData.getHeadFileIfLinked());
				person.setIdent(personXmlData.getID());
				person.setInsertDate(personXmlData.getInsertDate());
				person.setLastname(personXmlData.getLastName());
				person.setLive(personXmlData.isIsLiving());
				person.setMaidenname(personXmlData.getBirthName());
				person.setNationality(personXmlData.getNationality());
				person.setPhone1(personXmlData.getPhone1());
				person.setPhone2(personXmlData.getPhone2());
				person.setRegistryInfo(transformTextLines(personXmlData.getRegistryText()));
				person.setReligion(personXmlData.getReligion());
				person.setTitleFront(personXmlData.getPrefix());
				person.setTitleBack(personXmlData.getSuffix());
				person.setUserDef1(personXmlData.getUser1());
				person.setUserDef2(personXmlData.getUser2());
				person.setUserDef3(personXmlData.getUser3());
				person.setUserDef4(personXmlData.getUser4());
				person.setUserDef5(personXmlData.getUser5());
				person.setWeb1(personXmlData.getWWW1());
				person.setWeb2(personXmlData.getWWW2());

				if (person.isLive()) {
					Tree.LIVE_GROUP.addMember(person.getId());
				}

				if (person.isDead()) {
					Tree.DEAD_GROUP.addMember(person.getId());
				}

				// sets groups of person
				for (cz.sirsi.ancestry.core.data.Group g : Global.getInstance().getGroups()) {
					if (g.getMembers().contains(person.getId())) {
						person.addGroup(g.getName());
					}
				}

				people.put(person.getId(), person);

				if (!((Config.getInstance().getGroupsOnly().isEmpty() || isInGroup(person.getGroups(), Config.getInstance()
						.getGroupsOnly())) && (!isInGroup(person.getGroups(), Config.getInstance().getGroupsHide())))) {
					person.setHidden(true);
				}
			}

			transformPartnership(xmlData);

			Main.status.setProcessStatus(RodxLoader.class, "read.done");

		} catch (JAXBException e) {
			String message = "Reading from rodx file failed.";
			log.error(message, e);
			throw new AppException(message, e);
		}
	}

	private void transformLivePlaces(Person personXmlData, cz.sirsi.ancestry.core.data.Person person) {
		for (Residence residence : personXmlData.getResidence()) {
			person.addPlace(new LivePlace(residence.getText() != null ? residence.getText() : "", residence.getFrom(),
					residence.getTo()));
		}
	}

	private void transformOccupations(cz.sirsi.ancestry.core.data.Person person, Person personXmlData) {
		for (Occupation occupation : personXmlData.getOccupation()) {
			person.addJob(new Job(occupation.getText() != null ? occupation.getText() : "", occupation.getFrom(), occupation
					.getTo()));
		}
	}

	private void transformEducations(cz.sirsi.ancestry.core.data.Person person, Person personXmlData) {
		for (Education education : personXmlData.getEducation()) {
			person.addEducation(new cz.sirsi.ancestry.core.data.Education(education.getText() != null ? education.getText()
					: "", education.getFrom(), education.getTo()));
		}
	}

	private void transformAdditionalFiles(cz.sirsi.ancestry.core.data.Person newPerson, Person person) {
		Documents documents2 = person.getDocuments();
		if (documents2 != null) {
			for (long id : documents2.getDocumentNumber()) {
				newPerson.addCredential(new Long(id).intValue());
			}
		}

		Photos photos2 = person.getPhotos();
		if (photos2 != null) {
			for (long id : photos2.getPhotoNumber()) {
				newPerson.addPhoto(new Long(id).intValue());
			}
		}

		Files files = person.getFiles();
		if (files != null) {
			for (long id : files.getFileNumber()) {
				newPerson.addDocument(new Long(id).intValue());
			}
		}
	}

	private void transformPartnership(Ancestry ancestry) {
		RodLoader.lastUsedPartnershipId = 0;
		for (Partnership partnership : ancestry.getPartnerships().getPartnership()) {
			RodLoader.processMarriage(Tree.getInstance().getPeopleMap(), new Long(partnership.getPartner1()).intValue(),
					new Long(partnership.getPartner2()).intValue(), partnership.getType(), partnership.getTag1().intValue(),
					partnership.getTag2().intValue(), partnership.getWeddingDate(), partnership.getWeddingPlace(), partnership
							.getPriest(), partnership.getWitnesses(), partnership.getDivorceDate());
		}
	}

	private void transformGroups(Ancestry ancestry) {
		for (Group group : ancestry.getGroups().getGroup()) {
			cz.sirsi.ancestry.core.data.Group newGroup = new cz.sirsi.ancestry.core.data.Group(group.getName());
			for (long id : group.getMembers().getMemberNumber()) {
				newGroup.addMember(new Long(id).intValue());
			}
			Global.getInstance().addGroup(newGroup);
		}

		Tree.getInstance().addSpecialGroups();
	}

	private void transformCredentials(Ancestry ancestry) {
		Map<Integer, AdditionalFile> credentials = Tree.getInstance().getCredentialsMap();
		for (Document document : ancestry.getDocuments().getDocument()) {
			int id = new Long(document.getIDNumber()).intValue();
			credentials.put(id, new AdditionalFile(id, trim(document.getName()), trim(document.getYear()),
					transformTextLines(document.getComment()), trim(document.getPathIfLinked()), "jpg"));
		}
	}

	private void transformDocuments(Ancestry ancestry) {
		Map<Integer, AdditionalFile> documents = Tree.getInstance().getDocumentsMap();
		for (cz.sirsi.ancestry.core.load.rodx.Ancestry.Files.File file : ancestry.getFiles().getFile()) {
			int id = new Long(file.getIDNumber()).intValue();
			documents.put(id, new AdditionalFile(id, trim(file.getName()), trim(file.getYear()), transformTextLines(file
					.getComment()), trim(file.getPathIfLinked()), trim(file.getType())));
		}
	}

	private void transgormPhotos(Ancestry ancestry) {
		Map<Integer, AdditionalFile> photos = Tree.getInstance().getPhotosMap();
		for (Photo photo : ancestry.getPhotos().getPhoto()) {
			int id = new Long(photo.getIDNumber()).intValue();
			photos.put(id, new AdditionalFile(id, trim(photo.getName()), trim(photo.getYear()), transformTextLines(photo
					.getComment()), trim(photo.getPathIfLinked()), "jpg"));
		}
	}

	private void transformGlobal(Ancestry ancestry) {
		Global global = Global.getInstance();
		if (ancestry.getGeneral().getActivePerson() != null) {
			global.setActive((ancestry.getGeneral().getActivePerson()).toString());
		}
		global.setAuthor(trim(ancestry.getGeneral().getAuthor()));
		global.setCooperated(trim(ancestry.getGeneral().getCoWorkers()));
		global.setCreated(trim(ancestry.getGeneral().getCreated()));
		global.setTitle(trim(ancestry.getGeneral().getName()));
		global.setAdditionalInfo(transformTextLines(ancestry.getGeneral().getAdditionalInformation()));
		global.setRodUser1(ancestry.getGeneral().getUserDefined().getUser1());
		global.setRodUser2(ancestry.getGeneral().getUserDefined().getUser2());
		global.setRodUser3(ancestry.getGeneral().getUserDefined().getUser3());
		global.setRodUser4(ancestry.getGeneral().getUserDefined().getUser4());
		global.setRodUser5(ancestry.getGeneral().getUserDefined().getUser5());
	}

	private String transformTextLines(TextLines lines) {
		if (lines == null) {
			return null;
		}
		StringBuffer text = new StringBuffer();
		for (String line : lines.getTextLine()) {
			if (text.length() > 0) {
				text.append("\n");
			}
			text.append(line);
		}
		return text.toString();
	}
}
