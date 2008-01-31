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
	private int begin;


	public TextPartWord(String text) {
		this.text = text;
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
		return null;
	}


	public void setToolTipText(String tooltip) {
		this.tooltip = tooltip;
	}

	public String getToolTipText() {
		return tooltip;
	}

	public void clicked() {
		;
	}

	public int getLength() {
		return text.length();
	}
	
	public void deregister() {
		;
	}

}
