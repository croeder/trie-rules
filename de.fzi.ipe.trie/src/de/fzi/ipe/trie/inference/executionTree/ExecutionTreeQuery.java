package de.fzi.ipe.trie.inference.executionTree;

import java.util.Map;

import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.inference.ProofVariable;

public interface ExecutionTreeQuery extends ExecutionTreeInstantiation {	

	public Map<Variable,ProofVariable> getVariableMap();
	
	
}
