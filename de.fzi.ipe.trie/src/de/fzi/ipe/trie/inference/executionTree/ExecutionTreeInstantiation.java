package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;


public abstract interface ExecutionTreeInstantiation extends ExecutionTreeElement{
	
	public Atom[] getBody();
	
	public void create(KnowledgeBase kb, GoalStack goals);
	
}
