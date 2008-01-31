package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.StyleRange;

public class OffsetIndex {
	
	private int currentOffset = 0;
	private ArrayList<TextPart> textParts = new ArrayList<TextPart>(1000);
	
	public void add(TextPart textPart) {
		textPart.setOffset(currentOffset);
		textParts.ensureCapacity(textParts.size()+textPart.getLength());
		for (int i=0;i<textPart.getLength();i++) {
			textParts.add(currentOffset, textPart);
			currentOffset++;
		}
	}

	public void clear() {
		for (TextPart tp:textParts) tp.deregister();
		textParts.clear();
		currentOffset = 0;
	}
	
	public String getText() {
		StringBuilder builder = new StringBuilder();
		TextPart current = null;
		for (int i=0;i<currentOffset;i++) {
			if (textParts.get(i) != current) {
				current = textParts.get(i);
				builder.append(current.getText());
			}
		}
		return builder.toString();
	}
	
	public TextPart getTextPartAtOffset(int offset) {
		return textParts.get(offset);
	}
	
	public StyleRange[] getStyleRanges() {
		List<StyleRange> toReturn = new ArrayList<StyleRange>();
		TextPart lastTextPart = null;
		for (TextPart tp: textParts) {
			if (tp != lastTextPart) {
				StyleRange sr = tp.getStyleRange();
				if (sr != null) toReturn.add(tp.getStyleRange());
				lastTextPart = tp;
			}
		}
		return toReturn.toArray(new StyleRange[]{});
	}
	

}
