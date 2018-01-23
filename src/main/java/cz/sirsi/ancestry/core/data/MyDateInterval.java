//Last revision 24.2.09
package cz.sirsi.ancestry.core.data;

/**
 * Object for storing interval information
 * 
 * @author msiroky
 */
public class MyDateInterval {
	/**
	 * Used for counting average value
	 */
	private int count;

	private Integer days;
	private int id;
	private Integer months;
	private boolean normalized = false;
	private long onlyDays;
	private Integer years;

	/**
	 * Creates empty instance with onlyDays value
	 * 
	 * @param onlyDays OnlyDays to set
	 */
	public MyDateInterval(long onlyDays) {
		this.onlyDays = onlyDays;
		this.years = 0;
		this.months = 0;
		this.days = 0;
		this.id = -1;
		this.count = 0;
	}

	/**
	 * Creates object for storing interval (some items can be null)
	 * 
	 * @param onlyDays Count of days that represents this interval (count of days between two dates)
	 * @param years Count of whole years between two dates
	 * @param months Count of whole months between two dates without whole years
	 * @param days Count of whole days between two dates without whole years and whole months
	 * @param id Id of person (partnership etc.) that belongs to this interval
	 */
	public MyDateInterval(long onlyDays, Integer years, Integer months, Integer days, int id) {
		this.onlyDays = onlyDays;
		this.years = years;
		this.months = months;
		this.days = days;
		this.id = id;
		this.count = 1;
	}

	/**
	 * Adds length of given interval to this interval (used for computing average interval of more intervals)
	 * 
	 * @param toAdd Interval to add
	 */
	public void add(MyDateInterval toAdd) {
		if (toAdd != null) {
			this.onlyDays += toAdd.onlyDays;
			this.normalized = false;
			this.count++;
		}
	}

	/**
	 * Compute average interval from sum of onlyDays
	 */
	public void average() {
		this.onlyDays /= this.count > 0 ? this.count : 1;
		this.years = new Double(Math.floor(this.onlyDays / 365.24)).intValue();
		this.months = new Double(Math.floor((this.onlyDays - this.years * 365.24) / 30.44)).intValue();
		this.days = new Double(Math.round(this.onlyDays - this.years * 365.24 - this.months * 30.44)).intValue();
		this.id = 0;
		this.normalized = true;
	}

	/**
	 * @return the days
	 */
	public Integer getDays() {
		return this.days;
	}

	/**
	 * @return the person
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return the month
	 */
	public Integer getMonths() {
		return this.months;
	}

	/**
	 * @return the onlyDays
	 */
	public long getOnlyDays() {
		return this.onlyDays;
	}

	/**
	 * @return the years
	 */
	public Integer getYears() {
		return this.years;
	}

	/**
	 * @return the normalized
	 */
	public boolean isNormalized() {
		return this.normalized;
	}

	/**
	 * Normalizes date (solve problem if day>maximum or month>12 etc.)
	 * 
	 * @param sign DOCUMENT ME!
	 */
	public void normalize(int sign) {
		// TODO solve modulo 12
		if (this.normalized) {
			return;
		}

		if (this.months == null) {
			this.years -= sign;
		} else if (this.days == null) {
			this.months -= sign;
		}

		if (this.months != null) {
			if (this.months > 11) {
				this.months -= 12;
				this.years += 1;
			}

			if (this.months < 0) {
				this.months += 12;
				this.years -= 1;
			}
		}

		if (this.years != null && this.years < 0) {
			this.years = 0;
		}

		this.normalized = true;
	}

	/**
	 * Sets data of other interval to this interval (this interval will be clone of interval given as parameter)
	 * 
	 * @param otherMyDateInterval Interval to set
	 */
	public void set(MyDateInterval otherMyDateInterval) {
		this.onlyDays = otherMyDateInterval.onlyDays;
		this.years = otherMyDateInterval.years;
		this.months = otherMyDateInterval.months;
		this.days = otherMyDateInterval.days;
		this.id = otherMyDateInterval.id;
		this.count = otherMyDateInterval.count;
	}

	/**
	 * @param days the days to set
	 */
	public void setDays(Integer days) {
		this.days = days;
	}

	/**
	 * @param months the months to set
	 */
	public void setMonths(Integer months) {
		this.months = months;
	}

	/**
	 * @param normalized the normalized to set
	 */
	public void setNormalized(boolean normalized) {
		this.normalized = normalized;
	}

	/**
	 * @param years the years to set
	 */
	public void setYears(Integer years) {
		this.years = years;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return this.onlyDays + "days";
	}
}
