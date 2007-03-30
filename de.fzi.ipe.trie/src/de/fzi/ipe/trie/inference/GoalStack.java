package de.fzi.ipe.trie.inference;

import java.util.List;
import java.util.Stack;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public class GoalStack extends Stack<ExecutionTreeGoal>{
	
	private static final long serialVersionUID = 1L;

	public void add(List<ExecutionTreeElement> body) {
		for (int i=body.size()-1;i>=0;i--) push((ExecutionTreeGoal) body.get(i));
	}
	
	public void remove(List<ExecutionTreeElement> body) { //slow slow slow!
		for (int i=body.size()-1;i>=0;i--) remove((ExecutionTreeGoal) body.get(i));		
	}
	
}
