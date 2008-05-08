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
	
	public String toString() {
		StringBuilder toReturn = new StringBuilder();
		toReturn.append("Result "+i+": ");
		for (Variable v:getVariables()) {
			toReturn.append(v+"="+getBinding(v));
			toReturn.append(", ");
		}
		return toReturn.toString();
	}
	
	public boolean sameBindings (ResultLineProvider other) {
		for (Variable v: getVariables()) {
			GroundTerm term1 = getBinding(v);
			GroundTerm term2 = other.getBinding(v);
			if (term1 != term2) {
				if (term1 != null && term2 != null && term1.equals(term2)) continue;
				else return false;
			}
		}
		return true;
	}
	
	
}
