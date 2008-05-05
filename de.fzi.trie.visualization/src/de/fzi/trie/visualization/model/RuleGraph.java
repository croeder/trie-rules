package de.fzi.trie.visualization.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.RuleBase;

public class RuleGraph {
	
	private RuleNode[] elements; 
	
	public RuleGraph(RuleBase ruleBase) {
		HashMap<Rule,RuleNode> ruleNodes = new HashMap<Rule,RuleNode>();
		
		Collection<Rule> allRules = ruleBase.getAllRules();
		for (Rule r:allRules) ruleNodes.put(r, new RuleNode(r));
		
		Collection<RuleNode> allNodes = ruleNodes.values();
		for (RuleNode n:allNodes) n.connect(ruleBase, ruleNodes);
		
		this.elements = ruleNodes.values().toArray(new RuleNode[0]);
	}
	
	public RuleNode[] getElements() {
		return elements;
	}
	
	public static class RuleNode {
		
		private Rule rule;
		private Set<RuleNode> dependsOn = new HashSet<RuleNode>();
		private Set<RuleNode> dependingOn = new HashSet<RuleNode>();
		
		
		public RuleNode(Rule rule) {
			this.rule = rule;
		}

		public Rule getRule() {
			return rule;
		}
		
		public void connect(RuleBase ruleBase, HashMap<Rule, RuleNode> ruleNodes) {
			Set<Rule> dependsOnCandidates = new HashSet<Rule>();
			for (Atom a:rule.getBody()) dependsOnCandidates.addAll(ruleBase.getMatchingRules(a,Rule.DEFAULT_EDIT_DISTANCE));
			for (Rule r:dependsOnCandidates) {
				dependsOn.add(ruleNodes.get(r));
				ruleNodes.get(r).dependingOn.add(this);
			}
		}

		public Set<RuleNode> getDependsOn() {
			return dependsOn;
		}

		public Set<RuleNode> getDependingOn() {
			return dependingOn;
		}
	}

	
}
