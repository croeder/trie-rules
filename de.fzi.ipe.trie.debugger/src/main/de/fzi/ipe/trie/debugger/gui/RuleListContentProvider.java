package de.fzi.ipe.trie.debugger.gui;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class RuleListContentProvider implements IStructuredContentProvider {

	    private DebuggerRule[] elements;
	    private static final DebuggerRule[] EMPTY_LIST = new DebuggerRule[0];
	    
	    public RuleListContentProvider() {
	    }
	            
	    public Object[] getElements(Object inputElement) {
	        return elements;
	    }

	    public void dispose() {
	        ;
	    }

	    @SuppressWarnings("unchecked")
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	    	if (newInput == null) elements = EMPTY_LIST;
	    	else if (newInput instanceof Collection) {
	    		Collection<DebuggerRule> newInputSet = (Collection<DebuggerRule>) newInput;
	    		elements = newInputSet.toArray(EMPTY_LIST);
	    		Arrays.sort(elements, new RuleNameComparator());
	    	}
	    	else if (newInput instanceof DebuggerRule[]) {
	    		elements = (DebuggerRule[]) newInput;
	    	}
	    }


	    private static class RuleNameComparator implements Comparator<DebuggerRule> {

			public int compare(DebuggerRule r1, DebuggerRule r2) {
				return r1.getName().compareTo(r2.getName());
			}

	    	
	    }
	    
	    
	    
}
