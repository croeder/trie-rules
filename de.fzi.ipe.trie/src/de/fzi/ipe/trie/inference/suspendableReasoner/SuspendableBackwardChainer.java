package de.fzi.ipe.trie.inference.suspendableReasoner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.GoalStack;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;

public class SuspendableBackwardChainer {
	
	private Suspender suspender=null;
	
	private KnowledgeBase knowledgeBase;

	private GoalStack toProof = new GoalStack();
	private List<ExecutionTreeGoal> proofTrace = new LinkedList<ExecutionTreeGoal>();
	private VariableBindings vb = new VariableBindings();
	private List<VariableBindings> results = new ArrayList<VariableBindings>();
	private List<Prooftree> prooftrees = new ArrayList<Prooftree>();
	
	private ExecutionTreeQuery query;
	
	public SuspendableBackwardChainer(KnowledgeBase knowledgeBase, Suspender suspender) {
		this.knowledgeBase = knowledgeBase;
		this.suspender = suspender;
	}
	
	public GoalStack getGoalStack() {
		return toProof;
	}
	
	public List<ExecutionTreeGoal> getProofTrace() {
		return proofTrace;
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
				suspender.performedAction(Suspender.Action.TryingGoal, toProof.peek().getGoal(), null);
				ExecutionTreeGoal currentElement = toProof.pop(); 
				System.out.println("Try to prove "+currentElement); //TODO sysout
				
				if (!currentElement.proof(toProof, vb, knowledgeBase)) { //backtrack
					System.out.println("Failed to prove "+currentElement); //TODO sysout
					if (proofTrace.size() == 0) return;
					else {
						currentElement.backtrack(vb,toProof);
						toProof.push(currentElement);
						ExecutionTreeGoal lastGoal = proofTrace.remove(proofTrace.size()-1);
						toProof.push(lastGoal);
					}
				}
				else proofTrace.add(currentElement);
			}
			results.add(vb.clone());
			prooftrees.add(new Prooftree(query));
			if (proofTrace.size() == 0) return;
			else {
				ExecutionTreeGoal lastGoal = proofTrace.remove(proofTrace.size()-1);
				toProof.push(lastGoal);
			}
		}
	}

	public VariableBindings getVariableBindings() {
		return vb;
	}	
	
}
