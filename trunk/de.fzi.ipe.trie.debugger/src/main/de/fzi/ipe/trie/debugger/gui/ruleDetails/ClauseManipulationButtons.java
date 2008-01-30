package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Point;


/**
 * Class managing the custom buttons for the ruleDetails group. 
 * @author zach
 *
 */
public class ClauseManipulationButtons implements PaintListener{
	
	private List<CustomButton> buttons = new ArrayList<CustomButton>();
	
	
	public void clearAll() {
		for (CustomButton b:buttons) b.deregister();
		buttons.clear();
	}
	
	public void add(CustomButton button) {
		buttons.add(button);
	}	
	
	/**
	 * @returns true, if the click was on a button
	 * 			false otherwise
	 */
	public boolean click(Point point) {
		for (CustomButton b:buttons) {
			if (b.isPressed(point)) {
				b.click();
				return true;
			}
		}
		return false;
	}

	public void paintControl(PaintEvent e) {
		for (CustomButton b:buttons) {
			b.paint(e.gc);
		}
	}

}
