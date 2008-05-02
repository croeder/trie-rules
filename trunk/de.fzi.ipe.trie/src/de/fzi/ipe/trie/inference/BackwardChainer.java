package de.fzi.ipe.trie.inference;

import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;

/**
 * Interface to access backward chaining inference engines
 */
public interface BackwardChainer {

	public GoalStack getGoalStack();
	public List<ExecutionTreeGoal> getProofTrace();
	public VariableBindings getVariableBindings();	
	public ExecutionTreeQuery getExecutionTree();
	
	public Result hasProof(Atom[] goals);
	
}
