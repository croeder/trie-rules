package de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.inference.ProofVariable;
import de.fzi.ipe.trie.inference.VariableBindings;

public class VariableBindingsContentProvider implements IStructuredContentProvider {

	private VariableBindings vb;
	
	public Object[] getElements(Object inputElement) {
		if (vb == null) return new Object[0];
		else {
			Set<Entry<ProofVariable, GroundTerm>> entrySet = vb.getEntrySet();
			List<Entry<ProofVariable, GroundTerm>> tempList = new ArrayList<Entry<ProofVariable, GroundTerm>> (entrySet.size());
			tempList.addAll(entrySet);
			Collections.sort(tempList, new Comparator<Entry<ProofVariable, GroundTerm>>(){
				public int compare(Entry<ProofVariable, GroundTerm> e1, Entry<ProofVariable, GroundTerm> e2) {
					return e1.getKey().compareTo(e2.getKey());
				}});
			return tempList.toArray();
		}
	}

	public void dispose() {
		;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		vb = (VariableBindings) newInput;
	}

}
