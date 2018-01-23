//Last revision 24.2.09
package cz.sirsi.ancestry.core.data;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.PersonIntervalTypeEnum;
import cz.sirsi.ancestry.core.main.Tree;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object that stores and computes statistics
 * 
 * @author msiroky
 */
public class Statistics {
	private static Logger log = Logger.getLogger(Statistics.class);

	private MyDateInterval averageAge = new MyDateInterval(0);
	private MyDateInterval averageFirstChildAge = new MyDateInterval(0);
	private MyDateInterval averageManAge = new MyDateInterval(0);

	private MyDateInterval averageManFirstChildAge = new MyDateInterval(0);
	private MyDateInterval averageManMarriageAge = new MyDateInterval(0);
	private MyDateInterval averageMarriageAge = new MyDateInterval(0);

	private MyDateInterval averageWomanAge = new MyDateInterval(0);
	private MyDateInterval averageWomanAgeOnFirstChild = new MyDateInterval(0);
	private MyDateInterval averageWomanAgeOnWedding = new MyDateInterval(0);

	private int countMen = 0;
	private int countPeople = 0;
	private int countWomen = 0;

	private FrequentData<String> frequentBirthDayData = new FrequentData<String>();
	private FrequentData<String> frequentBirthMonthData = new FrequentData<String>();
	private FrequentData<String> frequentDeathDayData = new FrequentData<String>();
	private FrequentData<String> frequentDeathMonthData = new FrequentData<String>();
	private FrequentData<String> frequentManNameData = new FrequentData<String>();
	private FrequentData<String> frequentWeddingDayData = new FrequentData<String>();
	private FrequentData<String> frequentWeddingMonthData = new FrequentData<String>();
	private FrequentData<String> frequentWomanNameData = new FrequentData<String>();

	private int generationCount;

	private int liveMen = 0;
	private int livePeople = 0;
	private int liveWomen = 0;

	private int maxCountChildren = 0;
	private List<Person> maxCountChildrenPeople = new ArrayList<Person>();

	private MyDateInterval maxDeadAge = new MyDateInterval(0);
	private MyDateInterval maxDeadManAge = new MyDateInterval(0);
	private MyDateInterval maxDeadWomanAge = new MyDateInterval(0);
	private MyDateInterval maxLiveAge = new MyDateInterval(0);
	private MyDateInterval maxLiveManAge = new MyDateInterval(0);
	private MyDateInterval maxLiveWomanAge = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnFirstGrandGrandchild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnFirstGrandchild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnFirstChild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnGrandGrandchild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnGrandchild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnChild = new MyDateInterval(0);
	private MyDateInterval maxManAgeOnWedding = new MyDateInterval(0);
	private MyDateInterval maxMarriageLength = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnFirstGrandGrandchild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnFirstGrandchild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnFirstChild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnGrandGrandchild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnGrandchild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnChild = new MyDateInterval(0);
	private MyDateInterval maxWomanAgeOnWedding = new MyDateInterval(0);

	private MyDateInterval minDeadAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minDeadManAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minDeadWomanAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minLiveAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minLiveManAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minLiveWomanAge = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minManAgeOnGrandGrandchild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minManAgeOnGrandchild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minManAgeOnChild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minManAgeOnWedding = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minMarriageLength = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minWomanAgeOnGrandGrandchild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minWomanAgeOnGrandchild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minWomanAgeOnChild = new MyDateInterval(Long.MAX_VALUE);
	private MyDateInterval minWomanAgeOnWedding = new MyDateInterval(Long.MAX_VALUE);

	private boolean sortedBirthDays = false;
	private boolean sortedDeathDays = false;
	private boolean sortedWeddingDays = false;

	/**
	 * Calls average method on each property that stores sum of intervals
	 */
	private void averageAllAvgIntervals() {
		if (log.isDebugEnabled()) {
			log.debug("Compute average data.");
		}

		this.averageAge.average();
		this.averageManAge.average();
		this.averageWomanAge.average();

		this.averageMarriageAge.average();
		this.averageManMarriageAge.average();
		this.averageWomanAgeOnWedding.average();

		this.averageFirstChildAge.average();
		this.averageManFirstChildAge.average();
		this.averageWomanAgeOnFirstChild.average();

		if (log.isDebugEnabled()) {
			log.debug("Computing average data done.");
		}
	}

	/**
	 * Computes statistics for whole project
	 */
	public void computeStatistics() {
		initializesFrequentMaps();

		computeStatisticsForPeople();
		computeStatisticsForPartnerships();

		normalizeAllMaxMinItervals();

		averageAllAvgIntervals();

		// sort only month data, because days must be sorted for each locale (days sorting is done in getters)
		sortFrequentMonthData();
	}

	/**
	 * Goes for each partnership and computes data useful for statistics
	 */
	private void computeStatisticsForPartnerships() {
		Tree tree = Tree.getInstance();

		if (log.isDebugEnabled()) {
			log.debug("Computing statistics for partnerships");
		}

		for (Partnership partnership : tree.getPartnershipMap().values()) {
			if (partnership.getMan().isHidden() || partnership.getWoman().isHidden()) {
				continue;
			}

			checkFrequendData(partnership.getMarriageDateObj(), this.frequentWeddingMonthData, this.frequentWeddingDayData);

			MyDateInterval marriageLengthForMaximum = partnership.getMarriageLengthForType(DateUtils.DISTANCE_MINIMAL);
			if (marriageLengthForMaximum != null) {
				if (this.maxMarriageLength.getOnlyDays() < marriageLengthForMaximum.getOnlyDays()) {
					this.maxMarriageLength.set(marriageLengthForMaximum);
				}
			}

			MyDateInterval marriageLengthForMinimum = partnership.getMarriageLengthForType(DateUtils.DISTANCE_MAXIMAL);
			if (marriageLengthForMinimum != null) {
				if (this.minMarriageLength.getOnlyDays() > marriageLengthForMinimum.getOnlyDays()) {
					this.minMarriageLength.set(marriageLengthForMinimum);
				}
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("Computing stats for partnership done.");
		}
	}

	/**
	 * Goes for each person and computes data useful for statistics
	 */
	private void computeStatisticsForPeople() {
		Tree tree = Tree.getInstance();

		this.generationCount = tree.getMaxHierarchy() - tree.getMinHierarchy() + 1;

		if (log.isDebugEnabled()) {
			log.debug("Computing statistics for people.");
		}

		for (Person person : tree.getPeopleMap().values()) {
			if (person.isHidden()) {
				continue;
			}

			this.countPeople++;

			if (person.isLive()) {
				this.livePeople++;
				if (person.isMale()) {
					this.liveMen++;
				} else {
					this.liveWomen++;
				}
			}

			if (person.isMale()) {
				this.countMen++;

				checkPersonStatisticsData(person, this.maxLiveManAge, this.maxDeadManAge, this.minLiveManAge, this.minDeadManAge,
						this.averageManAge, this.averageManMarriageAge, this.averageManFirstChildAge, this.maxManAgeOnWedding, this.minManAgeOnWedding,
						this.maxManAgeOnChild, this.minManAgeOnChild, this.maxManAgeOnFirstChild, this.maxManAgeOnGrandchild,
						this.minManAgeOnGrandchild, this.maxManAgeOnFirstGrandchild, this.maxManAgeOnGrandGrandchild, this.minManAgeOnGrandGrandchild,
						this.maxManAgeOnFirstGrandGrandchild);
				this.frequentManNameData.addName(person.getFirstname());
			}

			if (person.isFemale()) {
				this.countWomen++;

				checkPersonStatisticsData(person, this.maxLiveWomanAge, this.maxDeadWomanAge, this.minLiveWomanAge, this.minDeadWomanAge,
						this.averageWomanAge, this.averageWomanAgeOnWedding, this.averageWomanAgeOnFirstChild, this.maxWomanAgeOnWedding,
						this.minWomanAgeOnWedding, this.maxWomanAgeOnChild, this.minWomanAgeOnChild, this.maxWomanAgeOnFirstChild,
						this.maxWomanAgeOnGrandchild, this.minWomanAgeOnGrandchild, this.maxWomanAgeOnFirstGrandchild,
						this.maxWomanAgeOnGrandGrandchild, this.minWomanAgeOnGrandGrandchild, this.maxWomanAgeOnFirstGrandGrandchild);
				this.frequentWomanNameData.addName(person.getFirstname());
			}

			checkPersonStatisticsData(person, this.maxLiveAge, this.maxDeadAge, this.minLiveAge, this.minDeadAge, this.averageAge,
					this.averageMarriageAge, this.averageFirstChildAge, null, null, null, null, null, null, null, null, null, null, null);

			checkMaxChildrenCount(person);

			checkFrequendData(person.getBirthDateObj(), this.frequentBirthMonthData, this.frequentBirthDayData);
			checkFrequendData(person.getDeathDateObj(), this.frequentDeathMonthData, this.frequentDeathDayData);

			if (log.isDebugEnabled()) {
				log.debug("Statistics for people computed successfully.");
			}
		}
	}

	/**
	 * @return Average age
	 */
	public MyDateInterval getAverageAge() {
		return this.averageAge;
	}

	/**
	 * @return Average age on first child
	 */
	public MyDateInterval getAverageAgeOnFirstChild() {
		return this.averageFirstChildAge;
	}

	/**
	 * @return Average age on wedding
	 */
	public MyDateInterval getAverageAgeOnWedding() {
		return this.averageMarriageAge;
	}

	/**
	 * @return Average age of men
	 */
	public MyDateInterval getAverageManAge() {
		return this.averageManAge;
	}

	/**
	 * @return Average age of man on first child
	 */
	public MyDateInterval getAverageManAgeOnFirstChild() {
		return this.averageManFirstChildAge;
	}

	/**
	 * @return Average age of men on wedding
	 */
	public MyDateInterval getAverageManAgeOnWedding() {
		return this.averageManMarriageAge;
	}

	/**
	 * @return Average age of women
	 */
	public MyDateInterval getAverageWomanAge() {
		return this.averageWomanAge;
	}

	/**
	 * @return Average age of women on first child
	 */
	public MyDateInterval getAverageWomanAgeOnFirstChild() {
		return this.averageWomanAgeOnFirstChild;
	}

	/**
	 * @return Average age of women on wedding
	 */
	public MyDateInterval getAverageWomanAgeOnWedding() {
		return this.averageWomanAgeOnWedding;
	}

	/**
	 * @return FrequentData for birth day (day of week)
	 */
	public FrequentData<String> getFrequentBirthDay() {
		if (!this.sortedBirthDays) {
			sortDayOfWeeks(this.frequentBirthDayData.getTheLeast());
			sortDayOfWeeks(this.frequentBirthDayData.getTheMost());
			this.sortedBirthDays = true;
		}
		return this.frequentBirthDayData;
	}

	/**
	 * @return FrequentData for birth month
	 */
	public FrequentData<String> getFrequentBirthMonth() {
		return this.frequentBirthMonthData;
	}

	/**
	 * @return Frequent data for death day (day of week)
	 */
	public FrequentData<String> getFrequentDeathDay() {
		if (!this.sortedDeathDays) {
			sortDayOfWeeks(this.frequentDeathDayData.getTheLeast());
			sortDayOfWeeks(this.frequentDeathDayData.getTheMost());
			this.sortedDeathDays = true;
		}
		return this.frequentDeathDayData;
	}

	/**
	 * @return Frequent data for death month
	 */
	public FrequentData<String> getFrequentDeathMonth() {
		return this.frequentDeathMonthData;
	}

	/**
	 * @return Frequent data for names of men
	 */
	public FrequentData<String> getFrequentManNames() {
		return this.frequentManNameData;
	}

	/**
	 * @return Frequent data for wedding day (day of week)
	 */
	public FrequentData<String> getFrequentWeddingDay() {
		if (!this.sortedWeddingDays) {
			sortDayOfWeeks(this.frequentWeddingDayData.getTheLeast());
			sortDayOfWeeks(this.frequentWeddingDayData.getTheMost());
			this.sortedWeddingDays = true;
		}
		return this.frequentWeddingDayData;
	}

	/**
	 * @return Frequent data for wedding month
	 */
	public FrequentData<String> getFrequentWeddingMonth() {
		return this.frequentWeddingMonthData;
	}

	/**
	 * @return Frequent data for names of women
	 */
	public FrequentData<String> getFrequentWomanNames() {
		return this.frequentWomanNameData;
	}

	/**
	 * @return the generation
	 */
	public int getGeneration() {
		return this.generationCount;
	}

	/**
	 * @return the liveMen
	 */
	public int getLiveMen() {
		return this.liveMen;
	}

	/**
	 * @return the livePeople
	 */
	public int getLivePeople() {
		return this.livePeople;
	}

	/**
	 * @return the liveWoman
	 */
	public int getLiveWomen() {
		return this.liveWomen;
	}

	/**
	 * @return Maximum count of children
	 */
	public int getMaxCountChildren() {
		return this.maxCountChildren;
	}

	/**
	 * @return People that had maximum count of children
	 */
	public List<Person> getMaxCountChildrenPeople() {
		return this.maxCountChildrenPeople;
	}

	/**
	 * @return the maxDeadAge
	 */
	public MyDateInterval getMaxDeadAge() {
		return this.maxDeadAge;
	}

	/**
	 * @return the maxDeadManAge
	 */
	public MyDateInterval getMaxDeadManAge() {
		return this.maxDeadManAge;
	}

	/**
	 * @return the maxDeadWomanAge
	 */
	public MyDateInterval getMaxDeadWomanAge() {
		return this.maxDeadWomanAge;
	}

	/**
	 * @return the maxLiveAge
	 */
	public MyDateInterval getMaxLiveAge() {
		return this.maxLiveAge;
	}

	/**
	 * @return the maxLiveManAge
	 */
	public MyDateInterval getMaxLiveManAge() {
		return this.maxLiveManAge;
	}

	/**
	 * @return the maxLiveWomanAge
	 */
	public MyDateInterval getMaxLiveWomanAge() {
		return this.maxLiveWomanAge;
	}

	/**
	 * @return the maxManAgeOnFirstGrandchild
	 */
	public MyDateInterval getMaxManAgeOnFirstGrandChild() {
		return this.maxManAgeOnFirstGrandchild;
	}

	/**
	 * @return the maxManAgeOnFirstGrandGrandchild
	 */
	public MyDateInterval getMaxManAgeOnFirstGreatGrandChild() {
		return this.maxManAgeOnFirstGrandGrandchild;
	}

	/**
	 * @return the maxManAgeOnFirstChild
	 */
	public MyDateInterval getMaxManAgeOnFirstChild() {
		return this.maxManAgeOnFirstChild;
	}

	/**
	 * @return the maxManAgeOnGrandchild
	 */
	public MyDateInterval getMaxManAgeOnGrandChild() {
		return this.maxManAgeOnGrandchild;
	}

	/**
	 * @return the maxManAgeOnGrandGrandchild
	 */
	public MyDateInterval getMaxManAgeOnGreatGrandChild() {
		return this.maxManAgeOnGrandGrandchild;
	}

	/**
	 * @return the maxFirstChildManAge
	 */
	public MyDateInterval getMaxManAgeOnChild() {
		return this.maxManAgeOnChild;
	}

	/**
	 * @return the maxWeddingManAge
	 */
	public MyDateInterval getMaxManAgeOnWedding() {
		return this.maxManAgeOnWedding;
	}

	/**
	 * @return the maxMarriageLength
	 */
	public MyDateInterval getMaxMarriageLength() {
		return this.maxMarriageLength;
	}

	/**
	 * @return the maxWomanAgeOnFirstGrandchild
	 */
	public MyDateInterval getMaxWomanAgeOnFirstGrandChild() {
		return this.maxWomanAgeOnFirstGrandchild;
	}

	/**
	 * @return the maxWomanAgeOnFirstGrandGrandchild
	 */
	public MyDateInterval getMaxWomanAgeOnFirstGreatGrandChild() {
		return this.maxWomanAgeOnFirstGrandGrandchild;
	}

	/**
	 * @return the maxWomanAgeOnFirstChild
	 */
	public MyDateInterval getMaxWomanAgeOnFirstChild() {
		return this.maxWomanAgeOnFirstChild;
	}

	/**
	 * @return the maxWomanAgeOnGrandchild
	 */
	public MyDateInterval getMaxWomanAgeOnGrandChild() {
		return this.maxWomanAgeOnGrandchild;
	}

	/**
	 * @return the maxWomanAgeOnGrandGrandchild
	 */
	public MyDateInterval getMaxWomanAgeOnGreatGrandChild() {
		return this.maxWomanAgeOnGrandGrandchild;
	}

	/**
	 * @return the maxFirstChildWomanAge
	 */
	public MyDateInterval getMaxWomanAgeOnChild() {
		return this.maxWomanAgeOnChild;
	}

	/**
	 * @return the maxWeddingWomanAge
	 */
	public MyDateInterval getMaxWomanAgeOnWedding() {
		return this.maxWomanAgeOnWedding;
	}

	/**
	 * @return the men
	 */
	public int getMen() {
		return this.countMen;
	}

	/**
	 * @return the minDeadAge
	 */
	public MyDateInterval getMinDeadAge() {
		return this.minDeadAge;
	}

	/**
	 * @return the minDeadManAge
	 */
	public MyDateInterval getMinDeadManAge() {
		return this.minDeadManAge;
	}

	/**
	 * @return the minDeadWomanAge
	 */
	public MyDateInterval getMinDeadWomanAge() {
		return this.minDeadWomanAge;
	}

	/**
	 * @return the minLiveAge
	 */
	public MyDateInterval getMinLiveAge() {
		return this.minLiveAge;
	}

	/**
	 * @return the minLiveManAge
	 */
	public MyDateInterval getMinLiveManAge() {
		return this.minLiveManAge;
	}

	/**
	 * @return the minLiveWomanAge
	 */
	public MyDateInterval getMinLiveWomanAge() {
		return this.minLiveWomanAge;
	}

	/**
	 * @return the minManAgeOnGrandchild
	 */
	public MyDateInterval getMinManAgeOnGrandChild() {
		return this.minManAgeOnGrandchild;
	}

	/**
	 * @return the minManAgeOnGrandGrandchild
	 */
	public MyDateInterval getMinManAgeOnGreatGrandChild() {
		return this.minManAgeOnGrandGrandchild;
	}

	/**
	 * @return the minFirstChildManAge
	 */
	public MyDateInterval getMinManAgeOnChild() {
		return this.minManAgeOnChild;
	}

	/**
	 * @return the minWeddingManAge
	 */
	public MyDateInterval getMinManAgeOnWedding() {
		return this.minManAgeOnWedding;
	}

	/**
	 * @return the minMarriageLength
	 */
	public MyDateInterval getMinMarriageLength() {
		return this.minMarriageLength;
	}

	/**
	 * @return the minWomanAgeOnGrandchild
	 */
	public MyDateInterval getMinWomanAgeOnGrandChild() {
		return this.minWomanAgeOnGrandchild;
	}

	/**
	 * @return the minWomanAgeOnGrandGrandchild
	 */
	public MyDateInterval getMinWomanAgeOnGreatGrandChild() {
		return this.minWomanAgeOnGrandGrandchild;
	}

	/**
	 * @return the minFirstChildWomanAge
	 */
	public MyDateInterval getMinWomanAgeOnChild() {
		return this.minWomanAgeOnChild;
	}

	/**
	 * @return the minWeddingWomanAge
	 */
	public MyDateInterval getMinWomanAgeOnWedding() {
		return this.minWomanAgeOnWedding;
	}

	/**
	 * @return the people
	 */
	public int getPeople() {
		return this.countPeople;
	}

	/**
	 * @return the woman
	 */
	public int getWomen() {
		return this.countWomen;
	}

	/**
	 * Checks frequent data of given date.
	 * 
	 * @param date Date to check
	 * @param frequentMonthData FrequentData for month
	 * @param frequentDayData FrequentData for day (day of week)
	 */
	private void checkFrequendData(MyCalendar date, FrequentData<String> frequentMonthData, FrequentData<String> frequentDayData) {
		if (!DateUtils.isEmpty(date) && date.isSet(Calendar.MONTH)) {
			frequentMonthData.addName(Integer.toString(date.get(Calendar.MONTH) + 1));
		}
		if (!DateUtils.isEmpty(date) && date.isSet(Calendar.DAY_OF_WEEK) && date.isSet(Calendar.DAY_OF_MONTH) && date.isSet(Calendar.MONTH)
				&& date.isSet(Calendar.YEAR)) {
			frequentDayData.addName(Integer.toString(date.get(Calendar.DAY_OF_WEEK)));
		}
	}

	/**
	 * Checks max count of children of given person
	 * 
	 * @param person Person to check
	 */
	private void checkMaxChildrenCount(Person person) {
		int childCount = person.getChildrenIds().size();
		if (childCount >= this.maxCountChildren) {
			if (childCount > this.maxCountChildren) {
				this.maxCountChildrenPeople.clear();
				this.maxCountChildren = childCount;
			}
			this.maxCountChildrenPeople.add(person);
		}
	}

	/**
	 * Checks if personIntervalForMaximum is greater than maxInterval. If yes then updates maxInterval.
	 * 
	 * @param maxInterval Currently the largest interval for some property (e.g. liveLength)
	 * @param personIntervalForMaximum Personal interval for some property (e.g. liveLength)
	 */
	private void checkMaximum(MyDateInterval maxInterval, MyDateInterval personIntervalForMaximum) {
		if (personIntervalForMaximum != null && maxInterval != null) {
			if (maxInterval.getOnlyDays() < personIntervalForMaximum.getOnlyDays()) {
				maxInterval.set(personIntervalForMaximum);
			}
		}
	}

	/**
	 * Checks if personIntervalForMinimum is smaller than minInterval. If yes then updates minInterval.
	 * 
	 * @param minInterval Currently the smallest interval for some property (e.g. liveLength)
	 * @param personIntervalForMinimum Personal interval for some property (e.g. liveLength)
	 */
	private void checkMinimum(MyDateInterval minInterval, MyDateInterval personIntervalForMinimum) {
		if (personIntervalForMinimum != null && minInterval != null) {
			if (minInterval.getOnlyDays() > personIntervalForMinimum.getOnlyDays()) {
				minInterval.set(personIntervalForMinimum);
			}
		}
	}

	/**
	 * Checks dates of given person and updates max/min/average intervals if needed.
	 * 
	 * @param person Person that age can be checked
	 * @param maxAgeFromLive the max age from live people
	 * @param maxAgeFromDead the max age from dead people
	 * @param minAgeFromLive the min age from live people
	 * @param minAgeFromDead the min age from dead people
	 * @param averageAgeParam the average age of people
	 * @param averageAgeOnWedding the average age on wedding
	 * @param averageAgeOnFirstChild the average age on first child
	 * @param maxAgeOnWedding the max age on wedding
	 * @param minAgeOnWedding the min age on wedding
	 * @param maxAgeOnChild the max age on child
	 * @param minAgeOnChild the min age on child
	 * @param maxAgeOnFirstChild the max age on first child
	 * @param maxAgeOnGrandchild the max age on grandchild
	 * @param minAgeOnGrandchild the min age on grandchild
	 * @param maxAgeOnFirstGrandchild the max age on first grandchild
	 * @param maxAgeOnGrandGrandchild the max age on grand grandchild
	 * @param minAgeOnGrandGrandchild the min age on grand grandchild
	 * @param maxAgeOnFirstGrandGrandchild the max age on first grand grandchild
	 */
	private void checkPersonStatisticsData(Person person, MyDateInterval maxAgeFromLive, MyDateInterval maxAgeFromDead,
			MyDateInterval minAgeFromLive, MyDateInterval minAgeFromDead, MyDateInterval averageAgeParam, MyDateInterval averageAgeOnWedding,
			MyDateInterval averageAgeOnFirstChild, MyDateInterval maxAgeOnWedding, MyDateInterval minAgeOnWedding, MyDateInterval maxAgeOnChild,
			MyDateInterval minAgeOnChild, MyDateInterval maxAgeOnFirstChild, MyDateInterval maxAgeOnGrandchild,
			MyDateInterval minAgeOnGrandchild, MyDateInterval maxAgeOnFirstGrandchild, MyDateInterval maxAgeOnGrandGrandchild,
			MyDateInterval minAgeOnGrandGrandchild, MyDateInterval maxAgeOnFirstGrandGrandchild) {

		if (log.isDebugEnabled()) {
			log.debug("Check person statistics data for person id=" + person.getId());
		}

		MyDateInterval personLiveLengthForMaximum = person.getLiveInterval("personLiveLengthForMaximum", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.LIVE_LENGTH);
		if (personLiveLengthForMaximum != null) {
			if (person.isLive() && (maxAgeFromLive.getOnlyDays() < personLiveLengthForMaximum.getOnlyDays())) {
				maxAgeFromLive.set(personLiveLengthForMaximum);
			}
			if (person.isDead() && (maxAgeFromDead.getOnlyDays() < personLiveLengthForMaximum.getOnlyDays())) {
				maxAgeFromDead.set(personLiveLengthForMaximum);
			}
		}

		MyDateInterval personLiveLengthForMinimum = person.getLiveInterval("personLiveLengthForMinimum", DateUtils.DISTANCE_MAXIMAL,
				PersonIntervalTypeEnum.LIVE_LENGTH);
		if (personLiveLengthForMinimum != null) {
			if (person.isLive() && (minAgeFromLive.getOnlyDays() > personLiveLengthForMinimum.getOnlyDays())) {
				minAgeFromLive.set(personLiveLengthForMinimum);
			}
			if (person.isDead() && (minAgeFromDead.getOnlyDays() > personLiveLengthForMinimum.getOnlyDays())) {
				minAgeFromDead.set(personLiveLengthForMinimum);
			}
		}

		checkMaximum(maxAgeOnWedding, person.getLiveInterval("maxAgeOnWedding", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.ALL_WEDDING_DAYS));
		checkMinimum(minAgeOnWedding, person.getLiveInterval("minAgeOnWedding", DateUtils.DISTANCE_MAXIMAL,
				PersonIntervalTypeEnum.ALL_WEDDING_DAYS));

		checkMaximum(maxAgeOnChild, person.getLiveInterval("maxAgeOnChild", DateUtils.DISTANCE_MINIMAL, PersonIntervalTypeEnum.ALL_CHILD_DAYS));
		checkMinimum(minAgeOnChild, person.getLiveInterval("minAgeOnChild", DateUtils.DISTANCE_MAXIMAL, PersonIntervalTypeEnum.ALL_CHILD_DAYS));

		checkMaximum(maxAgeOnGrandchild, person.getLiveInterval("maxAgeOnGrandchild", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.ALL_GRANDCHILD_DAYS));
		checkMinimum(minAgeOnGrandchild, person.getLiveInterval("minAgeOnGrandchild", DateUtils.DISTANCE_MAXIMAL,
				PersonIntervalTypeEnum.ALL_GRANDCHILD_DAYS));

		checkMaximum(maxAgeOnGrandGrandchild, person.getLiveInterval("maxAgeOnGrandGrandchild", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.ALL_GREATGRANDCHILD_DAYS));
		checkMinimum(minAgeOnGrandGrandchild, person.getLiveInterval("minAgeOnGrandGrandchild", DateUtils.DISTANCE_MAXIMAL,
				PersonIntervalTypeEnum.ALL_GREATGRANDCHILD_DAYS));

		checkMaximum(maxAgeOnFirstChild, person.getLiveInterval("maxAgeOnFirstChild", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.FIRST_CHILD_DAY));
		checkMaximum(maxAgeOnFirstGrandchild, person.getLiveInterval("maxAgeOnFirstGrandchild", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.FIRST_GRANDCHILD_DAY));
		checkMaximum(maxAgeOnFirstGrandGrandchild, person.getLiveInterval("maxAgeOnFirstGrandGrandchild", DateUtils.DISTANCE_MINIMAL,
				PersonIntervalTypeEnum.FIRST_GREATGRANDCHILD_DAY));

		if (person.isDead()) {
			averageAgeParam.add(person.getLiveLength());
		}

		averageAgeOnWedding.add(person.getLiveInterval("getAgeOnFirstWedding", DateUtils.DISTANCE_AVERAGE,
				PersonIntervalTypeEnum.FIRST_WEDDING_DAY));
		averageAgeOnFirstChild.add(person.getLiveInterval("getAgeOnFirstChild", DateUtils.DISTANCE_AVERAGE,
				PersonIntervalTypeEnum.FIRST_CHILD_DAY));
	}

	/**
	 * Initializes all months and days to 0 occurences
	 */
	private void initializesFrequentMaps() {
		if (log.isDebugEnabled()) {
			log.debug("Initializing frequent Maps");
		}

		for (int i = 1; i <= 7; i++) {
			this.frequentBirthDayData.addName(Integer.toString(i), 0);
			this.frequentDeathDayData.addName(Integer.toString(i), 0);
			this.frequentWeddingDayData.addName(Integer.toString(i), 0);
		}
		for (int i = 1; i <= 12; i++) {
			this.frequentBirthMonthData.addName(Integer.toString(i), 0);
			this.frequentDeathMonthData.addName(Integer.toString(i), 0);
			this.frequentWeddingMonthData.addName(Integer.toString(i), 0);
		}

		if (log.isDebugEnabled()) {
			log.debug("Frequent Maps initialized");
		}
	}

	/**
	 * Normalizes each maximum or minimum intervals
	 */
	private void normalizeAllMaxMinItervals() {
		if (log.isDebugEnabled()) {
			log.debug("Normalizing max-min intervals");
		}

		this.maxDeadManAge.normalize(1);
		this.minDeadManAge.normalize(-1);
		this.maxDeadWomanAge.normalize(1);
		this.minDeadWomanAge.normalize(-1);
		this.maxDeadAge.normalize(1);
		this.minDeadAge.normalize(-1);

		this.maxLiveManAge.normalize(1);
		this.minLiveManAge.normalize(-1);
		this.maxLiveWomanAge.normalize(1);
		this.minLiveWomanAge.normalize(-1);
		this.maxLiveAge.normalize(1);
		this.minLiveAge.normalize(-1);

		this.maxManAgeOnWedding.normalize(1);
		this.minManAgeOnWedding.normalize(-1);
		this.maxWomanAgeOnWedding.normalize(1);
		this.minWomanAgeOnWedding.normalize(-1);

		this.maxManAgeOnChild.normalize(1);
		this.minManAgeOnChild.normalize(-1);
		this.maxWomanAgeOnChild.normalize(1);
		this.minWomanAgeOnChild.normalize(-1);
		this.maxWomanAgeOnFirstChild.normalize(1);
		this.maxManAgeOnFirstChild.normalize(1);

		this.maxManAgeOnGrandchild.normalize(1);
		this.minManAgeOnGrandchild.normalize(-1);
		this.maxWomanAgeOnGrandchild.normalize(1);
		this.minWomanAgeOnGrandchild.normalize(-1);
		this.maxWomanAgeOnFirstGrandchild.normalize(1);
		this.maxManAgeOnFirstGrandchild.normalize(1);

		if (log.isDebugEnabled()) {
			log.debug("Normalizing max-min interval done.");
		}
	}

	/**
	 * Reset sorted flags of days, because it must be sorted for each locale
	 */
	public void reset() {
		this.sortedBirthDays = false;
		this.sortedDeathDays = false;
		this.sortedWeddingDays = false;
	}

	/**
	 * Sorts days of week in list (e.g. from Sunday to Saturday in US locale).
	 * 
	 * @param days List of days to sort
	 */
	private void sortDayOfWeeks(List<String> days) {
		int firstDay = 2;
		// TODO when sk_SK will return 2 (probably JDK7) then uncomment:
		// Calendar.getInstance(Config.getInstance().getLocale()).getFirstDayOfWeek();
		List<Integer> daysInt = new ArrayList<Integer>();
		for (String day : days) {
			daysInt.add((Integer.parseInt(day) - firstDay + 7) % 7);
		}
		Collections.sort(daysInt);
		days.clear();
		for (Integer day : daysInt) {
			days.add(Integer.toString((day + firstDay + 6) % 7 + 1));
		}
	}

	/**
	 * Sorts months of each month frequentData
	 */
	private void sortFrequentMonthData() {
		if (log.isDebugEnabled()) {
			log.debug("Sorting months data");
		}

		Collections.sort(this.frequentDeathMonthData.getTheMost());
		Collections.sort(this.frequentDeathMonthData.getTheLeast());
		Collections.sort(this.frequentBirthMonthData.getTheLeast());
		Collections.sort(this.frequentBirthMonthData.getTheMost());
		Collections.sort(this.frequentWeddingMonthData.getTheMost());
		Collections.sort(this.frequentWeddingMonthData.getTheLeast());

		if (log.isDebugEnabled()) {
			log.debug("Sorting months data done");
		}
	}
}
