// Last revision 27.2.09
package cz.sirsi.ancestry.core.data;

import cz.sirsi.ancestry.core.configuration.Config;
import cz.sirsi.ancestry.core.tools.DateUtils;
import cz.sirsi.ancestry.core.tools.MyCalendar;

/**
 * Object that stores live place data
 * 
 * @author msiroky
 */
public class LivePlace {
	private String address;
	private MyCalendar from;
	private MyCalendar to;

	/**
	 * Construct new object Place
	 * 
	 * @param address Address
	 * @param from From when
	 * @param to To when
	 */
	public LivePlace(String address, String from, String to) {
		super();
		this.address = address;
		this.from = DateUtils.parse(from);
		this.to = DateUtils.parse(to);
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return this.address;
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

		String fromTo = fromDate + (fromDate.length() != 0 && toDate.length() != 0 ? Config.getInstance().getFromToSplitter() : "")
				+ (fromDate.length() == 0 && toDate.length() != 0 ? Config.getInstance().getToPrefix() : "") + toDate;

		return fromTo.trim().length() > 0 ? fromTo : null;
	}

	/**
	 * @return the to
	 */
	public String getTo() {
		return this.to != null ? this.to.format() : null;
	}
}
