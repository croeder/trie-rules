package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleProxy;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ProoftreeRuleNode extends ProoftreeNode{

	private Rule rule;
	
	
	public Rule getRule() {
		return rule;
	}
	
	
	/**
	 * Returns true if this rule or any of the rules it is directly depending on return isAlmostMatch=true
	 * @return
	 */
	public boolean hasAlmostMatch() {
		if (isAlmostMatch()) return true;
		else {
			for (ProoftreeNode n: getChildren()) {
				if (n.isAlmostMatch()) return true;
			}
		}
		return false;
	}
	
	public boolean isAlmostMatch() {
		return rule instanceof RuleProxy;
	}
	
	public String getTooltip(){
		if (isAlmostMatch()) {
			RuleProxy ruleProxy = (RuleProxy) rule;
			return  "Rule was included based on the assumption that \n"+
					ruleProxy.getExplanation() + 
					"actually mean the same thing. ";
		}
		else if (rule.getComment() != null && rule.getComment().length() >0) return rule.getComment();
		else return null;
	}
	
	public ProoftreeRuleNode(ExecutionTreeRule rule, VariableBindings vb) {
		super(rule.getRule().getName());
		this.rule = rule.getRule();
		
		for (ExecutionTreeElement el: rule.getChildren()) {
			ProoftreeAtomNode atomNode = new ProoftreeAtomNode((ExecutionTreeGoal) el,vb);
			addChild(atomNode);
		}
	}

	
}
