package de.fzi.ipe.trie.debugger.gui.bindings;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;


public class BindingsTableLabelProvider implements ITableLabelProvider{
    
    private String[] sortedVariables;
    private Set<ILabelProviderListener> listeners = new HashSet<ILabelProviderListener>();
    
    public BindingsTableLabelProvider() {
    	;
    }

    public void setSortedVariables(String[] sortedVariables) {
    	this.sortedVariables = sortedVariables;
    	for (ILabelProviderListener l:listeners) {
    		l.labelProviderChanged(new LabelProviderChangedEvent(this));
    	}
    }
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        ResultLineProvider currentNode = (ResultLineProvider) element; 
        if (columnIndex == 0 && currentNode.basedOnAssumption()) {
        	return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_FOCUS);
        }
        else return null;
    }

    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
     */
    public String getColumnText(Object element, int columnIndex) {
        ResultLineProvider currentNode = (ResultLineProvider) element;
        if (sortedVariables != null) {
            if (sortedVariables.length>columnIndex) {
                String variableName = sortedVariables[columnIndex];
                if (currentNode != null) {
                	String columnText = DebugView.LABEL_PROVIDER.getLabel(currentNode.getBinding(variableName));
                	return (columnText != null) ? columnText : " ??? ";
                }
            }
            else {
                return "true";
            }
        }
        return ":-(";
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void addListener(ILabelProviderListener listener) {
        listeners.add(listener);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
     */
    public void dispose() {
        ;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object, java.lang.String)
     */
    public boolean isLabelProperty(Object element, String property) {
        return true;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
     */
    public void removeListener(ILabelProviderListener listener) {
        listeners.remove(listener);
    }

}
