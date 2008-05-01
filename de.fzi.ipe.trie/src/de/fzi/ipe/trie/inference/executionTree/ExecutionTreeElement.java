package de.fzi.ipe.trie.inference.executionTree;

import java.util.List;

public interface ExecutionTreeElement {

	
	public ExecutionTreeElement getParent();	
	
	public List<ExecutionTreeElement>  getChildren();
	
}
