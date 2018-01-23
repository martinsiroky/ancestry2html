//Last revision 1.2.09
package cz.sirsi.ancestry.core;

/**
 * Listener to listen changes of generation process (e.g. to show this changes in progressbar)
 * 
 * @author msiroky
 */
public interface StatusListener {
	/**
	 * Appends message to output
	 * 
	 * @param key Key of message
	 * @param parameters Parameters of message
	 */
	public void appendOutputMessage(String key, String... parameters);

	/**
	 * Prints error
	 * 
	 * @param text Text of error message
	 */
	public void error(String text);

	/**
	 * Prints message
	 * 
	 * @param key Key of message
	 * @param parameters Parameters of message
	 */
	public void printOutputMessage(String key, String... parameters);

	/**
	 * Prints message directly (without limit for printing many message in short time which is not so readable, but some message must be shown
	 * always)
	 * 
	 * @param key Key of message
	 * @param parameters Parameters of message
	 */
	public void printOutputMessageDirectly(String key, String... parameters);

	/**
	 * Sets status of progressbar for given position id
	 * 
	 * @param id Position id - unique string which specifies position in application
	 */
	@SuppressWarnings("unchecked")
	public void setProcessStatus(Class clazz, String id);
}
