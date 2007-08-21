package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

/**
 * A suspender that does not suspend (can be used when no suspending of the inference engine is needed)
 * This is the default suspender when none is provided. 
 */
public final class NullSuspender implements Suspender{

	public final void performedAction(Action a, ExecutionTreeGoal goal, Rule r) {
		;
	}

}
