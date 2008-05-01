package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.ProofVariable;

public interface ExecutionTreeRule extends ExecutionTreeInstantiation {

	public Rule getRule();
	
	public Atom getHead();
	
	public void replace(ProofVariable v1, ProofVariable v2);
	
}
