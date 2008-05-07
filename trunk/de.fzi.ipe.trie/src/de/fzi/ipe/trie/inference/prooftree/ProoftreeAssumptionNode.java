package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;

public class ProoftreeAssumptionNode extends ProoftreeNode{

	private Atom assumedGoal;
	
	public ProoftreeAssumptionNode(ExecutionTreeAssumption assumption) {
		super(assumption.toString());
		assumedGoal = assumption.getGoal();
	}
	
	public Atom getGoal() {
		return assumedGoal;
	}
	
	@Override
	public String getTooltip() {
		return "This fact is not in the knowledge base, \n"+
			   "adding it would make the shown proof \n"+
			   "possible. ";
	}
}
