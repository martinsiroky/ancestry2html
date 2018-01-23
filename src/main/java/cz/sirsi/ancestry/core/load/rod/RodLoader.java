/*
 * 
 */
package cz.sirsi.ancestry.core.load.rod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.Warnings;
import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.data.AdditionalFile;
import cz.sirsi.ancestry.core.data.Education;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.data.Group;
import cz.sirsi.ancestry.core.data.Job;
import cz.sirsi.ancestry.core.data.LivePlace;
import cz.sirsi.ancestry.core.data.Partnership;
import cz.sirsi.ancestry.core.data.Person;
import cz.sirsi.ancestry.core.exceptions.AppException;
import cz.sirsi.ancestry.core.main.Main;
import cz.sirsi.ancestry.core.main.Tree;
import cz.sirsi.ancestry.core.tools.Tools;

/**
 * Used for loading data from old rod format. Supported versions are 1.0.19, 1.0.20, 1.0.21 (UTF-8)
 * 
 * @author msiroky
 */
public class RodLoader {

	/**
	 * Singleton instance
	 */
	private static RodLoader instance;

	/**
	 * Counter for partnerships
	 */
	public static int lastUsedPartnershipId;

	/**
	 * Initializes logger for this class
	 **/
	private static Logger log = Logger.getLogger(RodLoader.class);

	/**
	 * @return Singleton instance of RodLoader
	 */
	public static RodLoader getInstance() {
		if (instance != null) {
			return instance;
		}

		instance = new RodLoader();

		return instance;
	}

	/**
	 * @param people Map of all people
	 * @param partner1 Id of first partner
	 * @param partner2 Id of second partner
	 * @param type Type of partnership. 0=partnership, 1=married, 2=divorced
	 * @param tag1
	 * @param tag2
	 * @param marriageDate Date of marriage
	 * @param marriagePlace Place of marriage
	 * @param priest Priest
	 * @param witness Witness
	 * @param divorceDate Date of divorce
	 */
	public static void processMarriage(Map<Integer, Person> people, Integer partner1, Integer partner2, int type, int tag1, int tag2,
			String marriageDate, String marriagePlace, String priest, String witness, String divorceDate) {
		Person firstPartner = people.get(partner1);
		Person secondPartner = people.get(partner2);
		if (firstPartner != null && secondPartner != null && partner1 != null) {
			firstPartner.addPartnersId(partner2);
			secondPartner.addPartnersId(partner1);

			Integer partnershipId = lastUsedPartnershipId++;
			String partnershipStringId = partner1.compareTo(partner2) > 0 ? partner1 + "-" + partner2 : partner2 + "-" + partner1;
			Partnership partnership = new Partnership(partnershipId);

			if (firstPartner.isFemale() && secondPartner.isMale()) {
				partnership.setMan(secondPartner);
				partnership.setWoman(firstPartner);
				partnership.setManSortOrder(tag1);
				partnership.setWomanSortOrder(tag2);
			} else {
				partnership.setMan(firstPartner);
				partnership.setWoman(secondPartner);
				partnership.setManSortOrder(tag2);
				partnership.setWomanSortOrder(tag1);
			}

			partnership.setDivorced(type == 2);
			partnership.setMarried(type > 0);
			partnership.setMarriageDate(marriageDate);
			partnership.setMarriagePlace(marriagePlace);
			partnership.setPriest(priest);
			partnership.setWitness(witness);
			partnership.setDivorceDate(divorceDate);

			Tree.getInstance().getPartnershipKeysMapping().put(partnershipId, partnershipStringId);
			Tree.getInstance().getPartnershipMap().put(partnershipStringId, partnership);
		}
	}

	/**
	 * Singleton constructor
	 */
	private RodLoader() {
		// nothing
	}

	/**
	 * Load global data and peoples from file
	 * 
	 * @param file File to load
	 * @throws AppException In case reading rod file failed
	 */
	public void loadProject(File file) throws AppException {
		Main.status.printOutputMessage("progress.readingRod");
		log.info("Reading rod file: " + file.getAbsolutePath());
		lastUsedPartnershipId = 0;

		BufferedReader reader = null;
		try {
			String[] encodings = { "utf-8", "windows-1250" };
			String line;
			String memoryLine = "";
			int counter = 0;

			for (String encoding : encodings) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
				memoryLine = null;
				counter = 0;

				while (!(line = reader.readLine()).startsWith("verze")) {
					// ignore these lines
				}
				String ancestryVersion = line.substring(6);
				log.info("Ancestry version=" + ancestryVersion);

				// Check supported version
				// TODO intelligent check of version
				String[] version = ancestryVersion.split("\\.");
				if ("1".equals(version[0]) && "0".equals(version[1]) && Integer.parseInt(version[2]) < 19) {
					String message = "Unsupported version of rod file " + ancestryVersion + ". Minimal version is 1.0.19.";
					log.error(message);
					throw new AppException(message);
				}
				Tree.getInstance().setAncestryVersion(ancestryVersion);

				if ("utf-8".equals(encoding)) {
					if (Integer.parseInt(version[2]) > 20) {
						break;
					}
					IOUtils.closeQuietly(reader);
				}
			}

			if (reader != null) {
				while ((line = memoryLine == null ? reader.readLine() : memoryLine) != null) {
					log.debug("line=" + line);
					if (counter++ % 50 == 0) {
						Main.status.setProcessStatus(RodLoader.class, ".loadRodFile-" + counter);
						Main.status.printOutputMessage("process.readingRodLine", Integer.toString(counter - 1), "?");
					}

					if (line.startsWith(Constants.ROD_GLOBAL_TAG)) {
						memoryLine = null;
						memoryLine = readGlobal(reader);
					} else if (line.startsWith(Constants.ROD_PERSON_TAG)) {
						memoryLine = null;
						memoryLine = readPerson(reader, Tree.getInstance().getPeopleMap());
					} else if (line.startsWith(Constants.ROD_MARRIAGE_TAG)) {
						memoryLine = null;
						memoryLine = readMarriage(reader, Tree.getInstance().getPeopleMap());
					} else {
						memoryLine = null;
					}
				}
			}

			Map<Integer, Person> people = Tree.getInstance().getPeopleMap();
			for (Person person : people.values()) {
				// sets groups of person
				for (Group g : Global.getInstance().getGroups()) {
					if (g.getMembers().contains(person.getId())) {
						person.addGroup(g.getName());
					}
				}

				if (!((Config.getInstance().getGroupsOnly().isEmpty() || Tools.isInGroup(person.getGroups(), Config.getInstance().getGroupsOnly())) && (!Tools
						.isInGroup(person.getGroups(), Config.getInstance().getGroupsHide())))) {
					person.setHidden(true);
				}
			}

			Main.status.printOutputMessage("progress.readingRodDone");
			log.info("Reading rod file successful");

			if (Tree.getInstance().getPeopleMap().size() == 0) {
				throw new AppException("Generating failed, no people in tree.");
			}
		} catch (IOException ex) {
			String message = "Reading from rod file failed.";
			log.error(message, ex);
			throw new AppException(message, ex);
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	private AdditionalFile readAdditionalFileInfo(BufferedReader reader, boolean jpg) throws IOException {
		String identifier = reader.readLine().trim();
		String externalFileName = null;
		if ("ext".equals(identifier)) {
			externalFileName = reader.readLine().trim();
			identifier = reader.readLine().trim();
		}
		int id = Integer.parseInt(identifier);
		String name = reader.readLine().trim();
		String year = reader.readLine().trim();
		StringBuffer description = new StringBuffer();

		String extension = jpg ? "jpg" : reader.readLine().trim();

		int descriptionRows = Integer.parseInt(reader.readLine().trim());

		for (int j = 0; j < descriptionRows; j++) {
			description.append(reader.readLine().trim());
			description.append(" ");
		}

		AdditionalFile additionalFile = new AdditionalFile(id, name, year, description.toString().trim(), externalFileName, extension);
		return additionalFile;
	}

	/**
	 * Reads global data from rod file
	 * 
	 * @param reader Reader
	 * @return Last read line (it is first line of next record)
	 * @throws IOException In case reading failed
	 */
	private String readGlobal(BufferedReader reader) throws IOException {
		log.debug("Reading global data");
		String line;
		String memoryLine = null;

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("<")) {
				memoryLine = line;
				break;
			}

			if (!line.startsWith("[")) {
				continue;
			}

			GlobalTagEnum item = GlobalTagEnum.getForRodTag(line);

			if (item != null) {
				try {
					Class<?> clazz = Class.forName("cz.sirsi.ancestry.core.data.Global");
					Method method = clazz.getMethod(item.getSetterMethod(), String.class);
					method.invoke(Global.getInstance(), reader.readLine().trim());
					continue;
				} catch (Exception e) {
					log.error("Can not set property", e);
					Warnings.getInstance().addWarning("Can not set property value for tag " + item.getRodTag() + ". ");
				}
			}

			if (line.startsWith("[skupiny]")) {
				int countOfGroups = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < countOfGroups; i++) {
					Group group = new Group(reader.readLine().trim());

					int countOfMembers = Integer.parseInt(reader.readLine().trim());

					for (int j = 0; j < countOfMembers; j++) {
						group.addMember(Integer.parseInt(reader.readLine().trim()));
					}
					Global.getInstance().addGroup(group);
				}
			}

			if (line.startsWith("[fotky]")) {
				int countOfPhotos = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < countOfPhotos; i++) {
					AdditionalFile photo = readAdditionalFileInfo(reader, true);

					Tree.getInstance().getPhotosMap().put(photo.getId(), photo);
				}
			}

			if (line.startsWith("[soubory]")) {
				int countOfFiles = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < countOfFiles; i++) {
					AdditionalFile document = readAdditionalFileInfo(reader, false);

					Tree.getInstance().getDocumentsMap().put(document.getId(), document);
				}
			}

			if (line.startsWith("[dokumenty]")) {
				int countOfFiles = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < countOfFiles; i++) {
					AdditionalFile credential = readAdditionalFileInfo(reader, true);

					Tree.getInstance().getCredentialsMap().put(credential.getId(), credential);
				}
			}
		}

		Tree.getInstance().addSpecialGroups();

		log.debug("Global data read successfully");
		return memoryLine;
	}

	/**
	 * Reads marriage data from rod file
	 * 
	 * @param reader Reader
	 * @param people Map with all people
	 * @return Last read line (it is first line of next record)
	 * @throws IOException In case reading failed
	 */
	private String readMarriage(BufferedReader reader, Map<Integer, Person> people) throws IOException {
		String line;
		String memoryLine = null;
		Integer partner1 = null;
		Integer partner2 = null;
		int type = 0;
		int tag1 = 0;
		int tag2 = 0;
		String marriageDate = null;
		String marriagePlace = null;
		String priest = null;
		String witness = null;
		String divorceDate = null;

		while ((line = reader.readLine()) != null) {
			log.debug("line=" + line);
			if (line.startsWith("<")) {
				memoryLine = line;
				break;
			}

			if (line.startsWith("[partner1]")) {
				partner1 = Integer.parseInt(reader.readLine().trim());
			}

			if (line.startsWith("[partner2]")) {
				partner2 = Integer.parseInt(reader.readLine().trim());
			}

			if (line.startsWith("[typ]")) {
				type = Integer.parseInt(reader.readLine().trim());
			}

			if (line.startsWith("[obradText]")) {
				marriageDate = reader.readLine().trim();
			}

			if (line.startsWith("[svatbaMisto]")) {
				marriagePlace = reader.readLine().trim();
			}

			if (line.startsWith("[oddavajiciKnez]")) {
				priest = reader.readLine().trim();
			}

			if (line.startsWith("[svedkove]")) {
				witness = reader.readLine().trim();
			}

			if (line.startsWith("[rozvodText]")) {
				divorceDate = reader.readLine().trim();
			}

			if (line.startsWith("[tag1]")) {
				tag1 = Integer.parseInt(reader.readLine().trim());
			}

			if (line.startsWith("[tag2]")) {
				tag2 = Integer.parseInt(reader.readLine().trim());
			}
		}

		processMarriage(people, partner1, partner2, type, tag1, tag2, marriageDate, marriagePlace, priest, witness, divorceDate);

		log.debug("Marriage data read successfully");
		return memoryLine;
	}

	/**
	 * Reads one person from rod file
	 * 
	 * @param reader Reader
	 * @param people Map with all people
	 * @return Last read line (it is first line of next record)
	 * @throws IOException In case reading file failed
	 */
	private String readPerson(BufferedReader reader, Map<Integer, Person> people) throws IOException {
		log.debug("Reading data of one person");
		String line;
		String memoryLine = null;
		Person person = new Person();

		while ((line = reader.readLine()) != null) {
			if (line.startsWith("<")) {
				memoryLine = line;
				break;
			}

			if (!line.startsWith("[")) {
				continue;
			}

			if (line.startsWith("[cislo]")) {
				person.setId(reader.readLine().trim());
				people.put(person.getId(), person);
			}

			if (line.startsWith("[foto]")) {
				int count = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < count; i++) {
					person.addPhoto(Integer.parseInt(reader.readLine().trim()));
				}
			}

			if (line.startsWith("[soubory]")) {
				int count = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < count; i++) {
					person.addDocument(Integer.parseInt(reader.readLine().trim()));
				}
			}

			if (line.startsWith("[dokumenty]")) {
				int count = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < count; i++) {
					person.addCredential(Integer.parseInt(reader.readLine().trim()));
				}
			}

			PersonTagEnum item = PersonTagEnum.getForRodTag(line);

			if (item != null) {
				try {
					Class<?> clazz = Class.forName("cz.sirsi.ancestry.core.data.Person");
					Method method = clazz.getMethod(item.getSetterMethod(), String.class);
					method.invoke(person, reader.readLine().trim());
				} catch (Exception e) {
					log.error("Can not set property", e);
					Warnings.getInstance().addWarning("Can not set property value for tag " + item.getRodTag() + ". ");
				}
			}

			if (line.startsWith("[bydliste]")) {
				person.addPlace(new LivePlace(reader.readLine().trim(), reader.readLine().trim(), reader.readLine().trim()));
			}

			if (line.startsWith("[povolani]")) {
				person.addJob(new Job(reader.readLine().trim(), reader.readLine().trim(), reader.readLine().trim()));
			}

			if (line.startsWith("[vzdelani]")) {
				person.addEducation(new Education(reader.readLine().trim(), reader.readLine().trim(), reader.readLine().trim()));
			}

			if (line.startsWith("[matriky]")) {
				int count = Integer.parseInt(reader.readLine().trim());
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < count; i++) {
					if (sb.length() > 0) {
						sb.append("\n");
					}
					sb.append(reader.readLine().trim());
				}
				person.setRegistryInfo(sb.toString());
			}

			if (line.startsWith("[doplnujiciTexty]")) {
				int count = Integer.parseInt(reader.readLine().trim());
				for (int i = 0; i < count; i++) {
					person.addAdditionalText(reader.readLine().trim());
				}
			}
		}

		if (person.isLive()) {
			Tree.LIVE_GROUP.addMember(person.getId());
		}

		if (person.isDead()) {
			Tree.DEAD_GROUP.addMember(person.getId());
		}

		log.debug("One person read successfully");
		return memoryLine;
	}

}
