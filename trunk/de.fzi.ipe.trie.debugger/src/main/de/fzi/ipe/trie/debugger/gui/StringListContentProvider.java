package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;


public class StringListContentProvider implements IStructuredContentProvider {

    private String[] elements;
    
    public StringListContentProvider(String[] elements) {
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
