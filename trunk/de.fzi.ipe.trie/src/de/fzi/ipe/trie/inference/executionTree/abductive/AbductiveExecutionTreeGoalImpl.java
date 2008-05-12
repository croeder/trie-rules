package de.fzi.ipe.trie.inference.executionTree.abductive;

import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.Unification;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeInstantiation;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class AbductiveExecutionTreeGoalImpl extends AbductiveExecutionTreeElementImpl implements ExecutionTreeGoal {

	private Atom goal;
	private boolean isPrepared = false;
	private int childIndex = 0;
	
	protected AbductiveExecutionTreeGoalImpl(Atom goal) {
		this.goal = goal;
	}
	
	public Atom getGoal() {
		return goal;
	}
	
	public boolean proof(GoalStack stack, VariableBindings vb, KnowledgeBase kb, Suspender suspender, ExecutionTreeQuery query) {
		if (!isPrepared) {
			suspender.performedAction(Suspender.Action.CALLING_GOAL, this, null);
			create(kb,suspender,vb);
		}
		else suspender.performedAction(Suspender.Action.RETRY_GOAL, this, null); 
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
			if (currentElement instanceof AbductiveExecutionTreeFactsImpl) {
				success = ((AbductiveExecutionTreeFactsImpl) currentElement).next(vb);
				if (!success) childIndex++;
			}
			else if (currentElement instanceof AbductiveExecutionTreeRuleImpl){
				AbductiveExecutionTreeRuleImpl currentRule = (AbductiveExecutionTreeRuleImpl) currentElement;
				boolean unify = (Unification.unify(goal, currentRule, vb, Rule.DEFAULT_EDIT_DISTANCE) != -1);
				if (unify) {
					currentRule.create(kb, stack);
					success = true;
				}
				else {
					success = false;
				}
				childIndex ++;
			}
			else if (currentElement instanceof AbductiveExecutionTreeAssumptionImpl) {
				AbductiveExecutionTreeAssumptionImpl assumption = (AbductiveExecutionTreeAssumptionImpl) currentElement;
				childIndex ++;
				if (query.kbGrounding().getFraction() >0.90) { 
					boolean unify = (Unification.unify(goal, assumption.getGoal(),assumption,vb,0) != -1);
					if (unify) {
						success = true;
					}
					else success = false;
				}
				else success = false;
			}
			
			if (success) {
				suspender.performedAction(Suspender.Action.EXIT_GOAL, this, null);
				break;
			}
			
		}
		return success;
	}
	

	public ExecutionTreeElement getCurrentlyProcessed() {
		if (childIndex == 0) {
			if (getChildren().size() > 0) return getChildren().get(0);
			else return null;
		}
		else {
			if (childIndex <= getChildren().size()) return getChildren().get(childIndex-1);
			else return null;
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
	
	protected void create(KnowledgeBase kb, Suspender suspender, VariableBindings vb) {		
		//facts
		addChild(new AbductiveExecutionTreeFactsImpl(goal,kb));
		
		//rules
		List<ExecutionTreeRule> matchingRules = kb.getRuleBase().getExecutionTreeRules(goal, AbductiveExecutionTreeFactory.getInstance(),Rule.DEFAULT_EDIT_DISTANCE);
		for (ExecutionTreeRule r:matchingRules) {
			AbductiveExecutionTreeRuleImpl ri = (AbductiveExecutionTreeRuleImpl) r;
			ri.setParent(this);
			addChild(r);
			suspender.performedAction(Suspender.Action.ADD_RULE_TO_EXECUTION_TREE, this, r.getRule());
		}
		
		//assumption
		ExecutionTreeAssumption assumption = new AbductiveExecutionTreeAssumptionImpl(goal,vb,this);
		addChild(assumption);
		
		childIndex = 0;
		isPrepared = true;
	}
	
}
