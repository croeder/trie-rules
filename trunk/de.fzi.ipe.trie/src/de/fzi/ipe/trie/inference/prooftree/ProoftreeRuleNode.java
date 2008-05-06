package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleProxy;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ProoftreeRuleNode extends ProoftreeNode{

	private Rule rule;
	
	
	public Rule getRule() {
		return rule;
	}
	
	public boolean isAlmostMatch() {
		return rule instanceof RuleProxy;
	}
	
	public ProoftreeRuleNode(ExecutionTreeRule rule) {
		super(rule.getRule().getName());
		this.rule = rule.getRule();
		
		for (ExecutionTreeElement el: rule.getChildren()) {
			ProoftreeAtomNode atomNode = new ProoftreeAtomNode((ExecutionTreeGoal) el);
			addChild(atomNode);
		}
	}

	
}
