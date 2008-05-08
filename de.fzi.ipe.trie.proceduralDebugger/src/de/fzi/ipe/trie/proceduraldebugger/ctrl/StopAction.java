package de.fzi.ipe.trie.proceduraldebugger.ctrl;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.DebugLogger;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;


public class StopAction extends ButtonEnabledAction implements SuspendListener {

	private static StopAction instance = new StopAction();

	public static StopAction getInstance() {
		return instance;
	}
	
	public StopAction () {
		setText("Stop");
		setToolTipText("Stop the execution of the inference engine");
		setEnabled(false);
		ReasoningAccess.getSuspender().addListener(this);
	}
	
	@Override
	public void run() {
		DebugLogger.log("Stop");
		ReasoningAccess.stopDebugging();
		StepAction.getInstance().setEnabled(false);
		JumpAction.getInstance().setEnabled(false);
		setEnabled(false);
	}

	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		if (a == Suspender.Action.END) {
			StepAction.getInstance().setEnabled(false);
			JumpAction.getInstance().setEnabled(false);
			setEnabled(false);
		}
	}

	public void waking() {
		;
	}
	
	

}
