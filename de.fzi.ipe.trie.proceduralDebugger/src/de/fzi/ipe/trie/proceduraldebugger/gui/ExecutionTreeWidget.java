package de.fzi.ipe.trie.proceduraldebugger.gui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.ExecutionTreeContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.ExecutionTreeElementLabelProvider;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class ExecutionTreeWidget extends ReasonerStateWidget implements ISelectionProvider{
	
	private TreeViewer tree;
	private SelectionEnforcer enforcer = new SelectionEnforcer();
	
	private Set<ISelectionChangedListener> listeners = new HashSet<ISelectionChangedListener>();
	
	
	public ExecutionTreeWidget(Composite parent, int height) {
		super(parent, "Execution Tree", height);
		tree = new TreeViewer(group);
		tree.setContentProvider(new ExecutionTreeContentProvider());
		tree.setLabelProvider(new ExecutionTreeElementLabelProvider());
		tree.addSelectionChangedListener(enforcer);
	}

	@Override
	public void changedReasoner() {
		tree.setInput(ReasoningAccess.getReasoner());
	}

	public void addSelectionChangedListener(ISelectionChangedListener list) {
		listeners.add(list);
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	public void setSelection(ISelection selection) {
		tree.setSelection(selection);
	}	

	public ISelection getSelection() {
		return tree.getSelection();
	}
	
	@Override
	public void refresh() {
		tree.refresh();
		tree.expandAll();
	}

	private void notifyListener(StructuredSelection sel) {
		for (ISelectionChangedListener lis: listeners) {
			lis.selectionChanged(new SelectionChangedEvent(this,sel));
		}
	}
	
	@Override
	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		super.suspending(a, goal, r);
		if (goal != null) { 
			StructuredSelection sel = new StructuredSelection(goal);
			tree.setSelection(sel,true);
			enforcer.currentSelection = sel;
			notifyListener(sel);
		}
	}

	/**
	 * A not so elegant class to enforce that the selection in the execution tree widget is not 
	 * changed by the user (because this would make the overall user interface inconsisttent)
	 */
	private class SelectionEnforcer implements ISelectionChangedListener {

		private ISelection currentSelection;
		
		public void selectionChanged(SelectionChangedEvent event) {
			ISelection selection = event.getSelection();
			if (currentSelection == selection) return;
			else if (currentSelection == null) return;
			else if (!currentSelection.equals(selection)) {
				tree.setSelection(currentSelection);
			}
			
		}
	
	}
	
	
}
