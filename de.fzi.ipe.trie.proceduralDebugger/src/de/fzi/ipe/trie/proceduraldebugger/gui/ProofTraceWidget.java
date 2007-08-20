package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.widgets.Composite;

import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.ExecutionTreeGoalListContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.ExecutionTreeGoalLabelProvider;

public class ProofTraceWidget extends ReasonerStateWidget{

	private ListViewer list;
	
	public ProofTraceWidget(Composite parent) {
		super(parent,"Proof Trace",150);
		list = new ListViewer(group);
		list.setContentProvider(new ExecutionTreeGoalListContentProvider());
		list.setLabelProvider(new ExecutionTreeGoalLabelProvider());
	}

	public void refresh() {
		list.refresh();
	}
	
	@Override
	public void changedReasoner() {
		list.setInput(reasoner.getProofTrace());		
	}
	
}
