// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object that stores data of education
 * 
 * @author msiroky
 */
public class Education {
	private MyCalendar from;
	private String name;
	private MyCalendar to;

	/**
	 * Constructs new object Education
	 * 
	 * @param name Name of education (e.g. "High school of W.Churchil, London")
	 * @param from Since when person was studying
	 * @param to To when person was studying
	 */
	public Education(String name, String from, String to) {
		this.name = name;
		this.from = DateUtils.parse(from);
		this.to = DateUtils.parse(to);
	}

	/**
	 * @return the from
	 */
	public String getFrom() {
		return this.from != null ? this.from.format() : null;
	}

	/**
	 * @return formated interval from to
	 */
	// TODO move to tag
	public String getFromTo() {
		String fromDate = getFrom() != null ? getFrom() : "";
		String toDate = getTo() != null ? getTo() : "";

		String fromTo = fromDate
				+ (fromDate.length() != 0 && toDate.length() != 0 ? Config.getInstance().getFromToSplitter() : "") + toDate;

		return fromTo.trim().length() > 0 ? fromTo : null;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to != null ? this.to.format() : null;
	}
}
