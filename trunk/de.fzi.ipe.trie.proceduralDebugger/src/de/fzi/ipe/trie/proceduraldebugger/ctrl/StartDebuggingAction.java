package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class StartDebuggingAction extends ButtonEnabledAction {

	private static StartDebuggingAction instance = new StartDebuggingAction();

	public static StartDebuggingAction getInstance() {
		return instance;
	}
	
	
	public StartDebuggingAction () {
		setText("Start Debugging");
		setShortText("Start");
		setToolTipText("Start Debugging");
		setEnabled(false);
	}
	
	@Override
	public void run() {
		ReasoningAccess.startDebugging();
		StepAction.getInstance().setEnabled(true);
		StopAction.getInstance().setEnabled(true);
		JumpAction.getInstance().setEnabled(true);
	}
	
	
}
