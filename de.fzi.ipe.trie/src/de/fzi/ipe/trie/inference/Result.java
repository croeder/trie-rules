package de.fzi.ipe.trie.inference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;

public class Result {
	
	private Map<String,Variable> nameToVariable = new HashMap<String,Variable>();
	private Map<Variable,Integer> variableToIndex = new HashMap<Variable,Integer>();
	private GroundTerm[][] results;
	private List<Prooftree> prooftrees;
	
	
	public Result(ExecutionTreeQuery query, List<VariableBindings> result, List<Prooftree> prooftrees) {
		Set<Variable> variables = query.getVariableMap().keySet();
		int i=0;
		for (Variable v:variables) {
			nameToVariable.put(v.getName(), v);
			variableToIndex.put(v,i);
			i++;
		}
		results = new GroundTerm[result.size()][i];
		for (int x=0;x<result.size();x++) {
			for (Variable v:variableToIndex.keySet()) {
				ProofVariable pv = query.getVariableMap().get(v);
				GroundTerm value = result.get(x).getVariableBinding(pv);
				results[x][variableToIndex.get(v)] = value;
			}
		}
		this.prooftrees = prooftrees;
	}
	
	public String toString() {
		List<String> variables = getSortedVariableNames();
		
		StringBuilder builder = new StringBuilder();
		for (String name: variables) {
			builder.append(name);
			builder.append("; ");
		}
		builder.append("\n");
		
		for (int x=0;x<results.length;x++) {
			for (String name:variables) {
				builder.append(getBinding(name, x));
				builder.append("; ");
			}
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public List<String> getSortedVariableNames() {
		List<String> variables = new ArrayList<String>();
		variables.addAll(nameToVariable.keySet());
		Collections.sort(variables);
		return variables;
	}
	
	public Prooftree getProoftree(int resultLine) {
		return prooftrees.get(resultLine);
	}
	
	public boolean isEmpty() {
		return results.length == 0;
	}
	
	public int numberResults() {
		return results.length;
	}
	
	public Set<Variable> getVariables() {
		return variableToIndex.keySet();
	}
	
	public GroundTerm getBinding(Variable var, int resultLine) {
		return results[resultLine][variableToIndex.get(var)];
	}
	
	public GroundTerm getBinding(String varName, int resultLine) {
		return getBinding(nameToVariable.get(varName),resultLine);
	}
	
	
	
}
