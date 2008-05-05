package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.RuleProxy;

/**
 * Used to represent a execution tree rule that was created from 
 * an 'almost' match between the rule head and the goal
 *
 */
public interface ExecutionTreeRuleAssumption extends ExecutionTreeRule{

	public RuleProxy getRule();
	
	public Atom getGoal();
	
}
