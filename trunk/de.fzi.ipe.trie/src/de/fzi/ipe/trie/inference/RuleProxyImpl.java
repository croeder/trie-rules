package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleProxy;



/**
 * Used to represent a rule that is only returned because it can be 'almost' unified
 */
public class RuleProxyImpl implements RuleProxy{
	
	private Rule rule;
	private Atom goal;
	
	
	public RuleProxyImpl(Rule rule, Atom goal) {
		this.rule = rule;
		this.goal = goal;
	}
	
	public Atom getGoal() {
		return goal;
	}

	public Atom[] getBody() {
		return rule.getBody();
	}

	public String getComment() {
		return rule.getComment();
	}

	public Atom[] getHead() {
		return rule.getHead();
	}

	public String getName() {
		return rule.getName();
	}
	
	public Rule getRule() {
		return rule;
	}

}
