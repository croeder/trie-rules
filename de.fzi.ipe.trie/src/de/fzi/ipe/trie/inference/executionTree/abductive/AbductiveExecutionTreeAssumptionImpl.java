package de.fzi.ipe.trie.inference.executionTree.abductive;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.inference.ProofVariable;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;

public class AbductiveExecutionTreeAssumptionImpl extends AbductiveExecutionTreeElementImpl implements ExecutionTreeAssumption{

	//the goal assumed to be true
	private Atom goal;
	
	protected AbductiveExecutionTreeAssumptionImpl(Atom goal, AbductiveExecutionTreeGoalImpl parent) {
		this.goal = goal.clone();
		setParent(parent);
	}
	
	
	public Atom getGoal() {
		return goal;
	}

	public String toString() {
		return "Assume "+goal.toString();
	}

	public void postprocess(VariableBindings vb) {
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof ProofVariable) {
				ProofVariable var = (ProofVariable) goal.getTerm(i);
				GroundTerm term = vb.getVariableBinding(var);
				if (term != null) goal.replace(var, term);
			}
		}
		
		
	}
	
}
