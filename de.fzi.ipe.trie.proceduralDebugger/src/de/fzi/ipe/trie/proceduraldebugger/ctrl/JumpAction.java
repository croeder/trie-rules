package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.proceduraldebugger.DebugLogger;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class JumpAction extends ButtonEnabledAction {

	private static JumpAction instance = new JumpAction();
	
	public static JumpAction getInstance() {
		return instance;
	}
	
	private JumpAction() {
		setShortText("Jump");
		setToolTipText("Jump to the next goal of the same rule");
		setText("Jump");	
		setEnabled(false);
	}
	
	
	
	@Override
	public void run() {
		DebugLogger.log("Jump");
		ReasoningAccess.getSuspender().jump();
	}

}
