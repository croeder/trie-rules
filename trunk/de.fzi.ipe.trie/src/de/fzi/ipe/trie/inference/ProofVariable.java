package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Term;

public class ProofVariable extends Term {
	
	static int counter=0;
	
	private int id; 
	private String variableName;
	
	public ProofVariable(String name) {
		id = counter ++;
		this.variableName = name;
	}
	
	public String getVariableName() {
		return variableName;
	}
	
	public String toString() {
		return "?"+id;
	}

}
