package de.fzi.ipe.trie;

public class Variable extends Term{
	
	private String name;
	
	public Variable(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof Variable)) return false;
		return name.equals(((Variable)other).name);
	}
	
	public int hashCode() {
		return name.hashCode();
	}
	
	public String toString() {
		return name;
	}

}
