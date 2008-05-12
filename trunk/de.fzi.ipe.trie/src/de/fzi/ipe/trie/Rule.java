package de.fzi.ipe.trie;

public interface Rule {

	//the default edit distance to be used when also retrieving rules that 
	//'almost' match
	public static final int DEFAULT_EDIT_DISTANCE = 3; 
	
	public String getName();
	
	public String getComment();
	
	public Atom[] getHead();
	
	public Atom[] getBody();
		
	
}
