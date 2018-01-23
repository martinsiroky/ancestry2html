package cz.sirsi.ancestry.gui.renderer;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.UIManager;

import cz.sirsi.ancestry.gui.bean.MapsProviderItemBean;

/**
 * Renders items of combo with maps provider
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class ComboMapsRenderer extends DefaultListCellRenderer {
	/**
	 * Constructs renderer instance
	 */
	public ComboMapsRenderer() {
		super();
		setOpaque(true);
	}

	/**
	 * @see javax.swing.DefaultListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	@Override
	public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected,
			final boolean cellHasFocus) {
		setText(((MapsProviderItemBean) value).getName());

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