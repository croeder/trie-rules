package de.fzi.ipe.trie.debugger.gui.bindings;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;


public class BindingsTableLabelProvider implements ITableLabelProvider{
    
    private String[] sortedVariables;
    
    
    public BindingsTableLabelProvider(String[] sortedVariables) {
        this.sortedVariables = sortedVariables;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
     */
    public Image getColumnImage(Object element, int columnIndex) {
        return null;
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
                    return DebugView.labelProvider.getLabel(currentNode.getBinding(variableName));
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
        ;        
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
        ;
    }

}
