package de.fzi.ipe.trie.inference.executionTree.simple;

import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.Unification;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeInstantiation;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ExecutionTreeGoalImpl extends ExecutionTreeElementImpl implements ExecutionTreeGoal {

	private Atom goal;
	private boolean isPrepared = false;
	private int childIndex = 0;
	
	protected ExecutionTreeGoalImpl(Atom goal) {
		this.goal = goal;
	}
	
	public Atom getGoal() {
		return goal;
	}
	
	public boolean proof(GoalStack stack, VariableBindings vb, KnowledgeBase kb, Suspender suspender, ExecutionTreeQuery query ) {
		if (!isPrepared) {
			suspender.performedAction(Suspender.Action.CALLING_GOAL, this, null);
			create(kb,suspender);
		}
		else suspender.performedAction(Suspender.Action.RETRY_GOAL, this, null); 
		boolean success = false;

		while (childIndex < getChildren().size()) {
			if (childIndex > 0) {
				ExecutionTreeElement previousElement = getChildren().get(childIndex-1);
				vb.removeBindings(previousElement);
				if (previousElement instanceof ExecutionTreeRuleImpl) {
					stack.remove(previousElement.getChildren());
				}
			}
			ExecutionTreeElement currentElement = getChildren().get(childIndex);
			if (currentElement instanceof ExecutionTreeFactsImpl) {
				success = ((ExecutionTreeFactsImpl) currentElement).next(vb);
				if (!success) childIndex++;
			}
			else {
				ExecutionTreeRuleImpl currentRule = (ExecutionTreeRuleImpl) getChildren().get(childIndex);
				boolean unify = (Unification.unify(goal, currentRule, vb,0) != -1);
				if (unify) {
					currentRule.create(kb, stack);
					success = true;
				}
				else {
					success = false;
				}
				childIndex ++;
			}
			if (success) {
				suspender.performedAction(Suspender.Action.EXIT_GOAL, this, null);
				break;
			}
			
		}
		return success;
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
	
	public boolean isPrepared() {
		return isPrepared;
	}
	
	protected void create(KnowledgeBase kb, Suspender suspender) {		
		addChild(new ExecutionTreeFactsImpl(goal,kb));
		
		List<ExecutionTreeRule> matchingRules = kb.getRuleBase().getExecutionTreeRules(goal, SimpleExecutionTreeFactory.getInstance(),0);
		for (ExecutionTreeRule r:matchingRules) {
			ExecutionTreeRuleImpl ri = (ExecutionTreeRuleImpl) r;
			ri.setParent(this);
			addChild(r);
			suspender.performedAction(Suspender.Action.ADD_RULE_TO_EXECUTION_TREE, this, r.getRule());
		}
		childIndex = 0;
		isPrepared = true;
	}
	
	
}
