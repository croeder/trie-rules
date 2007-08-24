package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.ExecutionTreeContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.ExecutionTreeElementLabelProvider;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class ExecutionTreeWidget extends ReasonerStateWidget{
	
	private TreeViewer tree;

	public ExecutionTreeWidget(Composite parent, int height) {
		super(parent, "Execution Tree", height);
		tree = new TreeViewer(group);
		tree.setContentProvider(new ExecutionTreeContentProvider());
		tree.setLabelProvider(new ExecutionTreeElementLabelProvider());
	}

	@Override
	public void changedReasoner() {
		tree.setInput(ReasoningAccess.getReasoner());
	}

	public void addSelectionListener(ISelectionChangedListener list) {
		tree.addSelectionChangedListener(list);
	}
	
	@Override
	public void refresh() {
		tree.refresh();
		tree.expandAll();
	}

	@Override
	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		super.suspending(a, goal, r);
		if (goal != null) tree.setSelection(new StructuredSelection(goal),true);
	}
	
	
}
