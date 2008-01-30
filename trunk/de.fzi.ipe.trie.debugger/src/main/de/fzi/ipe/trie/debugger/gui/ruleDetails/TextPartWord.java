package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.ISelectionListener;

public class TextPartWord implements TextPart {

	private String text, tooltip;
	private Color foreground;
	private Color background;
	private Set<SelectionListener> selectionListener = new HashSet<SelectionListener>();
	private int begin;

	public TextPartWord(String text, Color foreground, Color background) {
		this.text = text;
		setForeground(foreground);
		setBackground(background);
	}

	public TextPartWord(String text) {
		this(text, null, null);
	}

	public String getText() {
		return text;
	}

	public void setOffset(int begin) {
		this.begin = begin;
	}
	
	public int getOffset() {
		return begin;
	}

	public StyleRange getStyleRange() {
		StyleRange currentStyleRange = new StyleRange();
		currentStyleRange.start = begin;
		currentStyleRange.length = text.length();
		currentStyleRange.foreground = foreground;
		currentStyleRange.background = background;
		return currentStyleRange;
	}

	public void setForeground(Color foreground) {
		this.foreground = (foreground != null) ? foreground : COLOR_DEFAULT_FOREGROUND;
	}

	public void setBackground(Color background) {
		this.background = (background != null) ? background : COLOR_DEFAULT_BACKGROUND;
	}

	public void addSelectionListener(SelectionListener listener) {
		selectionListener.add(listener);
	}

	public void removeSelectionListener(ISelectionListener listener) {
		selectionListener.remove(listener);
	}

	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getToolTipText() {
		return tooltip;
	}

	public void clicked() {
		Iterator listeners = selectionListener.iterator();
		while (listeners.hasNext()) {
			SelectionListener current = (SelectionListener) listeners.next();
			current.widgetSelected(null);
		}
	}

	public int getLength() {
		return text.length();
	}

}
