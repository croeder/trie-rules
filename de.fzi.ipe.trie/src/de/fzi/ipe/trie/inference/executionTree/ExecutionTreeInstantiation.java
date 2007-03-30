package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;


public abstract class ExecutionTreeInstantiation extends ExecutionTreeElement{
	
	protected Atom[] body;
	
	public Atom[] getBody() {
		return body;
	}

	public void create(KnowledgeBase kb, GoalStack goals) {
		for (int i=0;i<body.length;i++) {
			ExecutionTreeGoal goal = new ExecutionTreeGoal(body[i]);
			goal.setParent(this);
			this.addChild(goal);
		}
		goals.add(getChildren());
	}

	
}
