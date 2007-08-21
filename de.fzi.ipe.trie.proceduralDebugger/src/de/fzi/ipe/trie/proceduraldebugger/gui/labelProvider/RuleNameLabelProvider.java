/**
 * 
 */
package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import org.eclipse.jface.viewers.LabelProvider;

import de.fzi.ipe.trie.Rule;

public class RuleNameLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof Rule) {
			Rule rule = (Rule) element;
			return rule.getName();
		}
		else return "No Rule Object - Internatl Error";
	}
	
}