package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.custom.StyleRange;

public class TextPartNewLine implements TextPart {

	int begin;
	
	public void clicked() {
		;
	}

	public int getLength() {
		return 1;
	}

	public StyleRange getStyleRange() {
		return null;
	}

	public String getText() {
		return "\n";
	}

	public String getToolTipText() {
		return null;
	}

	public void setOffset(int begin) {
		this.begin = begin;
	}

	public int getOffset() {
		return begin;
	}
	
}
