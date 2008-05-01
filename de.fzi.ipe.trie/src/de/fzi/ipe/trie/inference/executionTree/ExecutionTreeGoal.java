package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.VariableBindings;

public interface ExecutionTreeGoal extends ExecutionTreeElement {

	public Atom getGoal();
	
	public boolean proof(GoalStack stack, VariableBindings vb, KnowledgeBase kb, Suspender suspender );
	
	public ExecutionTreeElement getCurrentlyProcessed();
	
	public void backtrack(VariableBindings vb,GoalStack stack);
	
}
