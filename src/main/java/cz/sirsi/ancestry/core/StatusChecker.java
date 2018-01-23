/**
 * 
 */
package cz.sirsi.ancestry.core;

/**
 * @author msiroky
 */
public interface StatusChecker {
	/**
	 * Sets status of progressbar for given position id
	 * 
	 * @param id Position id - unique string which specifies position in application
	 */
	public void setProcessStatus(String id);

	/**
	 * Prints message
	 * 
	 * @param text Text of message
	 */
	public void printOutputMessage(String text);

	/**
	 * Prints error
	 * 
	 * @param text Text of error message
	 */
	public void error(String text);
}
