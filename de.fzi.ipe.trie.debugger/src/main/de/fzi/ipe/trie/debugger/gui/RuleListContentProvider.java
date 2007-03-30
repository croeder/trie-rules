package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class RuleListContentProvider implements IStructuredContentProvider {

	    private DebuggerRule[] elements;
	    
	    public RuleListContentProvider(DebuggerRule[] elements) {
	        this.elements = elements;
	    }
	            
	    public Object[] getElements(Object inputElement) {
	        return elements;
	    }

	    public void dispose() {
	        ;
	    }

	    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	        ;
	    }

	
}
