package de.fzi.ipe.trie.inference.executionTree;

import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Unification;
import de.fzi.ipe.trie.inference.VariableBindings;

public class ExecutionTreeGoal extends ExecutionTreeElement {

	private Atom goal;
	private boolean isPrepared = false;
	private int childIndex = 0;
	
	public ExecutionTreeGoal(Atom goal) {
		this.goal = goal;
	}
	
	public boolean proof(GoalStack stack, VariableBindings vb, KnowledgeBase kb) {
		if (!isPrepared) create(kb);
		boolean success = false;

		while (childIndex < getChildren().size()) {
			if (childIndex > 0) {
				ExecutionTreeElement previousElement = getChildren().get(childIndex-1);
				vb.removeBindings(previousElement);
				if (previousElement instanceof ExecutionTreeRule) {
					stack.remove(previousElement.getChildren());
				}
			}
			ExecutionTreeElement currentElement = getChildren().get(childIndex);
			if (currentElement instanceof ExecutionTreeFacts) {
				success = ((ExecutionTreeFacts) currentElement).next(vb);
				if (!success) childIndex++;
			}
			else {
				ExecutionTreeRule currentRule = (ExecutionTreeRule) getChildren().get(childIndex);
				boolean unify = Unification.unify(goal, currentRule, vb);
				assert(unify);
				currentRule.create(kb, stack);
				childIndex++;
				success = true;
			}
			if (success) break;
			
		}
		return success;
		//always remove any possible old Stack entries first. 
	}
	
	public ExecutionTreeElement getCurrentlyProcessed() {
		if (childIndex == 0) return getChildren().get(0);
		else {
			return getChildren().get(childIndex-1);
		}
	}

	public void backtrack(VariableBindings vb,GoalStack stack) {
		ExecutionTreeElement currentElement = getChildren().get(childIndex-1);
		vb.removeBindings(currentElement);
		if (currentElement instanceof ExecutionTreeInstantiation) {
			stack.remove(currentElement.getChildren());
		}
		clearChildren();
		isPrepared = false;
	}
	
	public String toString() {
		return "Goal: "+goal.toString();
	}
	
	protected void create(KnowledgeBase kb) {		
		System.out.println("call " + this); //TODO sysout
		addChild(new ExecutionTreeFacts(goal,kb));
		
		List<ExecutionTreeRule> matchingRules = kb.getRuleBase().getExecutionTreeRules(goal);
		for (ExecutionTreeRule r:matchingRules) {
			addChild(r);
		}
		childIndex = 0;
		isPrepared = true;
	}
	
	
}
