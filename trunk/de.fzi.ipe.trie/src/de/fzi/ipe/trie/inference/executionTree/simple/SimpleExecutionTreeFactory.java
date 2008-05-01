package de.fzi.ipe.trie.inference.executionTree.simple;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFactory;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

/**
 * Factory creating simple instances for the execution tree element interfaces.  
 * @author zach
 *
 */
public class SimpleExecutionTreeFactory implements ExecutionTreeFactory{

	private static SimpleExecutionTreeFactory instance = new SimpleExecutionTreeFactory();

	public static SimpleExecutionTreeFactory getInstance() {
		return instance;
	}
	
	public ExecutionTreeElement createExecutionTreeElement() {
		return new ExecutionTreeElementImpl();
	}

	public ExecutionTreeRule createExecutionTreeRule(Rule r, Atom a) {
		return new ExecutionTreeRuleImpl(r,a);
	}

	public ExecutionTreeQuery createExecutionTreeQuery(Atom[] goals) {
		return new ExecutionTreeQueryImpl(goals);
	}

	public ExecutionTreeFacts createExecutionTreeFacts(Atom goal, KnowledgeBase knowledgeBase) {
		return new ExecutionTreeFactsImpl(goal,knowledgeBase);
	}
	
	
	
	
	
	
}
