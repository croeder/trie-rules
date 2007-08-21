package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;

import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.ExecutionTreeGoalListContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.ExecutionTreeElementLabelProvider;

public class GoalStackWidget extends ReasonerStateWidget{

	private ListViewer list;
	
	public GoalStackWidget(Composite parent) {
		super(parent,"Current Goals", 150);
		list = new ListViewer(group);
		list.setContentProvider(new ExecutionTreeGoalListContentProvider());
		list.setLabelProvider(new ExecutionTreeElementLabelProvider());
	}

	public void refresh() {
		list.refresh();
	}
	
	@Override
	public void changedReasoner() {
		list.setInput(reasoner.getGoalStack());		
	}
	
	
}
