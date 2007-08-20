package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class StepAction extends ButtonEnabledAction {

	private static StepAction instance = new StepAction();
	
	public static StepAction getInstance() {
		return instance;
	}
	
	private StepAction() {
		setText("Step");
	}
	
	@Override
	public void run() {
		ReasoningAccess.getSuspender().step();		
	}

}
