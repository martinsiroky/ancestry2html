package cz.sirsi.ancestry.core.exceptions;

/**
 * Common exception used in application
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class AppException extends Exception {

	/**
	 * @param message Detail message of exception
	 */
	public AppException(String message) {
		super(message);
	}

	/**
	 * @param message Detail message of exception
	 * @param cause Cause of exception
	 */
	public AppException(String message, Throwable cause) {
		super(message, cause);
	}
}
