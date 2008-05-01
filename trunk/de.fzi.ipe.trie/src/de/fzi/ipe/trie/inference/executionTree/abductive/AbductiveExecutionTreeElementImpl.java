package de.fzi.ipe.trie.inference.executionTree.abductive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;

public class AbductiveExecutionTreeElementImpl implements ExecutionTreeElement{

	private List<ExecutionTreeElement> children= new ArrayList<ExecutionTreeElement>();
	private AbductiveExecutionTreeElementImpl parent;
		
	protected AbductiveExecutionTreeElementImpl() {
		;
	}
	
	protected void setParent(AbductiveExecutionTreeElementImpl parent) {
		this.parent = parent;
	}
	
	public AbductiveExecutionTreeElementImpl getParent() {
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
