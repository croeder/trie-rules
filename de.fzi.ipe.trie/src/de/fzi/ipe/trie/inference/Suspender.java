package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public interface Suspender {

	public enum Action { TryingGoal } ;
	
	/**
	 * Method that stops the thread if the current state matches the conditions specified in the suspender. Restarts the thred on 
	 * actions by the user. */ 
	public void performedAction(Action a, ExecutionTreeGoal goal, Rule r);	
			
}
