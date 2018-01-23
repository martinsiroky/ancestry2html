package cz.sirsi.ancestry.gui.main;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import cz.sirsi.ancestry.gui.tools.GuiTools;

/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is
 * being used commercially (ie, by a corporation, company or business for any purpose whatever) then you should purchase a license for each
 * developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these licensing terms. A
 * COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL
 * PURPOSE.
 */
@SuppressWarnings("serial")
public class ProcessDialog extends javax.swing.JDialog {

	private JButton buttonCancel;

	private JButton buttonOk;
	private JLabel labelLeft;
	private JLabel labelLeftDesc;
	private JLabel labelPercent;
	private JProgressBar progressGenerate;
	private JScrollPane scrollPane;
	private JTextArea textDetails;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Instantiates a new process dialog.
	 */
	public ProcessDialog() {
		super(GuiForm.getInstance());
		initGUI();
	}

	/**
	 * Action for button Cancel
	 * 
	 * @param evt Not in use
	 */
	public void buttonCancelActionPerformed(ActionEvent evt) {
		// TODO solve cancelling
		GuiForm.getInstance().getGenerationThread().interrupt();
	}

	/**
	 * Action for button OK
	 * 
	 * @param evt Not in use
	 */
	public void buttonOkActionPerformed(ActionEvent evt) {
		this.setVisible(false);
	}

	/**
	 * Initializes GUI of form
	 */
	private void initGUI() {
		FormLayout thisLayout = new FormLayout("7dlu, 66dlu, 95dlu, 26dlu, max(p;15dlu), 19dlu",
				"max(p;5dlu), 16dlu, 9dlu, 12dlu, 8dlu, 17dlu, max(p;15dlu), 52dlu");
		getContentPane().setLayout(thisLayout);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		{
			this.textDetails = new JTextArea();
			this.textDetails.setFont(new java.awt.Font("Courier New", 0, 12));
			this.textDetails.setEditable(false);
			this.textDetails.setAutoscrolls(true);
		}
		{
			this.progressGenerate = new JProgressBar();
			this.progressGenerate.setValue(0);
			getContentPane().add(this.progressGenerate, new CellConstraints("2, 2, 3, 1, default, fill"));
		}
		{
			this.labelLeftDesc = new JLabel();
			getContentPane().add(this.labelLeftDesc, new CellConstraints("4, 4, 2, 1, default, default"));
			this.labelLeftDesc.setText("Left:");
			this.labelLeftDesc.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		{
			this.labelLeft = new JLabel();
			getContentPane().add(this.labelLeft, new CellConstraints("6, 4, 1, 1, default, default"));
			this.labelLeft.setText("? s");
			this.labelLeft.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		{
			this.labelPercent = new JLabel();
			getContentPane().add(this.labelPercent, new CellConstraints("6, 2, 1, 1, default, default"));
			this.labelPercent.setText("0%");
			this.labelPercent.setHorizontalAlignment(SwingConstants.RIGHT);
		}
		{
			this.buttonOk = new JButton();
			getContentPane().add(this.buttonOk, new CellConstraints("4, 6, 3, 1, default, fill"));
			this.buttonOk.setText("OK");
			this.buttonOk.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonOkActionPerformed(evt);
				}
			});
		}
		{
			this.scrollPane = new JScrollPane();
			getContentPane().add(this.scrollPane, new CellConstraints("2, 8, 5, 1, fill, fill"));
			this.scrollPane.setViewportView(this.textDetails);
			this.textDetails.setPreferredSize(new java.awt.Dimension(331, 72));
			this.textDetails.setLineWrap(true);
			this.textDetails.setWrapStyleWord(true);
		}
		{
			this.buttonCancel = new JButton();
			getContentPane().add(this.buttonCancel, new CellConstraints("2, 6, 1, 1, default, fill"));
			this.buttonCancel.setText("Storno");
			this.buttonCancel.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					buttonCancelActionPerformed(evt);
				}
			});
		}

		// $hide>>$
		localizeForm();
		// $hide<<$

		this.pack();
		this.setSize(362, 255);

		Dimension d = getContentPane().getMinimumSize();
		Insets insets = GuiForm.getInstance().getInsetsDimensions();
		d.height += insets.top + insets.bottom + 10;
		d.width += insets.left + insets.right + 10;
		this.setMinimumSize(d);
	}

	/**
	 * Localizes all components of form
	 */
	private void localizeForm() {
		// $hide>>$
		GuiTools.localizeLabel(this.labelLeftDesc, "process.label.leftDescription");

		this.buttonOk.setText(GuiTools.getLocalized("process.button.ok"));
		this.buttonCancel.setText(GuiTools.getLocalized("process.button.cancel"));
		// $hide<<$
	}

	// getters and setters

	/**
	 * @return the buttonCancel
	 */
	public JButton getButtonCancel() {
		return this.buttonCancel;
	}

	/**
	 * @return the buttonOk
	 */
	public JButton getButtonOk() {
		return this.buttonOk;
	}

	/**
	 * @return the labelLeft
	 */
	public JLabel getLabelLeft() {
		return this.labelLeft;
	}

	/**
	 * @return the labelPercent
	 */
	public JLabel getLabelPercent() {
		return this.labelPercent;
	}

	/**
	 * @return ProgressBar instance
	 */
	public JProgressBar getProgressGenerate() {
		return this.progressGenerate;
	}

	/**
	 * @return Text details component
	 */
	public JTextArea getTextDetails() {
		return this.textDetails;
	}
}
