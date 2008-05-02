package de.fzi.ipe.trie.inference.executionTree;

import java.util.List;

public interface ExecutionTreeElement {

	
	public ExecutionTreeElement getParent();	
	
	public List<ExecutionTreeElement>  getChildren();
	
	/**
	 * 
	 * @return a number indicating how well this element is grounded in the knowledge base. 
	 */
	public double kbGrounding();
}
