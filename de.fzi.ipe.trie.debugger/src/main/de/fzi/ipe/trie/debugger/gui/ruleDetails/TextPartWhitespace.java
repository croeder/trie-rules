package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.custom.StyleRange;

public class TextPartWhitespace implements TextPart{

	private String text;
	private int offset; 
	
	public TextPartWhitespace(int length) {
		StringBuilder builder = new StringBuilder();
		for (int i=0;i<length;i++) {
			builder.append(" ");
		}
		text = builder.toString();
	}
	
	
	public void clicked() {
		;
	}

	public int getLength() {
		return text.length();
	}

	public StyleRange getStyleRange() {
		return null;
	}

	public String getText() {
		return text;
	}

	public String getToolTipText() {
		return null;
	}

	public void setOffset(int begin) {
		this.offset = begin;
	}
	
	public int getOffset() {
		return offset;
	}


	public void deregister() {
		;
	}

}
