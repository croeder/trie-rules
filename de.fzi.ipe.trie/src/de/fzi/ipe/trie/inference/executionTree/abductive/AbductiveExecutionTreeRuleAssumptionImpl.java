package de.fzi.ipe.trie.inference.executionTree.abductive;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.RuleProxy;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRuleAssumption;

public class AbductiveExecutionTreeRuleAssumptionImpl extends AbductiveExecutionTreeRuleImpl implements ExecutionTreeRuleAssumption{

	private Atom goal;
	
	/**
	 * Remember that a rule may have more than one head while a rule instantiation only has one!
	 */
	protected AbductiveExecutionTreeRuleAssumptionImpl(RuleProxy rule, Atom head, Atom goal) {
		super(rule,head);
		this.goal = goal;
	}

	public RuleProxy getRule() {
		return (RuleProxy) super.getRule();
	}
	
	public String toString() {
		return "RuleAssumption "+getRule().getName();
	}

	public Atom getGoal() {
		return goal;
	}
	

	
}
