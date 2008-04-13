package de.fzi.ipe.trie.filemanagement.gui;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class FileSetContentProvider implements IStructuredContentProvider {

	private Set<DebuggerFile> files;
	
	public Object[] getElements(Object inputElement) {
		if (files != null) return files.toArray();
		else return new Object[0];
	}

	public void dispose() {
		;
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		files = (Set<DebuggerFile>) newInput;		
	}

}
