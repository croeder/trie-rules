package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.inference.ProofVariable;



public class VariableBindingsTreeLabelProvider implements ITableLabelProvider{

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getColumnText(Object element, int columnIndex) {
		Entry<ProofVariable, GroundTerm> entry = (Entry<ProofVariable, GroundTerm>) element;
		if (columnIndex == 0) {
			return entry.getKey().toString();
		}
		else if (columnIndex == 1) {
			return entry.getValue().toString();
		}		
		else return null;
	}

	public void addListener(ILabelProviderListener listener) {
		;
	}

	public void dispose() {
		;
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		;
	}

	
	
	
}
