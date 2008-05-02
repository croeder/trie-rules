package de.fzi.ipe.trie.inference.executionTree.abductive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

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
	
	/**
	 * 
	 * @return a number indicating how well this element is grounded in the knowledge base. 
	 */
	public double kbGrounding() {
		double toReturn = 0;
		if (this instanceof ExecutionTreeAssumption) {
			toReturn = -2;
		}
		else if (this instanceof ExecutionTreeRule){
			toReturn = 2;
		}
		else if (this instanceof ExecutionTreeGoal){
			toReturn = 1;
		}
		for (ExecutionTreeElement elem: children) {
			toReturn += elem.kbGrounding(); 
		}
		return toReturn;
	}

	public void postprocess(VariableBindings vb) {
		for (ExecutionTreeElement element: getChildren()) {
			element.postprocess(vb);
		}
	}
}
