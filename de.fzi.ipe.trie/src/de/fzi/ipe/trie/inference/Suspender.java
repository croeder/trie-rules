package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public class Suspender {

	public enum Action { CALLING_GOAL, RETRY_GOAL, SUCCESS, END, FAIL_GOAL, EXIT_GOAL, ADD_RULE_TO_EXECUTION_TREE, STOPPED} ;
	
	private volatile boolean toStop = false;
	
	public void stop() {
		toStop = true;
	}


	
	/**
	 * Method that stops the thread if the current state matches the conditions specified in the suspender. Restarts the thread on 
	 * actions by the user. */ 
	public void performedAction(Action a, ExecutionTreeGoal goal, Rule r) {
		if (toStop) {
			toStop = false;
			throw new HardTerminate();
		}
	}

	public static class HardTerminate extends RuntimeException {
		private static final long serialVersionUID = 1L;
		
		public HardTerminate() {}
	}

}
