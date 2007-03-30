package de.fzi.ipe.trie.inference.executionTree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.ProofVariable;

public class ExecutionTreeRule extends ExecutionTreeInstantiation {

	private Rule rule;
	private Atom head;
	
	/**
	 * Remember that a rule may have more than one head while a rule instantiation only has one!
	 */
	public ExecutionTreeRule(Rule rule, Atom head) {
		body = rule.getBody().clone();
		for (int i=0;i<body.length;i++) body[i] = body[i].clone();
		this.head = head.clone();
		this.rule = rule;
	}

	public Rule getRule() {
		return rule;
	}
	
	public Atom getHead() {
		return head;
	}
	
	public String toString() {
		return "Rule "+rule.getName();
	}
	
	public void replace(ProofVariable v1, ProofVariable v2) {
		head.replace(v1, v2);
		for (Atom a:body) a.replace(v1, v2);
	}
	

	
}
