package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class StartDebuggingAction extends ButtonEnabledAction {

	private static StartDebuggingAction instance = new StartDebuggingAction();

	public static StartDebuggingAction getInstance() {
		return instance;
	}
	
	
	public StartDebuggingAction () {
		this.setText("2> Start Debugging");
		this.setEnabled(false);
	}
	
	@Override
	public void run() {
		ReasoningAccess.startDebugging();
	}
	
	
}
