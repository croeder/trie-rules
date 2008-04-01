package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;

public abstract class CustomButton extends TextPartWhitespace{
	
	private String tooltip;
	
	private Rectangle bounds;
	private Point upperLeftCorner;
	private Image image;
	
	CustomButton(Point upperLeftCorner, String tooltip) {
		super(3);
		this.tooltip = tooltip;
		this.upperLeftCorner = upperLeftCorner;
	} 
	
	void setImage(String imagePath) {
		image = DebuggerPlugin.getImage(imagePath);
		Rectangle imageBounds = image.getBounds();
		bounds = new Rectangle(upperLeftCorner.x, upperLeftCorner.y, imageBounds.width, imageBounds.height);
	}
	
	public void paint(GC gc) {
		gc.drawImage(image, upperLeftCorner.x, upperLeftCorner.y);
	}
	
	public boolean isPressed(Point clickLocation) {
		return bounds.contains(clickLocation);
	}

	public abstract void click();
	
	
	@Override
	public String getToolTipText() {
		return tooltip;
	}
	
	/**
	 * Called when an object of this type is removed from display, the object should
	 * deregister itself from all objects that know it. 
	 */
	public void deregister() {
		;
	}
	
}
