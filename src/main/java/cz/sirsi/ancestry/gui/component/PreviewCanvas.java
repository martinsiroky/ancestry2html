package cz.sirsi.ancestry.gui.component;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

/**
 * Canvas for painting preview of selected template
 * 
 * @author msiroky
 */
@SuppressWarnings("serial")
public class PreviewCanvas extends Canvas {

	private Image image;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void paint(Graphics g) {
		if (this.image != null) {
			g.drawImage(this.image, 0, 0, this);
		}
	}

	/**
	 * Sets image for painting
	 * 
	 * @param image Image to painting
	 */
	public void setImage(Image image) {
		this.image = image.getScaledInstance(this.getWidth(), this.getHeight(), Image.SCALE_DEFAULT);
	}
}
