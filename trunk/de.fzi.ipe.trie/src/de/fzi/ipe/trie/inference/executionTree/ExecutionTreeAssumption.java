package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;


/**
 * Used to represent a goal that is simply assumed to be true. 
 *
 */
public interface ExecutionTreeAssumption extends ExecutionTreeElement{

	public Atom getGoal();
	
}
