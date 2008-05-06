package de.fzi.ipe.trie.inference.executionTree;

import java.util.List;

import de.fzi.ipe.trie.inference.VariableBindings;

public interface ExecutionTreeElement {

	
	public ExecutionTreeElement getParent();	
	
	public List<ExecutionTreeElement>  getChildren();
	
	/**
	 * 
	 * @return a number indicating how well this element is grounded in the knowledge base. 
	 */
	public GroundingNumbers kbGrounding();


	/**
	 * Postprocessing - used to update the variables in the assumptions in the execution tree. 
	 */
	public void postprocess(VariableBindings vb);

}
