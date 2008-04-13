/**
 * 
 */
package de.fzi.ipe.trie.filemanagement.gui;

import org.eclipse.jface.viewers.LabelProvider;

import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class FileLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof DebuggerFile) return ((DebuggerFile)element).getName();
		else return super.getText(element);
	}
	
}