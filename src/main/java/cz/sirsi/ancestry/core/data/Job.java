// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object for storing data of job of the person
 * 
 * @author msiroky
 */
public class Job {
	private MyCalendar from;
	private String position;
	private MyCalendar to;

	/**
	 * Constructs new object Job
	 * 
	 * @param position Position in job
	 * @param from From when
	 * @param to To when
	 */
	public Job(String position, String from, String to) {
		super();
		this.position = position;
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

		String fromTo = fromDate + (fromDate.length() != 0 && toDate.length() != 0 ? Config.getInstance().getFromToSplitter() : "") + toDate;

		return fromTo.trim().length() > 0 ? fromTo : null;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return this.position;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to != null ? this.to.format() : null;
	}
}
