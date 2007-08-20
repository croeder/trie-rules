/**
 * 
 */
package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import org.eclipse.jface.viewers.LabelProvider;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public class ExecutionTreeGoalLabelProvider extends LabelProvider {

	public String getText(Object element) {
		if (element instanceof ExecutionTreeGoal) {
			ExecutionTreeGoal goal = (ExecutionTreeGoal) element;
			return goal.toString();
		}
		else return "GoalLabelProvider: Unexpected Element";
	}

}