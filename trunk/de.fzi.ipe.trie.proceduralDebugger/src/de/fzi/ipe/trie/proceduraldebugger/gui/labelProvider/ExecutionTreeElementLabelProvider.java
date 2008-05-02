/**
 * 
 */
package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProvider;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ExecutionTreeElementLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof ExecutionTreeGoal) {
			ExecutionTreeGoal goal = (ExecutionTreeGoal) element;
			return LabelUtil.toString(goal.getGoal());
		}
		else if (element instanceof ExecutionTreeRule) {
			ExecutionTreeRule rule = (ExecutionTreeRule) element;
			return rule.getRule().getName();
		}
		else if (element instanceof ExecutionTreeQuery) {
			return "Query";
		}
		else if (element instanceof Atom) {
			return LabelUtil.toString((Atom)element); 
		}
		else return "ExecutionTreeElementLabelProvider: Unexpected Element "+element.getClass();
	}

	
	@Override
	public void addListener(ILabelProviderListener listener) {
		LabelUtil.addListener(this, listener);
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		LabelUtil.removeListener(this, listener);
	}
	
}