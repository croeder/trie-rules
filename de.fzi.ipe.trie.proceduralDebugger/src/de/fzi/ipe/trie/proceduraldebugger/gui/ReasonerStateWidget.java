package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.SimpleBackwardChainer;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;

public abstract class ReasonerStateWidget implements SuspendListener{

	SimpleBackwardChainer reasoner; 
	Group group;
	
	public ReasonerStateWidget(Composite parent,String title, int height) {
		group = new Group(parent, SWT.NONE);		
		formatGroup(title,height);
		ReasoningAccess.getSuspender().addListener(this);
		reasoner = ReasoningAccess.getReasoner();
	}
	
	public abstract void changedReasoner();
	public abstract void refresh();
	
	private void formatGroup(String title, int height) {
		group.setText(title);
		group.setLayout(new FillLayout());

		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.heightHint = height;
		group.setLayoutData(layoutData);
	}
	
	public GridData getLayoutData() {
		return (GridData) group.getLayoutData();
	}
	
	
	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		if (reasoner != ReasoningAccess.getReasoner()) {
			reasoner = ReasoningAccess.getReasoner();
			changedReasoner();
		}
		refresh();
	}
	
	public void waking() {
		if (reasoner != ReasoningAccess.getReasoner()) {
			reasoner = ReasoningAccess.getReasoner();
			changedReasoner();
		}
		refresh();
	}

	
}
