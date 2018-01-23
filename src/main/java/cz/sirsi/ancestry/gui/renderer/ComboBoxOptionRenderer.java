package cz.sirsi.ancestry.gui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import cz.sirsi.ancestry.gui.bean.PrivacyItemBean;

/**
 * Renderer for option combo box
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class ComboBoxOptionRenderer extends DefaultListCellRenderer {
	/**
	 * Constructs renderer instance
	 */
	public ComboBoxOptionRenderer() {
		super();
		setOpaque(true);
	}

	/**
	 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		setText(((PrivacyItemBean) value).getText());

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);

		return this;
	}
}