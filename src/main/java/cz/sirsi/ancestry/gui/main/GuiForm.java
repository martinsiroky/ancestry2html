package cz.sirsi.ancestry.gui.main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.sirsi.ancestry.core.Constants;
import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.core.data.Global;
import cz.sirsi.ancestry.core.tools.Tools;
import cz.sirsi.ancestry.gui.GenerationTask;
import cz.sirsi.ancestry.gui.OpenDialogFileFilter;
import cz.sirsi.ancestry.gui.bean.CheckVersionItemBean;
import cz.sirsi.ancestry.gui.bean.CheckboxBean;
import cz.sirsi.ancestry.gui.bean.MapsProviderItemBean;
import cz.sirsi.ancestry.gui.bean.PrivacyComboBean;
import cz.sirsi.ancestry.gui.bean.PrivacyProfileItemBean;
import cz.sirsi.ancestry.gui.bean.TemplateItemBean;
import cz.sirsi.ancestry.gui.component.PreviewCanvas;
import cz.sirsi.ancestry.gui.listener.MainWindowListener;
import cz.sirsi.ancestry.gui.renderer.ComboCheckNewVersionRenderer;
import cz.sirsi.ancestry.gui.renderer.ComboMapsRenderer;
import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is
 * being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each
 * developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A
 * COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL
 * PURPOSE.
 */
@SuppressWarnings("serial")
public class GuiForm extends JFrame implements Constants {

	/**
	 * Logger instance
	 */
	private static Logger log = Logger.getLogger(GuiForm.class);

	/**
	 * Singleton instance of GuiForm
	 */
	private static GuiForm guiForm;

	/**
	 * @return Singleton instance of GuiForm
	 */
	public static GuiForm getInstance() {
		if (guiForm != null) {
			return guiForm;
		}

		guiForm = new GuiForm();

		return guiForm;
	}

	/**
	 * Main method to display this JFrame. Arguments can be specified (from Ancestry e.g.).
	 * 
	 * @param args Arguments in following order: language code for GUI; path to rod file
	 */
	public static void main(String[] args) {
		log.info("Ancestry2html version: " + Tools.getVersion() + " started.");

		if (args.length > 0) {
			// first parameter which specifies language
			GuiTools.setLang(args[0]);
			log.info("lang set to " + args[0]);
		}

		if (args.length > 1) {
			// second parameter which specifies rod file
			GuiTools.setRodSendFromAncestry(args[1]);
			log.info("rod file was set to " + args[1]);
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GuiForm inst = GuiForm.getInstance();
				FormLayout instLayout = new FormLayout(
						"max(p;5dlu), 58dlu, 105dlu, max(p;15dlu), 23dlu, 88dlu, 50dlu, max(p;15dlu), 88dlu, 50dlu",
						"max(p;4dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), 15dlu, max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), 32dlu, max(p;15dlu), 22dlu");
				inst.getContentPane().setLayout(instLayout);
				inst.setVisible(true);
				inst.setSize(765, 686);
			}
		});
	}

	protected PrivacyProfilesManager privacyProfilesManager;

	protected TemplatesManager templatesManager;

	private Thread generationThread;

	private Insets insetsDimensions;

	private MainWindowListener mainWindowListener;

	protected List<CheckboxBean> checkboxes;

	protected List<PrivacyComboBean> privacyCombos;

	// components of form

	protected JButton buttonAddTemplate;
	protected JButton buttonDeleteProfile;
	protected JButton buttonGenerate;
	protected JButton buttonCheckNow;
	protected JButton buttonOutputBrowse;
	protected JButton buttonRodBrowse;
	protected JButton buttonSaveProfile;
	protected PreviewCanvas canvasPreview;
	protected JComboBox comboDefaultLanguage;
	protected JComboBox comboGuiLanguage;
	protected JComboBox comboHideAdditionalInfo;
	protected JComboBox comboHideBirth;
	protected JComboBox comboHideContact;
	protected JComboBox comboHideCredentials;
	protected JComboBox comboHideDeath;
	protected JComboBox comboHideDocuments;
	protected JComboBox comboHideEducation;
	protected JComboBox comboHideHeads;
	protected JComboBox comboHideJobs;
	protected JComboBox comboHideMarriageDate;
	protected JComboBox comboHideMarriagePlace;
	protected JComboBox comboHideNationality;
	protected JComboBox comboHidePhotos;
	protected JComboBox comboHidePlaces;
	protected JComboBox comboHideRegistryInfo;
	protected JComboBox comboHideReligion;
	protected JComboBox comboHideUserDefined;
	protected JComboBox comboHideWebpages;
	protected JComboBox comboCheckNewVersion;
	protected JComboBox comboMaps;
	protected JComboBox comboPictureResolution;
	protected JComboBox comboPrivacyProfiles;
	protected JComboBox comboTemplate;
	protected JTextField editAddressSplitter;
	protected JTextField editHideGroups;
	protected JTextField editMainPerson;
	protected JTextField editOnlyGroups;
	protected JTextField editOutputDir;
	protected JTextField editRod;
	protected JCheckBox checkAutoclose;
	protected JCheckBox checkBlockHidden;
	protected JCheckBox checkDebug;
	protected JCheckBox checkMaidenName;
	protected JList listBlockedGroups;
	protected JList listAllowedGroups;
	protected JCheckBox checkShowMainPerson;
	protected JCheckBox checkHideAdditionalInfo;
	protected JCheckBox checkHidePath;
	protected JCheckBox checkInfoBlocked;
	protected JCheckBox checkInfoAllowed;
	protected JCheckBox checkOpenBrowser;
	protected JCheckBox checkShowBirthInLists;
	protected JCheckBox checkShowDeathInLists;
	protected JCheckBox checkShowId;
	protected JCheckBox checkShowIdInLists;
	protected JCheckBox checkShowShortDescInList;
	protected JCheckBox checkShowWeddingNameInList;
	protected JCheckBox checkUseBurialPlace;
	protected JScrollPane jScrollPane1;
	protected JTabbedPane jTabbedPane;
	protected JLabel labelAddressSplitter;
	protected JLabel labelDefaultLanguage;
	protected JLabel labelGuiLanguage;
	protected JLabel labelHideAdditionalInfo;
	protected JLabel labelHideBirth;
	protected JLabel labelHideContact;
	protected JLabel labelHideCredentials;
	protected JLabel labelHideDeath;
	protected JLabel labelHideDocuments;
	protected JLabel labelHideEducation;
	protected JLabel labelHideGroups;
	protected JLabel labelHideHeads;
	protected JLabel labelHideJobs;
	protected JLabel labelHideMarriageDate;
	protected JLabel labelHideMarriagePlace;
	protected JLabel labelHideNationality;
	protected JLabel labelHidePhotos;
	protected JLabel labelHidePlaces;
	protected JLabel labelHideRegistryInfo;
	protected JLabel labelHideReligion;
	protected JLabel labelHideUserDefined;
	protected JLabel labelHideWebpages;
	protected JLabel labelCheckNewVersion;
	protected JLabel labelLanguage;
	protected JLabel labelMainPerson;
	protected JLabel labelMaps;
	protected JLabel labelOnlyGroups;
	protected JLabel labelOutputDir;
	protected JLabel labelPictureResolution;
	protected JLabel labelPreview;
	protected JLabel labelProfile;
	protected JLabel labelRod;
	protected JLabel labelTemplate;
	protected JLabel labelTownInAddress;
	protected JLabel labelTownInBirth;
	protected JLabel labelWindowSetting;
	protected JList listLanguages;
	protected JPanel paneImagesSettings;
	protected JPanel panelAditionalSettings;
	protected JPanel panelBasicSettings;
	protected JPanel panelPlaces;
	protected JSeparator separatorPrivacy;
	protected JSeparator separatorPrivacy2;
	protected JSeparator separatorWindowSettings;
	protected JSpinner spinnerTownInAddress;
	protected JSpinner spinnerTownInBirth;

	// Set Look & Feel
	{
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			log.error("Error setting look and feel", e);
			e.printStackTrace();
		}
	}

	/**
	 * Constructs instance of form
	 */
	private GuiForm() {
		super();

		this.privacyProfilesManager = new PrivacyProfilesManager(this);
		this.templatesManager = new TemplatesManager(this);

		GuiTools.readIniProperties(null);
		GuiTools.initializeLogger();
		GuiTools.initLang();
		this.templatesManager.readListOfTemplates();
		this.privacyProfilesManager.readListOfPrivacyProfiles();
		GuiTools.readMapsProperties();
		initGUI();
		this.templatesManager.repaintPreview();
		// $hide>>$
		this.privacyProfilesManager.readAllPrivacyCombos();
		// $hide<<$
	}

	/**
	 * Generate html action
	 */
	public void buttonGenerateActionPerformed() {
		// $hide>>$
		Properties properties = prepareSettingsForNextUse();

		// save settings of form for next use into output directory
		GuiTools.saveIniProperties(this.editOutputDir.getText());

		startGenerationProcess(properties);
		// $hide<<$
	}

	/**
	 * Checks new updates on the web
	 */
	protected void buttonCheckNowActionPerformed() {
		checkForNewVersion(true);
		GuiTools.setLastUsed(Constants.PROPERTY_LAST_CHECK, Long.toString(new GregorianCalendar().getTimeInMillis()));
	}

	/**
	 * Browsing output folder action
	 */
	public void buttonOutputBrowseActionPerformed() {
		// $hide>>$
		JFileChooser fc = new JFileChooser();

		fc.setDialogTitle(GuiTools.getLocalized("selectOutput.title"));
		fc.setCurrentDirectory(new File(this.editOutputDir.getText()));

		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		fc.showOpenDialog(this);

		if (fc.getSelectedFile() != null) {
			String file = fc.getSelectedFile().getAbsolutePath();
			this.editOutputDir.setText(file);

			File lastSettingsFile = new File(file, ".Ancestry2html.properties");
			// if output folder contains setting of window used in last generation
			if (lastSettingsFile.exists()) {
				// TODO localize: JOptionPane jOptionPane = new JOptionPane(-);
				int result = JOptionPane.showConfirmDialog(this, GuiTools.getLocalized("selectOutput.loadSettings", null, new String[] { "\n" }), GuiTools
						.getLocalized("selectOutput.loadSettings.title"), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
					GuiTools.readIniProperties(file);
					initializeComponent();
					this.templatesManager.repaintPreview();

					this.privacyProfilesManager.readAllPrivacyCombos();
				}
			}
		}
		// $hide<<$
	}

	/**
	 * Browsing rod file action
	 */
	public void buttonRodBrowseActionPerformed() {
		// $hide>>$
		JFileChooser fc = new JFileChooser();

		fc.setDialogTitle(GuiTools.getLocalized("openRod.title"));
		fc.setCurrentDirectory(new File(this.editRod.getText()));

		fc.setAcceptAllFileFilterUsed(true);
		fc.addChoosableFileFilter(new OpenDialogFileFilter("rod", GuiTools.getLocalized("rod.name") + " (*.rod)"));
		fc.addChoosableFileFilter(new OpenDialogFileFilter("rodx", GuiTools.getLocalized("rodx.name") + " (*.rodx)"));
		fc.addChoosableFileFilter(new OpenDialogFileFilter("rodz", GuiTools.getLocalized("rodz.name") + " (*.rodz)"));

		fc.showOpenDialog(this);

		if (fc.getSelectedFile() != null) {
			String file = fc.getSelectedFile().getAbsolutePath();
			this.editRod.setText(file);
		}
		// $hide<<$
	}

	/**
	 * Action when language combo changed
	 * 
	 * @param evt Event that invoked this method
	 */
	protected void comboGuiLanguageItemStateChanged(ItemEvent evt) {
		if (evt.getStateChange() == ItemEvent.SELECTED) {
			GuiForm.getInstance().prepareSettingsForNextUse();
			GuiTools.saveIniProperties(null);
			GuiTools.saveIniProperties(GuiTools.getLastUsedOutput());

			GuiTools.readIniProperties(null);
			GuiTools.setLang(this.comboGuiLanguage.getSelectedItem().toString());
			GuiTools.initLang();
			initializeComponent();
			localizeForm();
			this.privacyProfilesManager.readAllPrivacyCombos();
		}
	}

	/**
	 * Checks newer version of application on website
	 * 
	 * @param showNotFound If show warning box when new version is not found (to disable this on automatically check)
	 */
	public void checkForNewVersion(boolean showNotFound) {
		try {
		    URL url = new URL("https://raw.githubusercontent.com/martinsiroky/ancestry2html/master/latestVersion");

            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(20000);
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String version = reader.readLine();

            if (Tools.compareTwoVersions(Tools.getVersion(), version) < 0) {
                int result = JOptionPane.showConfirmDialog(this, GuiTools.getLocalized("newerVersion.download", null, new String[] { version,
                        Tools.getVersion(), "\n" }), GuiTools.getLocalized("newerVersion.download.title"), JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try {
                        Desktop.getDesktop().browse(new URI("https://github.com/martinsiroky/ancestry2html/releases"));
                    } catch (Exception e) {
                        log.error("Can not open generated file in default browser", e);
                    }
                }
            } else if (showNotFound) {
                JOptionPane.showMessageDialog(this, GuiTools.getLocalized("newerVersion.notFound"), GuiTools
                        .getLocalized("newerVersion.notFound.title"), JOptionPane.INFORMATION_MESSAGE);
            }
		} catch (Throwable t) {
			JOptionPane.showMessageDialog(this, GuiTools.getLocalized("newerVersion.error"), GuiTools.getLocalized("newerVersion.error.title"),
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	/**
	 * Initializes gui
	 */
	private void initGUI() {
		try {
			FormLayout thisLayout = new FormLayout("max(p;4dlu), 499dlu, max(p;4dlu)", "max(p;0dlu), 400dlu, 5dlu, 20dlu, max(p;5px)");
			getContentPane().setLayout(thisLayout);
			setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			JPopupMenu.setDefaultLightWeightPopupEnabled(false);

			this.mainWindowListener = new MainWindowListener();
			addWindowListener(this.mainWindowListener);

			setTitle("Ancestry2html - " + Tools.getVersion());
			setVisible(false);

			setLocationByPlatform(true);
			setResizable(false);

			{
				this.buttonGenerate = new JButton();
				getContentPane().add(this.buttonGenerate, new CellConstraints("2, 4, 1, 1, fill, fill"));
				this.buttonGenerate.setText("Generate");
				this.buttonGenerate.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonGenerateActionPerformed();
					}
				});
			}

			{
				this.jTabbedPane = new JTabbedPane();
				getContentPane().add(this.jTabbedPane, new CellConstraints("1, 2, 3, 1, fill, fill"));
			}

			{
				this.panelBasicSettings = new JPanel();
				FormLayout panelBasicSettingsLayout = new FormLayout(
						"max(p;5dlu), 80dlu, 100dlu, 15dlu, 15dlu, 80dlu, max(p;50dlu), 15dlu, 80dlu, 50dlu",
						"max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), 24dlu, max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), 15dlu, max(p;15dlu), max(p;15dlu), 15dlu, max(p;15dlu), max(p;15dlu)");
				this.panelBasicSettings.setLayout(panelBasicSettingsLayout);
				this.jTabbedPane.addTab("Basic", null, this.panelBasicSettings, null);
				this.panelBasicSettings.setPreferredSize(new java.awt.Dimension(759, 593));
			}
			{
				this.panelAditionalSettings = new JPanel();
				FormLayout panelAditionalSettingsLayout = new FormLayout(
						"max(p;5px), 80dlu, 50dlu, 15dlu, 191dlu, 122dlu",
						"max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu)");
				this.panelAditionalSettings.setLayout(panelAditionalSettingsLayout);
				this.jTabbedPane.addTab("Aditional", null, this.panelAditionalSettings, null);
			}
			{
				this.paneImagesSettings = new JPanel();
				FormLayout paneImagesSettingsLayout = new FormLayout("max(p;5dlu), 80dlu, 50dlu, max(p;5dlu)",
						"max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu)");
				this.paneImagesSettings.setLayout(paneImagesSettingsLayout);
				this.jTabbedPane.addTab("Images", null, this.paneImagesSettings, null);
			}
			{
				this.panelPlaces = new JPanel();
				FormLayout panelPlacesLayout = new FormLayout("max(p;5px), 80dlu, 50dlu, max(p;5dlu)",
						"max(p;5dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu), max(p;15dlu)");
				this.panelPlaces.setLayout(panelPlacesLayout);
				this.jTabbedPane.addTab("Places", null, this.panelPlaces, null);
			}
			{
				this.labelPictureResolution = new JLabel();
				this.paneImagesSettings.add(this.labelPictureResolution, new CellConstraints("2, 2, 1, 1, default, default"));
				this.labelPictureResolution.setText("Max picture resolution");
				this.labelPictureResolution.setLabelFor(this.comboPictureResolution);
			}
			{
				this.comboPictureResolution = new JComboBox();
				this.paneImagesSettings.add(this.comboPictureResolution, new CellConstraints("3, 2, 1, 1, default, default"));
			}
			{
				this.editMainPerson = new JTextField();
				this.panelAditionalSettings.add(this.editMainPerson, new CellConstraints("3, 5, 1, 1, default, default"));
			}
			{
				this.labelMainPerson = new JLabel();
				this.panelAditionalSettings.add(this.labelMainPerson, new CellConstraints("2, 5, 1, 1, default, default"));
				this.labelMainPerson.setText("Main person");
			}
			{
				this.checkShowId = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowId, new CellConstraints("2, 2, 2, 1, default, default"));
				this.checkShowId.setText("Show id");
			}
			{
				this.checkMaidenName = new JCheckBox();
				this.panelAditionalSettings.add(this.checkMaidenName, new CellConstraints("2, 3, 3, 1, default, default"));
				this.checkMaidenName.setText("Use maiden name");
			}
			{
				this.checkUseBurialPlace = new JCheckBox();
			}
			{
				this.labelOnlyGroups = new JLabel();
				this.panelAditionalSettings.add(this.labelOnlyGroups, new CellConstraints("2, 13, 2, 1, default, default"));
				this.labelOnlyGroups.setText("Allowed groups");
			}
			{
				this.editOnlyGroups = new JTextField();
				this.panelAditionalSettings.add(this.editOnlyGroups, new CellConstraints("2, 14, 2, 1, default, default"));
				this.editOnlyGroups.setName("editOnlyGroups");
			}
			{
				this.checkBlockHidden = new JCheckBox();
				this.panelAditionalSettings.add(this.checkBlockHidden, new CellConstraints("2, 19, 2, 1, default, default"));
				this.checkBlockHidden.setText("Block hidden");
			}
			{
				this.editHideGroups = new JTextField();
				this.panelAditionalSettings.add(this.editHideGroups, new CellConstraints("2, 18, 2, 1, default, default"));
			}
			{
				this.labelHideGroups = new JLabel();
				this.panelAditionalSettings.add(this.labelHideGroups, new CellConstraints("2, 17, 2, 1, default, default"));
				this.labelHideGroups.setText("Blocked groups");
			}
			{
				this.labelLanguage = new JLabel();
				this.panelAditionalSettings.add(this.labelLanguage, new CellConstraints("2, 8, 1, 1, default, default"));
				this.labelLanguage.setText("Language");
			}
			{
				this.checkShowIdInLists = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowIdInLists, new CellConstraints("5, 2, 1, 1, default, default"));
				this.checkShowIdInLists.setText("Show id in lists");
			}
			{
				this.checkShowBirthInLists = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowBirthInLists, new CellConstraints("5, 3, 1, 1, default, default"));
				this.checkShowBirthInLists.setText("Show birth in lists");
			}
			{
				this.checkShowDeathInLists = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowDeathInLists, new CellConstraints("5, 4, 1, 1, default, default"));
				this.checkShowDeathInLists.setText("Show death in lists");
			}
			{
				this.checkShowShortDescInList = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowShortDescInList, new CellConstraints("5, 7, 1, 1, default, default"));
				this.checkShowShortDescInList.setText("Show short description in documents list");
			}
			{
				this.checkShowWeddingNameInList = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowWeddingNameInList, new CellConstraints("5, 5, 1, 1, default, default"));
				this.checkShowWeddingNameInList.setText("Show weding name in lists");
				this.checkShowWeddingNameInList.setEnabled(false);
			}
			{
				this.listLanguages = new JList();
			}
			{
				this.labelDefaultLanguage = new JLabel();
				this.panelAditionalSettings.add(this.labelDefaultLanguage, new CellConstraints("2, 11, 1, 1, default, default"));
				this.labelDefaultLanguage.setText("Default languge");
			}
			{
				this.comboDefaultLanguage = new JComboBox();
				this.panelAditionalSettings.add(this.comboDefaultLanguage, new CellConstraints("3, 11, 1, 1, default, default"));
			}
			{
				this.jScrollPane1 = new JScrollPane();
				this.panelAditionalSettings.add(this.jScrollPane1, new CellConstraints("3, 8, 1, 3, default, default"));

				{
					this.listLanguages = new JList();
					this.jScrollPane1.setViewportView(this.listLanguages);
					this.listLanguages.addListSelectionListener(new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent evt) {
							listLanguagesValueChanged(evt);
						}
					});
				}
			}
			{
				this.checkDebug = new JCheckBox();
				this.panelAditionalSettings.add(this.checkDebug, new CellConstraints("5, 25, 2, 1, default, default"));
				this.checkDebug.setText("Enable debug logging (extremly slower)");
			}
			{
				this.checkInfoAllowed = new JCheckBox();
				this.panelAditionalSettings.add(this.checkInfoAllowed, new CellConstraints("2, 15, 2, 1, default, default"));
				this.checkInfoAllowed.setText("Show list of allowed on index");
			}
			{
				this.checkInfoBlocked = new JCheckBox();
				this.panelAditionalSettings.add(this.checkInfoBlocked, new CellConstraints("2, 20, 2, 1, default, default"));
				this.checkInfoBlocked.setText("Show list of blocked on index page");
			}
			{
				this.checkHidePath = new JCheckBox();
				this.panelAditionalSettings.add(this.checkHidePath, new CellConstraints("5, 9, 1, 1, default, default"));
				this.checkHidePath.setText("Hide path to main person");
			}
			{
				this.checkHideAdditionalInfo = new JCheckBox();
				this.panelAditionalSettings.add(this.checkHideAdditionalInfo, new CellConstraints("5, 10, 1, 1, default, default"));
				this.checkHideAdditionalInfo.setText("Hide additional info on index.htm");
			}
			{
				this.checkShowMainPerson = new JCheckBox();
				this.panelAditionalSettings.add(this.checkShowMainPerson, new CellConstraints("5, 8, 1, 1, default, default"));
				this.checkShowMainPerson.setText("Show main person");
			}
			{
				ListModel listAllowedGroupsModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
				this.listAllowedGroups = new JList();
				this.panelAditionalSettings.add(this.listAllowedGroups, new CellConstraints("5, 13, 1, 3, default, default"));
				this.listAllowedGroups.setModel(listAllowedGroupsModel);
			}
			{
				ListModel listBlockedGroupsModel = new DefaultComboBoxModel(new String[] { "Item One", "Item Two" });
				this.listBlockedGroups = new JList();
				this.panelAditionalSettings.add(this.listBlockedGroups, new CellConstraints("5, 17, 1, 3, default, default"));
				this.listBlockedGroups.setModel(listBlockedGroupsModel);
			}
			{
				this.editAddressSplitter = new JTextField();
				this.panelPlaces.add(this.editAddressSplitter, new CellConstraints("3, 2, 1, 1, default, default"));
			}
			{
				this.labelAddressSplitter = new JLabel();
				this.panelPlaces.add(this.labelAddressSplitter, new CellConstraints("2, 2, 1, 1, default, default"));
				this.labelAddressSplitter.setText("Address splitter");
			}
			{
				this.labelTownInAddress = new JLabel();
				this.panelPlaces.add(this.labelTownInAddress, new CellConstraints("2, 3, 1, 1, default, default"));
				this.labelTownInAddress.setText("Town in address");
			}
			{
				this.spinnerTownInAddress = new JSpinner();
				this.panelPlaces.add(this.spinnerTownInAddress, new CellConstraints("3, 3, 1, 1, default, default"));
			}
			{
				this.spinnerTownInBirth = new JSpinner();
				this.panelPlaces.add(this.spinnerTownInBirth, new CellConstraints("3, 4, 1, 1, default, default"));
			}
			{
				this.labelTownInBirth = new JLabel();
				this.panelPlaces.add(this.labelTownInBirth, new CellConstraints("2, 4, 1, 1, default, default"));
				this.labelTownInBirth.setText("Town in birth");
			}
			{
				this.comboMaps = new JComboBox();
				this.panelPlaces.add(this.comboMaps, new CellConstraints("3, 5, 1, 1, default, default"));
			}
			{
				this.labelMaps = new JLabel();
				this.panelPlaces.add(this.labelMaps, new CellConstraints("2, 5, 1, 1, default, default"));
				this.labelMaps.setText("Maps searching");
			}
			{
				this.checkUseBurialPlace = new JCheckBox();
				this.panelPlaces.add(this.checkUseBurialPlace, new CellConstraints("2, 6, 2, 1, default, default"));
				this.checkUseBurialPlace.setText("Use burial place");
			}
			{
				this.comboHideBirth = new JComboBox();
				this.panelBasicSettings.add(this.comboHideBirth, new CellConstraints("7, 2, 1, 1, left, center"));
				this.comboHideBirth.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideBirth.setSize(75, 21);
			}
			{
				this.labelHideBirth = new JLabel();
				this.panelBasicSettings.add(this.labelHideBirth, new CellConstraints("6, 2, 1, 1, left, center"));
				this.labelHideBirth.setText("Hide birth");
			}
			{
				this.buttonRodBrowse = new JButton();
				this.panelBasicSettings.add(this.buttonRodBrowse, new CellConstraints("4, 2, 1, 1, center, center"));
				this.buttonRodBrowse.setText("...");
				this.buttonRodBrowse.setMargin(new java.awt.Insets(2, 2, 2, 2));
				this.buttonRodBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonRodBrowseActionPerformed();
					}
				});
			}
			{
				this.editRod = new JTextField();
				this.panelBasicSettings.add(this.editRod, new CellConstraints("3, 2, 1, 1, default, default"));
				this.editRod.setEditable(false);
				if (GuiTools.isRodSendFromAncestry()) {
					this.editRod.setEnabled(false);
					this.buttonRodBrowse.setEnabled(false);
				}
			}
			{
				this.labelRod = new JLabel();
				this.panelBasicSettings.add(this.labelRod, new CellConstraints("2, 2, 1, 1, default, default"));
				this.labelRod.setText("Rod file");
			}
			{
				this.labelHideDeath = new JLabel();
				this.panelBasicSettings.add(this.labelHideDeath, new CellConstraints("6, 3, 1, 1, default, default"));
				this.labelHideDeath.setText("Hide death");
			}
			{
				this.comboHideDeath = new JComboBox();
				this.panelBasicSettings.add(this.comboHideDeath, new CellConstraints("7, 3, 1, 1, left, center"));
				this.comboHideDeath.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideDeath.setSize(75, 21);
			}
			{
				this.comboHidePlaces = new JComboBox();
				this.panelBasicSettings.add(this.comboHidePlaces, new CellConstraints("7, 4, 1, 1, left, center"));
				this.comboHidePlaces.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHidePlaces.setSize(75, 21);
			}
			{
				this.labelHidePlaces = new JLabel();
				this.panelBasicSettings.add(this.labelHidePlaces, new CellConstraints("6, 4, 1, 1, left, center"));
				this.labelHidePlaces.setText("Hide places");
			}
			{
				this.labelHideJobs = new JLabel();
				this.panelBasicSettings.add(this.labelHideJobs, new CellConstraints("6, 5, 1, 1, left, center"));
				this.labelHideJobs.setText("Hide jobs");
			}
			{
				this.comboHideJobs = new JComboBox();
				this.panelBasicSettings.add(this.comboHideJobs, new CellConstraints("7, 5, 1, 1, left, center"));
				this.comboHideJobs.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideJobs.setSize(75, 21);
			}
			{
				this.comboHideEducation = new JComboBox();
				this.panelBasicSettings.add(this.comboHideEducation, new CellConstraints("7, 6, 1, 1, left, center"));
				this.comboHideEducation.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideEducation.setSize(75, 21);
			}
			{
				this.labelHideEducation = new JLabel();
				this.panelBasicSettings.add(this.labelHideEducation, new CellConstraints("6, 6, 1, 1, left, center"));
				this.labelHideEducation.setText("Hide education");
			}
			{
				this.comboHideContact = new JComboBox();
				this.panelBasicSettings.add(this.comboHideContact, new CellConstraints("7, 7, 1, 1, left, center"));
				this.comboHideContact.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideContact.setSize(75, 21);
			}
			{
				this.labelHideContact = new JLabel();
				this.panelBasicSettings.add(this.labelHideContact, new CellConstraints("6, 7, 1, 1, left, center"));
				this.labelHideContact.setText("Hide contact");
			}
			{
				this.comboHideWebpages = new JComboBox();
				this.panelBasicSettings.add(this.comboHideWebpages, new CellConstraints("7, 8, 1, 1, left, center"));
				this.comboHideWebpages.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideWebpages.setSize(75, 21);
			}
			{
				this.labelHideWebpages = new JLabel();
				this.panelBasicSettings.add(this.labelHideWebpages, new CellConstraints("6, 8, 1, 1, left, center"));
				this.labelHideWebpages.setText("Hide webpages");
			}
			{
				this.comboHideReligion = new JComboBox();
				this.panelBasicSettings.add(this.comboHideReligion, new CellConstraints("7, 9, 1, 1, left, center"));
				this.comboHideReligion.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideReligion.setSize(75, 21);
			}
			{
				this.labelHideReligion = new JLabel();
				this.panelBasicSettings.add(this.labelHideReligion, new CellConstraints("6, 9, 1, 1, left, center"));
				this.labelHideReligion.setText("Hide religion");
			}
			{
				this.comboHideNationality = new JComboBox();
				this.panelBasicSettings.add(this.comboHideNationality, new CellConstraints("7, 10, 1, 1, left, center"));
				this.comboHideNationality.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideNationality.setSize(75, 21);
			}
			{
				this.labelHideNationality = new JLabel();
				this.panelBasicSettings.add(this.labelHideNationality, new CellConstraints("6, 10, 1, 1, left, center"));
				this.labelHideNationality.setText("Hide nationality");
			}
			{
				this.comboHideUserDefined = new JComboBox();
				this.panelBasicSettings.add(this.comboHideUserDefined, new CellConstraints("7, 11, 1, 1, left, center"));
				this.comboHideUserDefined.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideUserDefined.setSize(75, 21);
			}
			{
				this.labelHideUserDefined = new JLabel();
				this.panelBasicSettings.add(this.labelHideUserDefined, new CellConstraints("6, 11, 1, 1, left, center"));
				this.labelHideUserDefined.setText("Hide user defined");
			}
			{
				this.comboHideRegistryInfo = new JComboBox();
				this.panelBasicSettings.add(this.comboHideRegistryInfo, new CellConstraints("7, 12, 1, 1, left, center"));
				this.comboHideRegistryInfo.setPreferredSize(new java.awt.Dimension(75, 21));
				this.comboHideRegistryInfo.setSize(75, 21);
			}
			{
				this.labelHideRegistryInfo = new JLabel();
				this.panelBasicSettings.add(this.labelHideRegistryInfo, new CellConstraints("6, 12, 1, 1, left, center"));
				this.labelHideRegistryInfo.setText("Hide registry info");
			}
			{
				this.buttonAddTemplate = new JButton();
				this.panelBasicSettings.add(this.buttonAddTemplate, new CellConstraints("3, 5, 2, 1, fill, center"));
				this.buttonAddTemplate.setText("Add template");
				this.buttonAddTemplate.setEnabled(false);
			}
			{
				this.comboTemplate = new JComboBox();
				this.panelBasicSettings.add(this.comboTemplate, new CellConstraints("3, 4, 2, 1, fill, center"));
				this.comboTemplate.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						GuiForm.this.templatesManager.comboTemplateItemStateChanged(evt);
					}
				});
			}
			{
				this.labelTemplate = new JLabel();
				this.panelBasicSettings.add(this.labelTemplate, new CellConstraints("2, 4, 1, 1, default, default"));
				this.labelTemplate.setText("Template");
			}
			{
				this.buttonOutputBrowse = new JButton();
				this.panelBasicSettings.add(this.buttonOutputBrowse, new CellConstraints("4, 7, 1, 1, default, default"));
				this.buttonOutputBrowse.setText("...");
				this.buttonOutputBrowse.setMargin(new java.awt.Insets(2, 2, 2, 2));
				this.buttonOutputBrowse.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonOutputBrowseActionPerformed();
					}
				});
			}
			{
				this.editOutputDir = new JTextField();
				this.panelBasicSettings.add(this.editOutputDir, new CellConstraints("3, 7, 1, 1, fill, center"));
				this.editOutputDir.setEditable(false);
			}
			{
				this.labelOutputDir = new JLabel();
				this.panelBasicSettings.add(this.labelOutputDir, new CellConstraints("2, 7, 1, 1, left, center"));
				this.labelOutputDir.setText("Output dir");
			}
			{
				this.buttonDeleteProfile = new JButton();
				this.panelBasicSettings.add(this.buttonDeleteProfile, new CellConstraints("3, 11, 2, 1, fill, center"));
				this.buttonDeleteProfile.setText("Delete profile");
				this.buttonDeleteProfile.setEnabled(false);
				this.buttonDeleteProfile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						GuiForm.this.privacyProfilesManager.buttonDeletePrivacyProfileActionPerformed();
					}
				});
			}
			{
				this.buttonSaveProfile = new JButton();
				this.panelBasicSettings.add(this.buttonSaveProfile, new CellConstraints("3, 10, 2, 1, fill, center"));
				this.buttonSaveProfile.setText("Save profile");
				this.buttonSaveProfile.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						GuiForm.this.privacyProfilesManager.buttonSavePrivacyProfileActionPerformed();
					}
				});
				if (!new File(Constants.PRIVACY_PROFILES_FILE_NAME).canWrite()) {
					this.buttonSaveProfile.setEnabled(false);
				}
			}
			{
				this.comboPrivacyProfiles = new JComboBox();
				this.panelBasicSettings.add(this.comboPrivacyProfiles, new CellConstraints("3, 9, 2, 1, default, default"));
				this.comboPrivacyProfiles.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						GuiForm.this.privacyProfilesManager.comboPrivacyProfilesItemStateChanged(evt);
					}
				});
			}
			{
				this.labelProfile = new JLabel();
				this.panelBasicSettings.add(this.labelProfile, new CellConstraints(2, 9, 1, 1, CellConstraints.LEFT, CellConstraints.CENTER,
						new Insets(0, 0, 0, 3)));
				this.labelProfile.setText("Privacy profile");
			}
			{
				this.comboHideHeads = new JComboBox();
				this.panelBasicSettings.add(this.comboHideHeads, new CellConstraints("10, 2, 1, 1, default, default"));
			}
			{
				this.labelHideHeads = new JLabel();
				this.panelBasicSettings.add(this.labelHideHeads, new CellConstraints("9, 2, 1, 1, default, default"));
				this.labelHideHeads.setText("Hide heads");
			}
			{
				this.comboHidePhotos = new JComboBox();
				this.panelBasicSettings.add(this.comboHidePhotos, new CellConstraints("10, 3, 1, 1, default, default"));
			}
			{
				this.labelHidePhotos = new JLabel();
				this.panelBasicSettings.add(this.labelHidePhotos, new CellConstraints("9, 3, 1, 1, default, default"));
				this.labelHidePhotos.setText("Hide photos");
			}
			{
				this.comboHideDocuments = new JComboBox();
				this.panelBasicSettings.add(this.comboHideDocuments, new CellConstraints("10, 4, 1, 1, default, default"));
			}
			{
				this.labelHideDocuments = new JLabel();
				this.panelBasicSettings.add(this.labelHideDocuments, new CellConstraints("9, 4, 1, 1, default, default"));
				this.labelHideDocuments.setText("Hide documents");
			}
			{
				this.comboHideCredentials = new JComboBox();
				this.panelBasicSettings.add(this.comboHideCredentials, new CellConstraints("10, 5, 1, 1, default, default"));
			}
			{
				this.labelHideCredentials = new JLabel();
				this.panelBasicSettings.add(this.labelHideCredentials, new CellConstraints("9, 5, 1, 1, default, default"));
				this.labelHideCredentials.setText("Hide credentials");
			}
			{
				this.comboHideMarriageDate = new JComboBox();
				this.panelBasicSettings.add(this.comboHideMarriageDate, new CellConstraints("10, 6, 1, 1, default, default"));
				this.comboHideMarriageDate.setToolTipText("Hide marriage place");
			}
			{
				this.labelHideMarriageDate = new JLabel();
				this.panelBasicSettings.add(this.labelHideMarriageDate, new CellConstraints("9, 6, 1, 1, default, default"));
				this.labelHideMarriageDate.setText("Hide marriage date");
			}
			{
				this.comboHideMarriagePlace = new JComboBox();
				this.panelBasicSettings.add(this.comboHideMarriagePlace, new CellConstraints("10, 7, 1, 1, default, default"));
			}
			{
				this.labelHideMarriagePlace = new JLabel();
				this.panelBasicSettings.add(this.labelHideMarriagePlace, new CellConstraints("9, 7, 1, 1, default, default"));
				this.labelHideMarriagePlace.setText("Hide marriage place");
			}
			{
				this.comboHideAdditionalInfo = new JComboBox();
				this.panelBasicSettings.add(this.comboHideAdditionalInfo, new CellConstraints("10, 8, 1, 1, default, default"));
			}
			{
				this.labelHideAdditionalInfo = new JLabel();
				this.panelBasicSettings.add(this.labelHideAdditionalInfo, new CellConstraints("9, 8, 1, 1, default, default"));
				this.labelHideAdditionalInfo.setText("Hide aditional data");
			}
			{
				this.labelPreview = new JLabel();
				this.panelBasicSettings.add(this.labelPreview, new CellConstraints("2, 13, 6, 1, center, center"));
				this.labelPreview.setText("Preview");
			}
			{
				this.canvasPreview = new PreviewCanvas();
				this.panelBasicSettings.add(this.canvasPreview, new CellConstraints("2, 14, 6, 11, center, fill"));
				this.canvasPreview.setBackground(new java.awt.Color(255, 255, 255));
				this.canvasPreview.setPreferredSize(new java.awt.Dimension(400, 300));
			}
			{
				this.comboGuiLanguage = new JComboBox();
				this.panelBasicSettings.add(this.comboGuiLanguage, new CellConstraints("10, 23, 1, 1, right, default"));
			}
			{
				this.checkOpenBrowser = new JCheckBox();
				this.panelBasicSettings.add(this.checkOpenBrowser, new CellConstraints("9, 22, 2, 1, default, default"));
				this.checkOpenBrowser.setText("Open browser when generated");
			}
			{
				this.separatorWindowSettings = new JSeparator();
				this.panelBasicSettings.add(this.separatorWindowSettings, new CellConstraints("8, 13, 3, 1, default, bottom"));
			}
			{
				this.labelGuiLanguage = new JLabel();
				this.panelBasicSettings.add(this.labelGuiLanguage, new CellConstraints("9, 24, 1, 1, default, default"));
				this.labelGuiLanguage.setText("Language of this widow");
				if (GuiTools.isLanguageNotSet()) {
					this.labelGuiLanguage.setForeground(new Color(255, 0, 0));
				}
			}
			{
				this.labelWindowSetting = new JLabel();
				this.panelBasicSettings.add(this.labelWindowSetting, new CellConstraints("8, 14, 3, 1, fill, top"));
				this.labelWindowSetting.setText("Settings bellow are used for this window");
				this.labelWindowSetting.setHorizontalAlignment(SwingConstants.CENTER);
				this.labelWindowSetting.setHorizontalTextPosition(SwingConstants.CENTER);
			}
			{
				this.checkAutoclose = new JCheckBox();
				this.panelBasicSettings.add(this.checkAutoclose, new CellConstraints("9, 21, 2, 1, default, default"));
				this.checkAutoclose.setText("Close window after generating");
			}
			{
				this.comboCheckNewVersion = new JComboBox();
				this.panelBasicSettings.add(this.comboCheckNewVersion, new CellConstraints("10, 19, 1, 1, default, default"));
			}
			{
				this.labelCheckNewVersion = new JLabel();
				this.labelCheckNewVersion.setText("Check new version");
				this.panelBasicSettings.add(this.labelCheckNewVersion, new CellConstraints("9, 19, 1, 1, default, default"));
			}
			{
				this.separatorPrivacy2 = new JSeparator();
				this.panelBasicSettings.add(this.separatorPrivacy2, new CellConstraints(5, 9, 1, 1, CellConstraints.FILL, CellConstraints.CENTER,
						new Insets(0, 0, 0, 12)));
			}
			{
				this.separatorPrivacy = new JSeparator();
				this.panelBasicSettings.add(this.separatorPrivacy, new CellConstraints("5, 2, 1, 11, center, fill"));
				this.separatorPrivacy.setOrientation(SwingConstants.VERTICAL);
			}
			{
				this.buttonCheckNow = new JButton();
				this.panelBasicSettings.add(this.buttonCheckNow, new CellConstraints("9, 20, 2, 1, default, default"));
				this.buttonCheckNow.setText("Check new version now!");
				this.buttonCheckNow.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						buttonCheckNowActionPerformed();
					}
				});
			}

			{
				// $hide>>$
				if (this.comboGuiLanguage.getSelectedItem() == null) {
					String[] availableGuiLanguages = MessagesTools.getGuiMessages().getAvailableLanguages().toArray(new String[0]);
					Arrays.sort(availableGuiLanguages);
					ComboBoxModel comboGuiLanguageModel = new DefaultComboBoxModel(availableGuiLanguages);
					this.comboGuiLanguage.setModel(comboGuiLanguageModel);

					for (int i = 0; i < this.comboGuiLanguage.getItemCount(); i++) {
						if (((String) this.comboGuiLanguage.getItemAt(i)).equals(GuiTools.getLang())) {
							this.comboGuiLanguage.setSelectedIndex(i);
							break;
						}
					}
				}
				// $hide<<$

				this.comboGuiLanguage.addItemListener(new ItemListener() {
					public void itemStateChanged(ItemEvent evt) {
						comboGuiLanguageItemStateChanged(evt);
					}
				});
			}

			// $hide>>$
			prepareListOfAllCheckboxes();
			this.privacyProfilesManager.prepareListOfAllPrivacyCombos();

			initializeComponent();
			localizeForm();

			long lastCheck = Long.parseLong(GuiTools.getLastUsed(Constants.PROPERTY_LAST_CHECK, "0"));
			long now = new GregorianCalendar().getTimeInMillis();
			long secondsToWait = ((CheckVersionItemBean) this.comboCheckNewVersion.getSelectedItem()).getSeconds();

			if (secondsToWait > 0 && (now - lastCheck) / 1000 > secondsToWait) {
				checkForNewVersion(false);
				GuiTools.setLastUsed(Constants.PROPERTY_LAST_CHECK, Long.toString(now));
			}
			// $hide<<$

			pack();
			this.setSize(797, 718);

			// dpi problem solving:
			JFrame blah = new JFrame();
			blah.pack();
			this.insetsDimensions = blah.getInsets();
			blah = null;

			Dimension d = getContentPane().getMinimumSize();
			d.height += this.insetsDimensions.top + this.insetsDimensions.bottom + 8;
			d.width += this.insetsDimensions.left + this.insetsDimensions.right - 3;
			setMinimumSize(d);
			this.setIconImage(new ImageIcon(getClass().getClassLoader().getResource("icon.png")).getImage());
			// end
		} catch (Exception e) {
			log.error("Error initialize GUI window", e);
		}
	}

	/**
	 * Creates list of all checkboxes
	 */
	private void prepareListOfAllCheckboxes() {
		this.checkboxes = new ArrayList<CheckboxBean>();
		addCheckboxBean(this.checkInfoAllowed, PROPERTY_SHOW_INFO_ALLOWED, "check.showInfoAllowed", true);
		addCheckboxBean(this.checkInfoBlocked, PROPERTY_SHOW_INFO_BLOCKED, "check.showInfoBlocked", true);
		addCheckboxBean(this.checkShowId, PROPERTY_SHOW_ID, "check.showId", false);
		addCheckboxBean(this.checkMaidenName, "useMaidenName", "check.maidenNames", true);
		addCheckboxBean(this.checkUseBurialPlace, "useBurialPlace", "check.useBurialPlace", true);
		addCheckboxBean(this.checkBlockHidden, PROPERTY_BLOCK_HIDDEN, "check.blockHidden", false);
		addCheckboxBean(this.checkShowIdInLists, PROPERTY_SHOW_ID_IN_LISTS, "check.showIdInLists", false);
		addCheckboxBean(this.checkShowBirthInLists, PROPERTY_SHOW_BIRTH_IN_LISTS, "check.showBirthInLists", true);
		addCheckboxBean(this.checkShowDeathInLists, PROPERTY_SHOW_DEATH_IN_LISTS, "check.showDeathInLists", true);
		addCheckboxBean(this.checkShowShortDescInList, PROPERTY_SHOW_SHORT_DESC_IN_LISTS, "check.showShortDescInLists", false);
		addCheckboxBean(this.checkShowWeddingNameInList, PROPERTY_SHOW_WEDDING_NAME_IN_LISTS, "check.showWeddingNameInLists", true);
		addCheckboxBean(this.checkAutoclose, PROPERTY_AUTOCLOSE, "check.autoclose", false);
		addCheckboxBean(this.checkOpenBrowser, PROPERTY_OPEN_BROWSER, "check.openBrowser", true);
		addCheckboxBean(this.checkDebug, PROPERTY_ENABLE_DEBUG, "check.enableDebug", false);
		addCheckboxBean(this.checkHidePath, PROPERTY_HIDE_PATH_TO_MAIN_PERSON, "check.hidePath", false);
		addCheckboxBean(this.checkHideAdditionalInfo, PROPERTY_HIDE_ADDITIONAL_INFO_ON_INDEX, "check.hideAdditionalInfoOnIndex", false);
		addCheckboxBean(this.checkShowMainPerson, PROPERTY_SHOW_MAIN_PERSON, "check.showMainPerson", true);
	}

	/**
	 * Adds checkbox to list of all checkboxes
	 * 
	 * @param checkBox Checkbox to add
	 * @param propertyKey Property key to store value of checkbox
	 * @param localizationKey Localization key for localization checkbox
	 * @param defaultValue Default value, in case of first run
	 */
	private void addCheckboxBean(JCheckBox checkBox, String propertyKey, String localizationKey, boolean defaultValue) {
		this.checkboxes.add(new CheckboxBean(checkBox, propertyKey, localizationKey, defaultValue));
	}

	/**
	 * Initializes all components of form
	 */
	private void initializeComponent() {
		// $hide>>$

		this.privacyProfilesManager.initPrivacyCombos();

		{
			ComboBoxModel comboCheckNewVersionModel = new DefaultComboBoxModel(new CheckVersionItemBean[] {
					new CheckVersionItemBean(GuiTools.getLocalized("combo.checkNewVersion.daily"), 60 * 60 * 24),
					new CheckVersionItemBean(GuiTools.getLocalized("combo.checkNewVersion.weekly"), 7 * 60 * 60 * 24),
					new CheckVersionItemBean(GuiTools.getLocalized("combo.checkNewVersion.monthly"), 30 * 60 * 60 * 24),
					new CheckVersionItemBean(GuiTools.getLocalized("combo.checkNewVersion.never"), 0) });
			this.comboCheckNewVersion.setModel(comboCheckNewVersionModel);
			this.comboCheckNewVersion.setRenderer(new ComboCheckNewVersionRenderer());

			for (int i = 0; i < this.comboCheckNewVersion.getItemCount(); i++) {
				if (Long.toString(((CheckVersionItemBean) this.comboCheckNewVersion.getItemAt(i)).getSeconds()).equals(
						GuiTools.getLastUsed(Constants.PROPERTY_CHECK_NEW_VERSION, "60 * 60 * 24"))) {
					this.comboCheckNewVersion.setSelectedIndex(i);
					break;
				}
			}
		}
		{
			String[] selected = GuiTools.getLastUsed(Constants.PROPERTY_LANGUAGES, "cz").split(";");
			String[] availableLanguages = GuiTools.getAvailableLanguages();

			ListModel listLanguagesModel = new DefaultComboBoxModel(availableLanguages);
			this.listLanguages.setModel(listLanguagesModel);

			List<String> allLAnguages = Arrays.asList(availableLanguages);
			int[] selectedIndices = new int[selected.length];
			for (int i = 0; i < selected.length; i++) {
				selectedIndices[i] = allLAnguages.indexOf(selected[i]);
			}
			this.listLanguages.setSelectedIndices(selectedIndices);

			ComboBoxModel comboDefaultLanguageModel = new DefaultComboBoxModel(selected);
			this.comboDefaultLanguage.setModel(comboDefaultLanguageModel);
			this.comboDefaultLanguage.setSelectedItem(GuiTools.getLastUsed(Constants.PROPERTY_LANGUAGES, "cz"));
		}
		{
			ListCellRenderer comboMapsRenderer = new ComboMapsRenderer();
			ComboBoxModel comboMapsModel = new DefaultComboBoxModel(GuiTools.getMaps().toArray());
			this.comboMaps.setModel(comboMapsModel);
			this.comboMaps.setRenderer(comboMapsRenderer);

			for (int i = 0; i < this.comboMaps.getItemCount(); i++) {
				if (((MapsProviderItemBean) this.comboMaps.getItemAt(i)).getId().toString().equals(
						GuiTools.getLastUsed(Constants.PROPERTY_MAPS, "0"))) {
					this.comboMaps.setSelectedIndex(i);
					break;
				}
			}
		}
		{
			List<String> items = new ArrayList<String>();
			Collections.addAll(items, "320x240", "320x-", "640x480", "640x-", "800x600", "800x-", "1024x768", "1024x-");
			String lastUsed = GuiTools.getLastUsed(Constants.PROPERTY_MAX_SIZE_PHOTO, "800x600");
			if (!items.contains(lastUsed)) {
				items.add(0, lastUsed);
			}
			ComboBoxModel comboPictureResolutionModel = new DefaultComboBoxModel(items.toArray());
			this.comboPictureResolution.setModel(comboPictureResolutionModel);
			this.comboPictureResolution.setSelectedItem(GuiTools.getLastUsed(Constants.PROPERTY_MAX_SIZE_PHOTO, "800x600"));
			this.comboPictureResolution.setEditable(true);
		}
		{
			this.privacyProfilesManager.initPrivacyProfilesCombo();
		}
		{
			this.templatesManager.initTemplatesCombo();
		}

		// load last values of all checkboxes
		for (CheckboxBean checkbox : this.checkboxes) {
			GuiTools.loadLastBooleanValue(checkbox.getCheckBox(), checkbox.getPropertyKey(), checkbox.getDefaultValue());
		}

		this.editOutputDir.setText(GuiTools.getLastUsedOutput());
		this.editRod.setText(GuiTools.getLastUsedRod());
		this.editAddressSplitter.setText(GuiTools.getLastUsed(Constants.PROPERTY_ADDRESS_SPLITTER, ","));
		this.editOnlyGroups.setText(GuiTools.getLastUsed(Constants.PROPERTY_GROUPS_ONLY, ""));
		this.editHideGroups.setText(GuiTools.getLastUsed(Constants.PROPERTY_GROUPS_HIDE, ""));
		this.editMainPerson.setText(GuiTools.getLastUsed(Constants.PROPERTY_MAIN_PERSON_ID, "0"));

		this.spinnerTownInBirth.setValue(GuiTools.getLastUsedTownPositionInBirth());
		this.spinnerTownInAddress.setValue(GuiTools.getLastUsedTownPositionInAddress());
		// $hide<<$
	}

	/**
	 * When select or deselect some language in list of available languages
	 * 
	 * @param evt Event
	 */
	protected void listLanguagesValueChanged(ListSelectionEvent evt) {
		// fill new items into default language combo
		this.comboDefaultLanguage.removeAllItems();
		for (Object selected : this.listLanguages.getSelectedValues()) {
			this.comboDefaultLanguage.addItem(selected);
		}
	}

	/**
	 * Localizes all components of form
	 */
	private void localizeForm() {
		// $hide>>$
		this.jTabbedPane.setTitleAt(0, GuiTools.getLocalized("panel.basicSettings"));
		this.jTabbedPane.setTitleAt(1, GuiTools.getLocalized("panel.additionalSettings"));
		this.jTabbedPane.setTitleAt(2, GuiTools.getLocalized("panel.imagesSettings"));
		this.jTabbedPane.setTitleAt(3, GuiTools.getLocalized("panel.placesSettings"));

		this.buttonDeleteProfile.setText(GuiTools.getLocalized("button.deleteProfile"));
		this.buttonAddTemplate.setText(GuiTools.getLocalized("button.addTemplate"));
		this.buttonSaveProfile.setText(GuiTools.getLocalized("button.addProfile"));
		this.buttonGenerate.setText(GuiTools.getLocalized("button.generate"));
		this.buttonCheckNow.setText(GuiTools.getLocalized("button.checkNow"));

		GuiTools.localizeLabel(this.labelTemplate, "label.template");
		GuiTools.localizeLabel(this.labelOutputDir, "label.outputDir");
		GuiTools.localizeLabel(this.labelHideRegistryInfo, "label.hideRegistryInfo");
		GuiTools.localizeLabel(this.labelHideUserDefined, "label.hideUserDefined");
		GuiTools.localizeLabel(this.labelHideNationality, "label.hideNationality");
		GuiTools.localizeLabel(this.labelHideReligion, "label.hideReligion");
		GuiTools.localizeLabel(this.labelHideWebpages, "label.hideWebpages");
		GuiTools.localizeLabel(this.labelHideContact, "label.hideContact");
		GuiTools.localizeLabel(this.labelHideEducation, "label.hideEducation");
		GuiTools.localizeLabel(this.labelProfile, "label.selectProfile");
		GuiTools.localizeLabel(this.labelHideBirth, "label.hideBirth");
		GuiTools.localizeLabel(this.labelHideDeath, "label.hideDeath");
		GuiTools.localizeLabel(this.labelHidePlaces, "label.hidePlaces");
		GuiTools.localizeLabel(this.labelHideJobs, "label.hideJobs");
		GuiTools.localizeLabel(this.labelHidePhotos, "label.hidePhotos");
		GuiTools.localizeLabel(this.labelHideHeads, "label.hideHeads");
		GuiTools.localizeLabel(this.labelRod, "label.rodFile");
		GuiTools.localizeLabel(this.labelCheckNewVersion, "label.checkNewVersion");
		GuiTools.localizeLabel(this.labelPreview, "label.preview");
		GuiTools.localizeLabel(this.labelAddressSplitter, "label.addressSplitter");
		GuiTools.localizeLabel(this.labelTownInAddress, "label.townInAddress");
		GuiTools.localizeLabel(this.labelTownInBirth, "label.townInBirth");
		GuiTools.localizeLabel(this.labelHideDocuments, "label.hideDocuments");
		GuiTools.localizeLabel(this.labelHideMarriageDate, "label.hideMarriageDate");
		GuiTools.localizeLabel(this.labelHideCredentials, "label.hideCredentials");
		GuiTools.localizeLabel(this.labelOnlyGroups, "label.onlyGroups");
		GuiTools.localizeLabel(this.labelHideGroups, "label.hideGroups");
		GuiTools.localizeLabel(this.labelHideMarriagePlace, "label.hideMarriagePlace");
		GuiTools.localizeLabel(this.labelHideAdditionalInfo, "label.hideAdditionalInfo");
		GuiTools.localizeLabel(this.labelPictureResolution, "label.pictureResolution");
		GuiTools.localizeLabel(this.labelLanguage, "label.language");
		GuiTools.localizeLabel(this.labelMainPerson, "label.mainPerson");
		GuiTools.localizeLabel(this.labelMaps, "label.maps");
		GuiTools.localizeLabel(this.labelGuiLanguage, "label.guiLanguage");
		GuiTools.localizeLabel(this.labelWindowSetting, "label.windowSettings");
		GuiTools.localizeLabel(this.labelDefaultLanguage, "label.defaultLanguage");

		// localize all checkboxes
		for (CheckboxBean checkbox : this.checkboxes) {
			GuiTools.localizeCheckbox(checkbox.getCheckBox(), checkbox.getLocalizationKey());
		}
		// $hide<<$
	}

	/**
	 * Stores values of components for next use
	 * 
	 * @return Properties with components status for next use
	 */
	public Properties prepareSettingsForNextUse() {
		// $hide>>$
		Properties properties = new Properties();

		GuiTools.setLastUsed(Constants.PROPERTY_GUI_LANGUAGE, (String) this.comboGuiLanguage.getSelectedItem());
		GuiTools.setLastUsed(Constants.PROPERTY_CHECK_NEW_VERSION, Long.toString(((CheckVersionItemBean) this.comboCheckNewVersion
				.getSelectedItem()).getSeconds()));

		GuiTools.setLastUsed("outputDir", this.editOutputDir.getText());
		GuiTools.setLastUsed("rodFile", this.editRod.getText());
		GuiTools.setLastUsed("template", ((TemplateItemBean) this.comboTemplate.getSelectedItem()).getTemplatePropertiesFile());
		GuiTools.setLastUsed("privacy", ((PrivacyProfileItemBean) this.comboPrivacyProfiles.getSelectedItem()).getPrivacyPropertiesFile());

		// store values of all checkboxes for next use
		for (CheckboxBean checkbox : this.checkboxes) {
			GuiTools.storeProperty(properties, checkbox.getPropertyKey(), checkbox.getCheckBox().isSelected());
		}

		GuiTools.storeProperty(properties, Constants.PROPERTY_TOWN_IN_ADDRESS, this.spinnerTownInAddress.getValue().toString());
		GuiTools.storeProperty(properties, Constants.PROPERTY_TOWN_IN_BIRTH, this.spinnerTownInBirth.getValue().toString());

		GuiTools.storeProperty(properties, Constants.PROPERTY_ADDRESS_SPLITTER, this.editAddressSplitter.getText());
		GuiTools.storeProperty(properties, Constants.PROPERTY_GROUPS_ONLY, this.editOnlyGroups.getText());
		GuiTools.storeProperty(properties, Constants.PROPERTY_GROUPS_HIDE, this.editHideGroups.getText());
		GuiTools.storeProperty(properties, Constants.PROPERTY_MAIN_PERSON_ID, this.editMainPerson.getText());

		GuiTools.storeProperty(properties, Constants.PROPERTY_MAX_SIZE_PHOTO, (String) this.comboPictureResolution.getSelectedItem());
		GuiTools.storeProperty(properties, Constants.PROPERTY_DEFAULT_LANGUAGE, this.comboDefaultLanguage.getSelectedItem().toString());
		GuiTools.storeProperty(properties, Constants.PROPERTY_MAPS, ((MapsProviderItemBean) this.comboMaps.getSelectedItem()).getId()
				.toString());
		GuiTools.storeProperty(properties, Constants.PROPERTY_LANGUAGES, GuiTools.getSelectedLanguagesAsString(this.listLanguages
				.getSelectedValues()));

		// set privacy properties only for generating (not for next use)
		this.privacyProfilesManager.storeAllPrivacyCombos(properties);

		return properties;
		// $hide<<$
	}

	/**
	 * Starts generating in new thread and shows process dialog window
	 * 
	 * @param properties Properties with user generation preferences
	 */
	private void startGenerationProcess(Properties properties) {
		// $hide>>$
		Global global = Global.getInstance();
		global.setProfileName(((PrivacyProfileItemBean) this.comboPrivacyProfiles.getSelectedItem()).getName());
		global.setTemplateName(((TemplateItemBean) this.comboTemplate.getSelectedItem()).getName());

		ProcessDialog processDialog = new ProcessDialog();
		processDialog.setModal(true);

		// creates new thread for generating
		// TODO allow absolute path to template properties
		this.generationThread = new Thread(new GenerationTask(properties, ".." + File.separator
				+ ((TemplateItemBean) this.comboTemplate.getSelectedItem()).getTemplatePropertiesFile(), processDialog));
		this.generationThread.start();

		processDialog.setLocation(this.getLocation().x + (getWidth() - processDialog.getWidth()) / 2, this.getLocation().y
				+ (getHeight() - processDialog.getHeight()) / 2);
		processDialog.setVisible(true);
		// $hide<<$
	}

	// getter and setters

	/**
	 * Gets the main window listener.
	 * 
	 * @return the main window listener
	 */
	public MainWindowListener getMainWindowListener() {
		return this.mainWindowListener;
	}

	/**
	 * @return the generationThread
	 */
	public Thread getGenerationThread() {
		return this.generationThread;
	}

	/**
	 * Gets the check autoclose.
	 * 
	 * @return the check autoclose
	 */
	public JCheckBox getCheckAutoclose() {
		return this.checkAutoclose;
	}

	/**
	 * Gets the check debug.
	 * 
	 * @return the check debug
	 */
	public JCheckBox getCheckDebug() {
		return this.checkDebug;
	}

	/**
	 * Gets the check open browser.
	 * 
	 * @return the check open browser
	 */
	public JCheckBox getCheckOpenBrowser() {
		return this.checkOpenBrowser;
	}

	/**
	 * @return the insetsDimensions
	 */
	public Insets getInsetsDimensions() {
		return this.insetsDimensions;
	}

}
