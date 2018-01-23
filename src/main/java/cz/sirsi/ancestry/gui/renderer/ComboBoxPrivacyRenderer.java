package cz.sirsi.ancestry.gui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import cz.sirsi.ancestry.core.configuration.MessagesTools;
import cz.sirsi.ancestry.gui.bean.PrivacyProfileItemBean;

/**
 * Renders items in combo with privacy profiles
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class ComboBoxPrivacyRenderer extends DefaultListCellRenderer {
	/**
	 * Constructs renderer instance
	 */
	public ComboBoxPrivacyRenderer() {
		super();
		setOpaque(true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index,
			final boolean isSelected, final boolean cellHasFocus) {
		String keyOrName = ((PrivacyProfileItemBean) value).getName();

		setText(MessagesTools.getGuiMessages().getMessage(keyOrName, keyOrName, null, false));

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