package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;


/**
 * Interface that classes interested in suspender events can implements. 
 * The suspender ensures that all methods are only called from the UI Thread.
 * @author zach
 *
 */
public interface SuspendListener {
	
	public void suspending(Action a, ExecutionTreeGoal goal, Rule r);
	public void waking();

	
}
