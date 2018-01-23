package cz.sirsi.ancestry.core.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cz.sirsi.ancestry.core.data.MyDateInterval;

/**
 * @author msiroky
 */
public class DateUtils {
	/**
	 * DOCUMENT ME!
	 */
	public static final int DISTANCE_AVERAGE = 0;
	/**
	 * DOCUMENT ME!
	 */
	public static final int DISTANCE_MAXIMAL = 1;
	/**
	 * DOCUMENT ME!
	 */
	public static final int DISTANCE_MINIMAL = -1;
	/**
	 * DOCUMENT ME!
	 */
	public static final int PRECISION_DAY = 1;
	/**
	 * DOCUMENT ME!
	 */
	public static final int PRECISION_MONTH = 2;
	/**
	 * DOCUMENT ME!
	 */
	public static final int PRECISION_YEAR = 3;

	// TODO input locale independent
	private static SimpleDateFormat sdfDay = new SimpleDateFormat("dd.MM.yyyy");
	private static SimpleDateFormat sdfMonh = new SimpleDateFormat("MM.yyyy");
	private static SimpleDateFormat sdfMonh2 = new SimpleDateFormat("'?.'MM.yyyy");
	private static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");

	/**
	 * Compares two dates
	 * 
	 * @param date1 First date to compare
	 * @param date2 Second date to compare
	 * @return DOCUMENT ME!
	 */
	public static int compareTwoDates(MyCalendar date1, MyCalendar date2) {
		if (isEmpty(date1) && isEmpty(date2)) {
			return 0;
		}
		if (isEmpty(date1)) {
			return 1;
		}
		if (isEmpty(date2)) {
			return -1;
		}

		MyCalendar dateModificated1 = date2.clone();
		MyCalendar dateModificated2 = date1.clone();

		dateModificated1.computePrecisionAndFillMissingValues(0);
		dateModificated2.computePrecisionAndFillMissingValues(0);

		int result = dateModificated1.before(dateModificated2) ? 1 : dateModificated1.after(dateModificated2) ? -1 : 0;

		return result;
	}

	/**
	 * Returns distance between two dates in days, months and years
	 * 
	 * @param date1 First date
	 * @param date2 Second date
	 * @param type What to do with dates before computing distance if some values are not specified (what distance we can
	 *          count):
	 *          <ul>
	 *          <li>AVERAGE_DISTANCE - use average value (e.g. day is not set, then is used half of the month - e.g.
	 *          12.1992 - 6.2000 converts to 15.12.1992 - 15.6.2000)</li>
	 *          <li>MAXIMAL_DISTANCE - use minimal value for first date and maximal value for second date (e.g. 12.1992 -
	 *          6.2000 converts to 1.12.1992 - 30.6.2000)</li>
	 *          <li>MINIMAL_DISTANCE - use maximal value for first date and minimal value for second date (e.g. 12.1992 -
	 *          6.2000 converts to 31.12.1992 - 1.6.2000)</li>
	 *          </ul>
	 * @param id Id of object (person or partnership) that belongs to the interval (e.g. interval can be live length of
	 *          some person)
	 * @return Distance between 2 dates
	 */
	public static MyDateInterval distance(MyCalendar date1, MyCalendar date2, int type, int id) {
		if (DateUtils.isEmpty(date1) || DateUtils.isEmpty(date2)) {
			return null;
		}

		MyCalendar dateModificated1;
		MyCalendar dateModificated2;

		if (date1.after(date2)) {
			dateModificated1 = date2.clone();
			dateModificated2 = date1.clone();
		} else {
			dateModificated1 = date1.clone();
			dateModificated2 = date2.clone();
		}

		// fill missing values in first date and computes its precision
		int precision1 = dateModificated1.computePrecisionAndFillMissingValues(type);
		// fill missing values in second date and computes its precision (type must be opposite for end of interval)
		int precision2 = dateModificated2.computePrecisionAndFillMissingValues(-type);

		int precision = precision1 > precision2 ? precision1 : precision2;

		long onlyDays = Math.round((dateModificated2.getTimeInMillis() - dateModificated1.getTimeInMillis()) / 1000 / 60
				/ 60 / 24D);

		int years = dateModificated2.get(Calendar.YEAR) - dateModificated1.get(Calendar.YEAR)
				- (precision == PRECISION_YEAR ? type : 0);
		// TODO
		int months = dateModificated2.get(Calendar.MONTH) - dateModificated1.get(Calendar.MONTH)
				- (precision == PRECISION_MONTH ? type : 0);
		int days = dateModificated2.get(Calendar.DAY_OF_MONTH) - dateModificated1.get(Calendar.DAY_OF_MONTH)
				- (precision == PRECISION_DAY ? /* type : only when we can count only 100% whole days */0 : 0);

		if (precision < PRECISION_MONTH && days < 0) {
			Calendar cal = dateModificated2.getCalendar();
			cal.add(Calendar.MONTH, -1);
			days += cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			months -= 1;
		}

		if (precision < PRECISION_YEAR && months < 0) {
			months += 12;
			years -= 1;
		}

		return new MyDateInterval(onlyDays, years, (precision < PRECISION_YEAR ? months : null),
				(precision < PRECISION_MONTH ? days : null), id);
	}

	/**
	 * @param date Date to check
	 * @return True if date is null or is represented only by text
	 */
	public static boolean isEmpty(MyCalendar date) {
		return date == null || date.hasOnlyText();
	}

	/**
	 * Parses string that represents date to object Calendar
	 * 
	 * @param dateStringParam String to parse
	 * @return Parsed date or null in case parsing failed
	 */
	public static MyCalendar parse(String dateStringParam) {
		if (dateStringParam == null) {
			return null;
		}

		String dateString = dateStringParam.trim();

		MyCalendar calendar = new MyCalendar();
		if (dateString.matches("^\\d+\\.\\d+\\.\\d\\d\\d\\d$")) {
			try {
				calendar.setTime(sdfDay.parse(dateString));
				return calendar;
			} catch (ParseException e) {
				// nothing
			}
		}

		if (dateString.matches("^\\d+\\.\\d\\d\\d\\d$")) {
			try {
				calendar.setTime(sdfMonh.parse(dateString));
				calendar.clear(Calendar.DAY_OF_MONTH);
				return calendar;
			} catch (ParseException e) {
				// nothing
			}
		}

		if (dateString.matches("^\\?+\\.\\d+\\.\\d\\d\\d\\d$")) {
			try {
				calendar.setTime(sdfMonh2.parse(dateString));
				calendar.clear(Calendar.DAY_OF_MONTH);
				return calendar;
			} catch (ParseException e) {
				// nothing
			}
		}

		if (dateString.matches("^\\d\\d\\d\\d$")) {
			try {
				calendar.setTime(sdfYear.parse(dateString));
				calendar.clear(Calendar.DAY_OF_MONTH);
				calendar.clear(Calendar.MONTH);
				return calendar;
			} catch (ParseException e) {
				// nothing
			}
		}

		calendar.setTextRepresentationIfNotParseble(dateString);

		return calendar;
	}

}
