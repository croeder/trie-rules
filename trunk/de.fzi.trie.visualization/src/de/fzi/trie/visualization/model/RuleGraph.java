package de.fzi.trie.visualization.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleProxy;
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
			for (Atom a:rule.getBody()) {
				List<Rule> matchingRules = ruleBase.getMatchingRules(a,Rule.DEFAULT_EDIT_DISTANCE);
				for (Rule r:matchingRules) {
					if (r instanceof RuleProxy) dependsOnCandidates.add(((RuleProxy)r).getRule());
					else dependsOnCandidates.add(r);
				}
			}
			for (Rule r:dependsOnCandidates) {
				dependsOn.add(ruleNodes.get(r));
				if (r instanceof RuleProxy) {
					RuleProxy rp = (RuleProxy) r;
					ruleNodes.get(rp.getRule()).dependingOn.add(this);
				}
				else ruleNodes.get(r).dependingOn.add(this);
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
