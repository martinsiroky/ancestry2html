package cz.sirsi.ancestry.core.tools;

import cz.sirsi.ancestry.core.configuration.MessagesTools;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Small composition of GregorianCalendar that allows work with not exact dates (like e.g. ?.1.1990) - does not provide all methods as
 * Calendar, only needed for this project
 * 
 * @author msiroky
 */
public class MyCalendar {
	private Calendar calendar;

	private boolean day;
	private boolean month;
	private String textRepresentationIfNotParseble;

	private boolean year;

	/**
	 * Creates new instance with date set to current date
	 */
	public MyCalendar() {
		this.calendar = new GregorianCalendar();
		this.year = true;
		this.month = true;
		this.day = true;
	}

	/**
	 * Creates new instance for given year, month and day. Any filed can be null in case it is not known
	 * 
	 * @param year Year or null in case year is not known
	 * @param month Month or null in case month is not known
	 * @param day Day or null in case day is not known
	 */
	public MyCalendar(Integer year, Integer month, Integer day) {
		this.year = year != null;
		this.month = month != null;
		this.day = day != null;
		this.calendar = new GregorianCalendar(this.year ? year : 2000, this.month ? month : 1, this.day ? day : 1);
	}

	/**
	 * Only recall original after method of {@link GregorianCalendar}
	 * 
	 * @param other @see {@link GregorianCalendar#before(Object)}
	 * @return @see {@link GregorianCalendar#after(Object)}
	 */
	public boolean after(MyCalendar other) {
		return this.calendar.after(other.getCalendar());
	}

	/**
	 * Only recall original before method of {@link GregorianCalendar}
	 * 
	 * @param other @see {@link GregorianCalendar#before(Object)}
	 * @return @see {@link GregorianCalendar#before(Object)}
	 */
	public boolean before(MyCalendar other) {
		return this.calendar.before(other.getCalendar());
	}

	/**
	 * Clears given field to explicitly tell thet the date is not exact
	 * 
	 * @param field @see {@link Calendar#clear()}
	 */
	public void clear(int field) {
		if (field == Calendar.YEAR) {
			this.year = false;
		}
		if (field == Calendar.MONTH) {
			this.month = false;
		}
		if (field == Calendar.DAY_OF_MONTH) {
			this.day = false;
		}
	}

	/**
	 * Creates new instance of MyCalendar with the same data as this
	 */
	@Override
	public MyCalendar clone() {
		return new MyCalendar(this.year ? this.calendar.get(Calendar.YEAR) : null, this.month ? this.calendar.get(Calendar.MONTH) : null,
				this.day ? this.calendar.get(Calendar.DAY_OF_MONTH) : null);
	}

	/**
	 * Computes precision of date (day, month or year precision)
	 * 
	 * @param type DOCUMENT ME!
	 * @return DOCUMENT ME!
	 */
	public int computePrecisionAndFillMissingValues(int type) {
		int precision;

		if (this.day) {
			precision = DateUtils.PRECISION_DAY;
		} else if (this.month) {
			if (type < 0) {
				this.set(Calendar.DAY_OF_MONTH, 1);
			}
			if (type > 0) {
				this.set(Calendar.DAY_OF_MONTH, this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
			}
			if (type == 0) {
				this.set(Calendar.DAY_OF_MONTH, this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH) / 2);
			}
			precision = DateUtils.PRECISION_MONTH;
		} else {
			if (type < 0) {
				this.set(Calendar.DAY_OF_MONTH, 1);
				this.set(Calendar.MONTH, 1);
			}
			if (type > 0) {
				this.set(Calendar.DAY_OF_MONTH, this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				this.set(Calendar.MONTH, this.calendar.getActualMaximum(Calendar.MONTH));
			}
			if (type == 0) {
				this.set(Calendar.DAY_OF_MONTH, this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
				this.set(Calendar.MONTH, this.calendar.getActualMaximum(Calendar.MONTH) / 2);
			}
			precision = DateUtils.PRECISION_YEAR;
		}
		return precision;
	}

	/**
	 * @return Calendar transformed to human readable form (by locale)
	 */
	public String format() {

		String dateFormat = MessagesTools.getWebMessages().getMessage("dateFormat", "dd.MM.yyyy", null, false);

		if (hasOnlyText()) {
			return this.textRepresentationIfNotParseble;
		}

		if (!this.day && !this.month) {
			return Integer.toString(this.get(Calendar.YEAR));
		}

		return dateFormat.replaceAll("yyyy", (this.year ? Integer.toString(this.get(Calendar.YEAR)) : "?")).replaceAll("MM",
				(this.month ? Integer.toString(this.get(Calendar.MONTH) + 1) : "?")).replaceAll("dd",
				(this.day ? Integer.toString(this.get(Calendar.DAY_OF_MONTH)) : "?"));
	}

	/**
	 * @param field Field to get its value @see Calendar#get(int)
	 * @return Value of the field
	 */
	public int get(int field) {
		return this.calendar.get(field);
	}

	/**
	 * @param field Field to get actual maximum @see Calendar#getActualMaximum(int)
	 * @return Actual maximum
	 */
	public int getActualMaximum(int field) {
		return this.calendar.getActualMaximum(field);
	}

	/**
	 * Gets inner instance of Calendar (only for usage in after, before methods)
	 * 
	 * @return Inner instance of Calendar
	 */
	protected Calendar getCalendar() {
		return this.calendar;
	}

	/**
	 * @param field Field to get maximum. @see Calendar#getMaximum(int)
	 * @return Maximum of given field
	 */
	public int getMaximum(int field) {
		return this.calendar.getMaximum(field);
	}

	/**
	 * @return the textRepresentationIfNotParseble
	 */
	public String getTextRepresentationIfNotParseble() {
		return this.textRepresentationIfNotParseble;
	}

	/**
	 * @return Time in miliseconds
	 * @see Calendar#getTimeInMillis()
	 */
	public long getTimeInMillis() {
		return this.calendar.getTimeInMillis();
	}

	/**
	 * If this date has only text representation (cannot be parsed to object MyCalendar to compute with it)
	 * 
	 * @return True if this date has only text representation, false otherwise
	 */
	public boolean hasOnlyText() {
		return this.textRepresentationIfNotParseble != null;
	}

	/**
	 * Reimplemented method. Return false always if given field was not set explicitly before calling this method!
	 * 
	 * @param field Filed @see {@link Calendar#isSet(int)}
	 * @return Value of field or null if it is not set
	 */
	public boolean isSet(int field) {
		switch (field) {
		case Calendar.YEAR:
			return this.year;
		case Calendar.MONTH:
			return this.month;
		case Calendar.DAY_OF_MONTH:
			return this.day;
		case Calendar.DAY_OF_WEEK:
			return this.day && this.month && this.year;
		}
		return false;
	}

	/**
	 * Sets value of the field (@see {@link Calendar#set(int, int)})
	 * 
	 * @param field Field to set
	 * @param value Value to set
	 */
	public void set(int field, int value) {
		switch (field) {
		case Calendar.YEAR:
			this.year = true;
			break;
		case Calendar.MONTH:
			this.month = true;
			break;
		case Calendar.DAY_OF_MONTH:
			this.day = true;
			break;
		}

		this.calendar.set(field, value);
	}

	/**
	 * @param textRepresentationIfNotParseble the textRepresentationIfNotParseble to set
	 */
	public void setTextRepresentationIfNotParseble(String textRepresentationIfNotParseble) {
		this.textRepresentationIfNotParseble = textRepresentationIfNotParseble;
	}

	/**
	 * @param date Date to set
	 * @see Calendar#setTime(Date)
	 */
	public void setTime(Date date) {
		this.calendar = new GregorianCalendar();
		this.calendar.setTime(date);
	}

	/**
	 * Gets date in format YYYY-MM-DD (in case some field was cleared then print "?" instead of)
	 */
	@Override
	public String toString() {
		return (this.year ? this.calendar.get(Calendar.YEAR) : "?") + "-" + (this.month ? this.calendar.get(Calendar.MONTH) + 1 : "?") + "-"
				+ (this.day ? this.calendar.get(Calendar.DAY_OF_MONTH) : "?");
	}
}
