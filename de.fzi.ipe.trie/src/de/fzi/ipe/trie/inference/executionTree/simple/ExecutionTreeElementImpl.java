package de.fzi.ipe.trie.inference.executionTree.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;

public class ExecutionTreeElementImpl implements ExecutionTreeElement{

	private List<ExecutionTreeElement> children= new ArrayList<ExecutionTreeElement>();
	private ExecutionTreeElementImpl parent;
		
	protected ExecutionTreeElementImpl() {
		;
	}
	
	protected void setParent(ExecutionTreeElementImpl parent) {
		this.parent = parent;
	}
	
	public ExecutionTreeElementImpl getParent() {
		return parent;
	}
	
	protected void addChild(ExecutionTreeElement child) {
		children.add(child);
	}
	
	protected void clearChildren() {
		children.clear();
	}
	
	public List<ExecutionTreeElement>  getChildren() {
		return Collections.unmodifiableList(children);
	}
	
	/**
	 * 
	 * @return a number indicating how well this element is grounded in the knowledge base. 
	 */
	public double kbGrounding() {
		return Double.MAX_VALUE;
	}
	
	/**
	 * Postprocessing - used to update the variables in the assumptions in the execution tree. 
	 */
	public void postprocess(VariableBindings vb) {
		; //not needed in the case of the simple inference engine. 
	}

	
}