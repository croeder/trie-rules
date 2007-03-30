package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.inference.Result;


public class BindingsTableContentProvider implements IStructuredContentProvider{
    
    private ResultLineProvider[] roots;
    
    
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     */
    public Object[] getElements(Object inputElement) {
        return roots;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     */
    public void dispose() {
        ;
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     */
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    	Result result = (Result) newInput;
    	if (result != null) {
    		roots = new ResultLineProvider[result.numberResults()];
    		for (int i=0;i<result.numberResults();i++) {
    			roots[i] = new ResultLineProvider(result,i);
    		}
    	}
    	else roots = new ResultLineProvider[0];
    }
    
    
    
    
    

}
