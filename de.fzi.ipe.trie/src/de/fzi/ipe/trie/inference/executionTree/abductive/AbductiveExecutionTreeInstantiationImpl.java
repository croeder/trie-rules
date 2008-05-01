package de.fzi.ipe.trie.inference.executionTree.abductive;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeInstantiation;


public abstract class AbductiveExecutionTreeInstantiationImpl extends AbductiveExecutionTreeElementImpl implements ExecutionTreeInstantiation{
	
	protected Atom[] body;
	
	public Atom[] getBody() {
		return body;
	}

	public void create(KnowledgeBase kb, GoalStack goals) {
		for (int i=0;i<body.length;i++) {
			AbductiveExecutionTreeGoalImpl goal = new AbductiveExecutionTreeGoalImpl(body[i]);
			goal.setParent(this);
			this.addChild(goal);
		}
		goals.add(getChildren());
	}

	
}
