package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import org.eclipse.swt.widgets.Shell;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.proceduraldebugger.DebugLogger;
import de.fzi.ipe.trie.proceduraldebugger.model.DatamodelAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class StartDebuggingAction extends ButtonEnabledAction {

	private static StartDebuggingAction instance;
	
	private Shell shell;
	
	
	public static StartDebuggingAction getInstance(Shell shell) {
		if (instance == null) {
			instance = new StartDebuggingAction(shell);
		}
		return instance;
	}
	
	
	private StartDebuggingAction (Shell shell) {
		this.shell = shell;
		setText("Start Debugging");
		setShortText("Start");
		setToolTipText("Start Debugging");
		setEnabled(true);
	}
	
	
	
	@Override
	public void run() {
		DebugLogger.log("StartDebugging");
		
		//try to find a rule named 'Query' 
		Rule rule = DatamodelAccess.getKnowledgeBase().getRuleBase().getRule("Query");
		if (rule != null) ReasoningAccess.setStartingPoint(rule);
		else SelectRuleAction.getInstance(shell).run();
		
		ReasoningAccess.startDebugging();
		StepAction.getInstance().setEnabled(true);
		StopAction.getInstance().setEnabled(true);
		JumpAction.getInstance().setEnabled(true);
	}
	
	
}
