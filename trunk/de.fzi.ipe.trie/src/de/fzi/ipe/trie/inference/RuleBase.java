package de.fzi.ipe.trie.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class RuleBase {

	Collection<Rule> rules = new HashSet<Rule>();
	
	/**
	 * Ridicoulisly slow method to find all rules for which a head atom unifies with the goal. A 
	 * rule may be returned multiple times when it has more than one head, at least two of which unify with
	 * the goal. 
	 * @param newRules
	 */
	public List<Rule> getMatchingRules(Atom goal) {
		List<Rule> toReturn = new ArrayList<Rule>();
		for (Rule r:rules) {
			for (Atom a:r.getHead()) {
				if (Unification.canUnify(goal, a)) toReturn.add(r);
			}
		}
		return toReturn;
	}
	
	public Collection<Rule> getAllRules() {
		return Collections.unmodifiableCollection(rules);
	}
	
	
	/**
	 * Ridicoulisly slow method to find all rules for which a head atom unifies with the goal. A 
	 * rule may be returned multiple times when it has more than one head, at least two of which unify with
	 * the goal. 
	 * @param newRules
	 */
	public List<ExecutionTreeRule> getExecutionTreeRules(Atom goal) {
		List<ExecutionTreeRule> toReturn = new ArrayList<ExecutionTreeRule>();
		for (Rule r:rules) {
			for (Atom a:r.getHead()) {
				if (Unification.canUnify(goal, a)) toReturn.add(new ExecutionTreeRule(r,a));
			}
		}
		return toReturn;
	}
	
	
	public Rule getRule(String name) {
		for (Rule r:rules) {
			if (r.getName().equals(name)) return r;
		}
		return null;
	}
	
	public void addRules(Collection<Rule> newRules) {
		rules.addAll(newRules);
	}
	
	public void removeRules(Collection<Rule> toRemove) {
		rules.removeAll(toRemove);
	}
	
	
}
