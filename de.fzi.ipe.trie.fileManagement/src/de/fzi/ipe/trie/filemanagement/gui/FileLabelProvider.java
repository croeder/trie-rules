/**
 * 
 */
package de.fzi.ipe.trie.filemanagement.gui;

import java.io.File;

import org.eclipse.jface.viewers.LabelProvider;

public class FileLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof File) return ((File)element).getName();
		else return super.getText(element);
	}
			
}