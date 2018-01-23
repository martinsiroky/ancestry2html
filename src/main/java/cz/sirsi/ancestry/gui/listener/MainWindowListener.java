package cz.sirsi.ancestry.gui.listener;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import cz.sirsi.ancestry.gui.main.GuiForm;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * Listener to handle closing window by cross button (on the right top corner)
 * 
 * @author msiroky
 */
public class MainWindowListener implements WindowListener {

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowActivated(final WindowEvent e) {
		// nothing
	}

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowClosed(final WindowEvent e) {
		// nothing
	}

	/**
	 * When window is closed. Stored settings for next use (to the current output folder and to global settings folder in
	 * Documents&Settings).
	 * 
	 * @param e Event
	 */
	public void windowClosing(final WindowEvent e) {
		GuiForm.getInstance().prepareSettingsForNextUse();

		GuiTools.saveIniProperties(null);
		GuiTools.saveIniProperties(GuiTools.getLastUsedOutput());
		System.exit(0);
	}

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowDeactivated(final WindowEvent e) {
		// nothing
	}

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowDeiconified(final WindowEvent e) {
		// nothing
	}

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowIconified(final WindowEvent e) {
		// nothing
	}

	/**
	 * Do nothing
	 * 
	 * @param e Event
	 */
	public void windowOpened(final WindowEvent e) {
		// nothing
	}
}
