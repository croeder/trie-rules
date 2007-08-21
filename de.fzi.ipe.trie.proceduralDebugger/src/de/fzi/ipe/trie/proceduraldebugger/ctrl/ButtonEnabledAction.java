package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Simple utility class to aid the creation of buttons from actions. 
 */
public abstract class ButtonEnabledAction extends Action implements SelectionListener, DisposeListener {

	private Image image;
	private Collection<Button> buttons = new HashSet<Button>();
	
	private String shortText;
	
	public String getShortText() {
		return shortText;
	}
	
	public void setShortText(String shortText) {
		this.shortText = shortText;
	}

	public abstract void run();
	
	public Image getImage() {
		if (image != null) return image;
		else {
			if (getImageDescriptor() != null) {
				image = getImageDescriptor().createImage();
				return image;
			}
			else return null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		if (image != null) image.dispose();
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		for (Button b: buttons) b.setEnabled(enabled);
	}
	
	public void widgetDefaultSelected(SelectionEvent e) {
		;
	}

	public void widgetSelected(SelectionEvent e) {
		run();
	}
	
	public void widgetDisposed(DisposeEvent e) {
		buttons.remove((Button)e.getSource());
	}

	public Button createButton(Composite parent) {
		Button button = new Button(parent,getStyle()|SWT.WRAP);
		if (getShortText() == null) button.setText(getText());
		else button.setText(getShortText());
		button.setToolTipText(getToolTipText());
		button.setEnabled(this.isEnabled());
		if (getImageDescriptor() != null) button.setImage(getImage());
		button.addSelectionListener(this);
		button.addDisposeListener(this);
		buttons.add(button);
		return button;
	}
	
}
