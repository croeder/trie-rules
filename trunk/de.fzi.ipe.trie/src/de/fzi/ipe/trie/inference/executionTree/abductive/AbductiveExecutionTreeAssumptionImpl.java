package de.fzi.ipe.trie.inference.executionTree.abductive;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;

public class AbductiveExecutionTreeAssumptionImpl extends AbductiveExecutionTreeElementImpl implements ExecutionTreeAssumption{

	//the goal assumed to be true
	private Atom goal;
	
	protected AbductiveExecutionTreeAssumptionImpl(Atom goal, VariableBindings vb, AbductiveExecutionTreeGoalImpl parent) {
		this.goal = goal.clone();
		setParent(parent);
	}
	
	
	public Atom getGoal() {
		return goal;
	}

	public String toString() {
		return "Assume "+goal.toString();
	}
	

	
}
