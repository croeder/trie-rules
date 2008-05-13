package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Term;

public class ProofVariable extends Term implements Comparable<ProofVariable> {
	
	static int counter=0;
	
	private int id; 
	private String variableName;
	
	public ProofVariable(String name) {
		id = counter ++;
		this.variableName = name;
	}
	
	/**
	 * The resets the counter used to generate unique variable names, this method must
	 * never be called during an inference process, or the inference process may
	 * return *any* result.
	 */
	public static void resetCounter() {
		counter = 0;
	}
	
	public String getVariableName() {
		return variableName;
	}
	
	public String toString() {
		return "?"+id;
	}

	public int compareTo(ProofVariable other) {
		return (id - other.id);
		
	}
	
	public boolean equals(Object other) {
		if (other instanceof ProofVariable) {
			return (id == ((ProofVariable)other).id);
		}
		else return false;
	}
	
	public int getId() {
		return id;
	}
	
	public int hashCode() {
		return id;
	}

	
	
}
