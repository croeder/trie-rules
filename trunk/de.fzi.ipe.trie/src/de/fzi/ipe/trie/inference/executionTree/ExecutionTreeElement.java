package de.fzi.ipe.trie.inference.executionTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExecutionTreeElement {

	private List<ExecutionTreeElement> children= new ArrayList<ExecutionTreeElement>();
	private ExecutionTreeElement parent;
		
	protected void setParent(ExecutionTreeElement parent) {
		this.parent = parent;
	}
	
	public ExecutionTreeElement getParent() {
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
