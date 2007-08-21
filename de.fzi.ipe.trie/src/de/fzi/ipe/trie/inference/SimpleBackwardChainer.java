package de.fzi.ipe.trie.inference;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;

public class SimpleBackwardChainer {

	private KnowledgeBase knowledgeBase;
	private Suspender suspender;
	
	private GoalStack toProof = new GoalStack();
	private List<ExecutionTreeGoal> proofTrace = new LinkedList<ExecutionTreeGoal>();
	private VariableBindings vb = new VariableBindings();
	private List<VariableBindings> results = new ArrayList<VariableBindings>();
	private List<Prooftree> prooftrees = new ArrayList<Prooftree>();
	
	private ExecutionTreeQuery query;
	
	public GoalStack getGoalStack() { return toProof; }
	public List<ExecutionTreeGoal> getProofTrace() { return proofTrace; }
	public VariableBindings getVariableBindings() { return vb; }	
	public ExecutionTreeQuery getExecutionTree() { return query; }

	public SimpleBackwardChainer(KnowledgeBase knowledgeBase) {
		this(knowledgeBase,new NullSuspender());
	}
	
	public SimpleBackwardChainer(KnowledgeBase knowledgeBase, Suspender suspender) {
		this.knowledgeBase = knowledgeBase;
		this.suspender = suspender;
	}
	
	
	
	public Result hasProof(Atom[] goals) {
		query = new ExecutionTreeQuery(goals);
		query.create(knowledgeBase,toProof);
		
		proceed();
		return new Result(query,results, prooftrees);
	}
	
	
	
	
	private void proceed() {
		while (true) {
			while (!toProof.isEmpty()) {
				ExecutionTreeGoal currentElement = toProof.pop(); 
				
				if (!currentElement.proof(toProof, vb, knowledgeBase,suspender)) { //backtrack
					suspender.performedAction(Suspender.Action.FAIL_GOAL, currentElement, null);
					if (proofTrace.size() == 0) {
						suspender.performedAction(Suspender.Action.END, null, null);						
						return;
					}
					else {
						currentElement.backtrack(vb,toProof);
						toProof.push(currentElement);
						ExecutionTreeGoal lastGoal = proofTrace.remove(proofTrace.size()-1);
						toProof.push(lastGoal);
					}
				}
				else proofTrace.add(currentElement);
			}
			suspender.performedAction(Suspender.Action.SUCCESS, null, null);
			results.add(vb.clone());
			prooftrees.add(new Prooftree(query));
			if (proofTrace.size() == 0) {
				suspender.performedAction(Suspender.Action.END, null, null);
				return;
			}
			else {
				ExecutionTreeGoal lastGoal = proofTrace.remove(proofTrace.size()-1);
				toProof.push(lastGoal);
			}
		}
	}	
	
}
