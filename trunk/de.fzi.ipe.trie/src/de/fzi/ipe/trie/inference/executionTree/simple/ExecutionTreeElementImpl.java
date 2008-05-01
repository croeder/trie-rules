package de.fzi.ipe.trie.inference.executionTree.simple;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
	
}
