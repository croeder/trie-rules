package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Term;

public class ProofVariable extends Term implements Comparable {
	
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

	public int compareTo(Object other) {
		return (id - ((ProofVariable)other).id);
		
	}
	
	public boolean equals(Object other) {
		if (other instanceof ProofVariable) {
			return (id == ((ProofVariable)other).id);
		}
		else return false;
	}
	
	public int hashCode() {
		return id;
	}

	
	
}
