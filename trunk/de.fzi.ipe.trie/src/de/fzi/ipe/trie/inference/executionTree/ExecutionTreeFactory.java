package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.KnowledgeBase;

public interface ExecutionTreeFactory {

	public ExecutionTreeRule createExecutionTreeRule(Rule r, Atom a);
	
	public ExecutionTreeElement createExecutionTreeElement();
	
	public ExecutionTreeQuery createExecutionTreeQuery(Atom[] goals);

	public ExecutionTreeFacts createExecutionTreeFacts(Atom goal,KnowledgeBase knowledgeBase);
	
}
