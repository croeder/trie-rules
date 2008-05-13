package de.fzi.ipe.trie.inference.executionTree.abductive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRuleAssumption;
import de.fzi.ipe.trie.inference.executionTree.GroundingNumbers;

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
	 * @return a tuple with two number, the first being the number of all elements 
	 * in the tree, the second the number of non-assumptions
	 */
	public GroundingNumbers kbGrounding() {
		GroundingNumbers toReturn; 
		if (this instanceof ExecutionTreeGoal) {
			toReturn = new GroundingNumbers(2,2);
			ExecutionTreeGoal goal = (ExecutionTreeGoal) this;
			ExecutionTreeElement current = goal.getCurrentlyProcessed();
			if (current != null) toReturn = GroundingNumbers.add(goal.getCurrentlyProcessed().kbGrounding(),toReturn);
		}
		else {
			if (this instanceof ExecutionTreeAssumption) {
				toReturn = new GroundingNumbers(2,0);
			}
			else if (this instanceof ExecutionTreeRuleAssumption) {
				toReturn = new GroundingNumbers(1,0);
			}
			else if (this instanceof ExecutionTreeRule){
				toReturn = new GroundingNumbers(2,2);
			}
			else toReturn = new GroundingNumbers(0,0);
			for (ExecutionTreeElement elem: children) {
				toReturn = GroundingNumbers.add(toReturn, elem.kbGrounding()); 
			}
		}
		return toReturn;
	}


}
