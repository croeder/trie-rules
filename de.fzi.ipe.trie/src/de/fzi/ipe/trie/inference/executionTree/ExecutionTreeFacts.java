package de.fzi.ipe.trie.inference.executionTree;

import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.VariableBindings;

public interface ExecutionTreeFacts extends ExecutionTreeElement {

	
	public Set<Atom> getFacts();
	
	public Atom getGoal();
	
	public boolean next(VariableBindings vb);
	
	public Atom getLastAtom();
	
}
