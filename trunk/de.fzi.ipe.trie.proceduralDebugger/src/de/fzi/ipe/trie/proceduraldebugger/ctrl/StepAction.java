package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.proceduraldebugger.DebugLogger;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class StepAction extends ButtonEnabledAction {

	private static StepAction instance = new StepAction();
	
	public static StepAction getInstance() {
		return instance;
	}
	
	private StepAction() {
		setText("Step");
		setToolTipText("Step inferencen engine");
		setEnabled(false);
	}
	
	@Override
	public void run() {
		DebugLogger.log("Step");
		ReasoningAccess.getSuspender().step();		
	}

}
