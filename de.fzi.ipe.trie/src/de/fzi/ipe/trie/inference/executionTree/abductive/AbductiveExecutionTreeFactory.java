package de.fzi.ipe.trie.inference.executionTree.abductive;

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
public class AbductiveExecutionTreeFactory implements ExecutionTreeFactory{

	private static AbductiveExecutionTreeFactory instance = new AbductiveExecutionTreeFactory();

	public static AbductiveExecutionTreeFactory getInstance() {
		return instance;
	}
	
	public ExecutionTreeElement createExecutionTreeElement() {
		return new AbductiveExecutionTreeElementImpl();
	}

	public ExecutionTreeRule createExecutionTreeRule(Rule r, Atom a) {
		return new AbductiveExecutionTreeRuleImpl(r,a);
	}

	public ExecutionTreeQuery createExecutionTreeQuery(Atom[] goals) {
		return new AbductiveExecutionTreeQueryImpl(goals);
	}

	public ExecutionTreeFacts createExecutionTreeFacts(Atom goal, KnowledgeBase knowledgeBase) {
		return new AbductiveExecutionTreeFactsImpl(goal,knowledgeBase);
	}
	
	
	
	
	
	
}
