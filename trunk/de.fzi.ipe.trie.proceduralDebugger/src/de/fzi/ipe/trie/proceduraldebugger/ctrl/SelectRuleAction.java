package de.fzi.ipe.trie.proceduraldebugger.ctrl;


import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.RuleCollectionContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.RuleNameLabelProvider;
import de.fzi.ipe.trie.proceduraldebugger.model.DatamodelAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class SelectRuleAction extends ButtonEnabledAction{

	private Shell shell;
	
	private static SelectRuleAction instance;
	
	
	public static SelectRuleAction getInstance(Shell shell) {
		if (instance == null) {
			instance = new SelectRuleAction(shell);
		}
		return instance;
	}
	
	private SelectRuleAction(Shell shell) {
		this.shell = shell;
		setShortText("Select");
		setToolTipText("Select Rule or Query");
		setText("Select Query");
	}

	@Override
	public void run() {
		ListDialog listDialog = new ListDialog(shell);
		listDialog.setBlockOnOpen(true);
		listDialog.setContentProvider(new RuleCollectionContentProvider());
		listDialog.setLabelProvider(new RuleNameLabelProvider());
		listDialog.setInput(DatamodelAccess.getKnowledgeBase().getRuleBase().getAllRules());
		listDialog.setTitle("Choose Starting Point");
		listDialog.setMessage("Please choose the rule or query that you want to use as starting point");
		int  code = listDialog.open();
		if ((code == Dialog.OK) && (listDialog.getResult().length > 0)) {
			ReasoningAccess.setStartingPoint((Rule) listDialog.getResult()[0]);
			StartDebuggingAction.getInstance().setEnabled(true);
		}
	}
	
}
