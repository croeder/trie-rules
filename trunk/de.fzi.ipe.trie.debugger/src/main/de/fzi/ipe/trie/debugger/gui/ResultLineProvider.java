package de.fzi.ipe.trie.debugger.gui;

import java.util.Set;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;

public class ResultLineProvider {

	private int i;
	private Result result;
	
	
	public ResultLineProvider(Result result, int line) {
		this.i = line;
		this.result = result;
	}


	public GroundTerm getBinding(String varName) {
		return result.getBinding(varName, i);
	}


	public GroundTerm getBinding(Variable var) {
		return result.getBinding(var, i);
	}


	public Prooftree getProoftree() {
		return result.getProoftree(i);
	}

	public boolean basedOnAssumption() {
		return result.getProoftree(i).getGrounding()<1d;
	}

	public Set<Variable> getVariables() {
		return result.getVariables();
	}
	
	
	
	
}
