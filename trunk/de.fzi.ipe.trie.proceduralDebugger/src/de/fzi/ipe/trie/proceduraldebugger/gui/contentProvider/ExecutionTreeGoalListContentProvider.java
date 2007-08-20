package de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public class ExecutionTreeGoalListContentProvider implements IStructuredContentProvider {

	private List<ExecutionTreeGoal> goalStack;

	public Object[] getElements(Object inputElement) {
		if (goalStack != null) return goalStack.toArray();
		else return new Object[0];
	}

	public void dispose() {
		;
	}

	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		goalStack = (List<ExecutionTreeGoal>) newInput;
	}
	
	
}
