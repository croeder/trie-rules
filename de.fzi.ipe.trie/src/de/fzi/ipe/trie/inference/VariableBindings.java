package de.fzi.ipe.trie.inference;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;

public class VariableBindings {

	
	private Map<ProofVariable,GroundTerm> variableBindings = new HashMap<ProofVariable,GroundTerm>();
	private ArrayMap bindingLineage = new ArrayMap();
	
	
	public void addVariableBinding(ProofVariable var, GroundTerm term, ExecutionTreeElement elem) {
		GroundTerm oldTerm = variableBindings.put(var, term);
		assert(oldTerm == null || oldTerm.equals(term));
		bindingLineage.add(elem, var);
	}
	
	public GroundTerm getVariableBinding(ProofVariable var) {
		return variableBindings.get(var);
	}
	
	public Term getCurrentTerm(Term var) {
		if (var instanceof GroundTerm) {
			return var;
		}
		else {
			GroundTerm groundTerm = variableBindings.get(var);
			return groundTerm != null ? groundTerm : var;			
		}
	}
	
	public Set<Entry<ProofVariable, GroundTerm>> getEntrySet() {
		return variableBindings.entrySet();
	}
	
	public void removeBindings(ExecutionTreeElement elem) {
		ProofVariable[] vars = bindingLineage.removeVars(elem);
		for (ProofVariable v:vars) {
			variableBindings.remove(v);
		}
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (ProofVariable v:variableBindings.keySet()) {
			builder.append(v + "=>" + variableBindings.get(v));
			builder.append(" ,");
		}
		return builder.toString();
	}
	
	public VariableBindings clone() {
		VariableBindings toReturn = new VariableBindings();
		toReturn.variableBindings.putAll(variableBindings);
		toReturn.bindingLineage.bindingLineage.putAll(bindingLineage.bindingLineage);
		return toReturn;
	}
	
	private static class ArrayMap {
		
		final static ProofVariable[] EMPTY = new ProofVariable[0];
		Map<ExecutionTreeElement,ProofVariable[]> bindingLineage = new HashMap<ExecutionTreeElement,ProofVariable[]>();
		
		void add(ExecutionTreeElement element,ProofVariable var) {
			ProofVariable[] vars = bindingLineage.get(element);
			if (vars == null) {
				vars = new ProofVariable[1];
				vars[0] = var;
				bindingLineage.put(element, vars);
			}
			else {
				ProofVariable[] newVars = new ProofVariable[vars.length+1];
				for (int i=0;i<vars.length;i++) newVars[i] = vars[i];
				newVars[newVars.length-1] = var;
				bindingLineage.put(element, newVars);
			}
		}
		
		ProofVariable[] removeVars(ExecutionTreeElement elem) {
			ProofVariable[] vars = bindingLineage.remove(elem);
			return vars != null ? vars : EMPTY;
		}
		
	}

}
