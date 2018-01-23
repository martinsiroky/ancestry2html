package cz.sirsi.ancestry.gui;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import cz.sirsi.ancestry.core.StatusListener;
import cz.sirsi.ancestry.core.Warnings;
import cz.sirsi.ancestry.core.main.Main;
import cz.sirsi.ancestry.core.tools.Tools;
import cz.sirsi.ancestry.gui.main.GuiForm;
import cz.sirsi.ancestry.gui.main.ProcessDialog;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * Task that runs generating in new thread. It receives messages from generator and shows them in ProcessDialog window.
 * 
 * @author msiroky
 */
public class GenerationTask implements Runnable, StatusListener {

	private static Logger log = Logger.getLogger(GenerationTask.class);

	private static Map<String, Long> newProgressMap = new HashMap<String, Long>();
	private static Map<String, Long> progressMap = new HashMap<String, Long>();

	private boolean error = false;
	private JLabel labelLeft;
	private JLabel labelPercent;
	private long lastTime = 0;
	private ProcessDialog processDialog;
	private JProgressBar progress;
	private Properties properties;
	private long startTime;
	private String templateProperties;

	/**
	 * @param properties Properties with personal settings of generator
	 * @param templateProperties Properties file name (which contains settings for template)
	 * @param processDialog Dialog for showing process status
	 */
	public GenerationTask(Properties properties, String templateProperties, ProcessDialog processDialog) {
		this.properties = properties;
		this.templateProperties = templateProperties;
		this.processDialog = processDialog;
		this.labelLeft = processDialog.getLabelLeft();
		this.labelPercent = processDialog.getLabelPercent();
		this.progress = processDialog.getProgressGenerate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void appendOutputMessage(String key, String... parameters) {
		String text = this.processDialog.getTextDetails().getText();
		this.processDialog.getTextDetails().setText(text + "\n" + GuiTools.getLocalized(key, key, parameters));
	}

	/**
	 * Prints error to the text area
	 * 
	 * @param text Text of error message
	 */
	@Override
	public void error(String text) {
		this.error = true;
		this.processDialog.getTextDetails().setText(this.processDialog.getTextDetails().getText() + "\n!!!!!!!!!!!\n" + text);

		fleshWindow();
	}

	/**
	 * After task finished - print result, store total time and progressBar settings for next use. Open browser if it is requested. Closes
	 * application if it is requested.
	 * 
	 * @param progressBarSettings Properties to store progressbar values
	 * @param urlToIndex Url to index page (used for opennig browser)
	 */
	private void postTask(Properties progressBarSettings, String urlToIndex) {
		long totalTime = System.currentTimeMillis() - this.startTime;

		this.processDialog.getButtonOk().setEnabled(true);
		this.processDialog.getButtonCancel().setEnabled(false);

		if (this.error) {
			this.processDialog.setTitle(GuiTools.getLocalized("process.title.error"));
			return;
		}

		this.progress.setValue(100);
		this.labelPercent.setText("100%");
		this.processDialog.setTitle(GuiTools.getLocalized("process.title.ok"));
		this.processDialog.getTextDetails().setText(
				GuiTools.getLocalized("process.successfullyDone", null, new String[] { new Long(totalTime / 1000).toString() }));

		for (String warning : Warnings.getInstance().getWarnings()) {
			// TODO localization of warnings
			Main.status.appendOutputMessage(warning);
		}

		progressBarSettings.clear();
		progressMap.clear();
		for (String key : newProgressMap.keySet()) {
			long value = 100 * newProgressMap.get(key) / totalTime;
			progressMap.put(key, value);
			progressBarSettings.setProperty(key, value + "");
		}

		try {
			progressBarSettings.store(new FileOutputStream(new File(GuiTools.getLastUsedOutput(), ".progressbar.properties")), null);
		} catch (Throwable t) {
			log.warn("Can not write progressbar properties", t);
		}

		if (urlToIndex != null && GuiForm.getInstance().getCheckOpenBrowser().isSelected()) {
			try {
				Desktop.getDesktop().browse(new URI("file:///" + urlToIndex.replace(File.separator, "/")));
			} catch (Exception e) {
				log.error("Can not open generated file in default browser", e);
			}
		}

		fleshWindow();

		if (GuiForm.getInstance().getCheckAutoclose().isSelected()) {
			GuiForm.getInstance().getMainWindowListener().windowClosing(null);
		}
	}

	/**
	 * Fleshes window after generating done or failed
	 */
	private void fleshWindow() {
		this.processDialog.toFront();
		GuiForm.getInstance().setState(java.awt.Frame.NORMAL);
		Toolkit.getDefaultToolkit().beep();
	}

	/**
	 * Prepares data and dialog before task.
	 * 
	 * @param progressBarSettings Properties with last progressbar values
	 */
	private void preTask(Properties progressBarSettings) {
		try {
			progressBarSettings.load(new FileInputStream(new File(GuiTools.getLastUsedOutput(), ".progressbar.properties")));
		} catch (Throwable t) {
			try {
				progressBarSettings.load(new FileInputStream(new File(new File(Tools.getBasePath().getParent(), "etc"), "progressbar.properties")));
			} catch (Throwable t1) {
				log.warn("Can not load progressbar default properties", t1);
			}
		}

		for (Object key : progressBarSettings.keySet()) {
			try {
				progressMap.put((String) key, Long.parseLong(progressBarSettings.getProperty((String) key)));
			} catch (Throwable t) {
				// nothing to do, skip this value
			}
		}

		this.processDialog.setTitle(GuiTools.getLocalized("process.title"));
		this.progress.setValue(0);
		this.labelPercent.setText("0%");
		this.labelLeft.setText("? s");
		this.processDialog.getButtonOk().setEnabled(false);

		// TODO uncomment: this.processDialog.getButtonCancel().setEnabled(true);
		this.startTime = System.currentTimeMillis();
	}

	/**
	 * Prints message to the box on dialog by given key of messages and parameters of message. If directly is false then prints only if las
	 * message was printed before one second, otherwise prints message directly.
	 * 
	 * @param key Key of message to show
	 * @param directly true in case print always, false in case print only if last message was printed 1s ago and more
	 * @param parameters Parameters of message
	 */
	private void printOutputMessage(String key, boolean directly, String... parameters) {
		long time = new Date().getTime();

		if (directly || time - this.lastTime > 1000) {
			this.processDialog.getTextDetails().setText(GuiTools.getLocalized(key, key, parameters));
			this.lastTime = time;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printOutputMessage(String key, String... parameters) {
		printOutputMessage(key, false, parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void printOutputMessageDirectly(String key, String... parameters) {
		printOutputMessage(key, true, parameters);
	}

	/**
	 * Runs generating
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Properties progressBarSettings = new Properties();

		preTask(progressBarSettings);

		String urlToIndex = Main.main(new String[] { "-f", GuiTools.getLastUsedRod(), "-o", GuiTools.getLastUsedOutput(), "-s",
				new File(GuiTools.getLastUsedOutput(), "css").getAbsolutePath(), "-i",
				new File(GuiTools.getLastUsedOutput(), "imgs").getAbsolutePath(), "-c", this.templateProperties }, this.properties, this);

		postTask(progressBarSettings, urlToIndex);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setProcessStatus(Class clazz, String id) {
		Long value = progressMap.get(clazz.getSimpleName() + id);
		long time = new Date().getTime() - this.startTime;

		if (value != null) {
			this.progress.setValue(value.intValue());
			this.labelLeft.setText((100 - value) * (time / (value == 0 ? 1 : value)) / 1000 + "s");
			this.labelPercent.setText(value + "%");
		}

		newProgressMap.put(id, time);
	}
}
