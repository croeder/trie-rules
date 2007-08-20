/**
 * 
 */
package de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.Rule;

public class RuleCollectionContentProvider implements IStructuredContentProvider {

	private Rule[] elements;
	
	public Object[] getElements(Object inputElement) {
		return elements;
	}

	public void dispose() {
		;
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			Collection<Rule> input = (Collection<Rule>) newInput;
			List<Rule> rules = new ArrayList<Rule>();
			rules.addAll(input);
			Collections.sort(rules,new Comparator<Rule>() {
	
				public int compare(Rule r1, Rule r2) {
					return r1.getName().compareTo(r2.getName());
				}
				
			});
			elements = rules.toArray(new Rule[0]);
		}
		else elements = null;
	} 
	
}