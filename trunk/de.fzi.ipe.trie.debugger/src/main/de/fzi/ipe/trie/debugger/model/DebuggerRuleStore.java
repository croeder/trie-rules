package de.fzi.ipe	.trie.debugger.model;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.extensionPoint.KnowledgeBaseListener;
import de.fzi.ipe.trie.inference.KnowledgeBase;


public class DebuggerRuleStore implements KnowledgeBaseListener {

	private static Datamodel dm;
	private static Map<Rule,DebuggerRule> ruleCache = new HashMap<Rule,DebuggerRule>();

	
	
	public static void clearCache() {
		ruleCache.clear();
	}


	public static DebuggerRule getRule(Rule rule) {
		DebuggerRule toReturn = ruleCache.get(rule);
		if (toReturn == null) {
			toReturn = new DebuggerRule(rule);
			ruleCache.put(rule,toReturn);
		}
		return toReturn;
	}
	
	public static DebuggerRule getRule(String ruleName) {
		Rule rule = dm.getKnowledgeBase().getRuleBase().getRule(ruleName);
		return getRule(rule);
	}
	
	public static KnowledgeBase getKnowledgeBase() {
		return dm.getKnowledgeBase();
	}
	
	public static Set<DebuggerRule> getPossibilities(DebuggerAtom bodyClause) {
		List<Rule> matchingRules = dm.getKnowledgeBase().getRuleBase().getMatchingRules(bodyClause.getAtom());
		Set<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		for (Rule r:matchingRules) {
			toReturn.add(getRule(r));
		}
		return toReturn;
	}
	
	/**
	 * Returns all rules this rule could depend on.
	 * @param rule
	 * @return
	 */
	public static Set getCompleteRuleExtent(DebuggerRule rule) {
		Set<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		Stack<DebuggerRule> toDo = new Stack<DebuggerRule>();
		toDo.push(rule);
		while (!toDo.empty()) {
			DebuggerRule current = (DebuggerRule) toDo.pop();
			DebuggerRule[] allPossibilities = current.getAllPossibilities();
			for (int i=0;i<allPossibilities.length;i++) {
				if (!toReturn.contains(allPossibilities[i])) {
					toReturn.add(allPossibilities[i]);
					toDo.push(allPossibilities[i]);
				}
			}
		}
		return toReturn;
	}
	
	public static Collection<DebuggerRule> getRules() {
		Set<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		Collection<Rule> allRules = dm.getKnowledgeBase().getRuleBase().getAllRules();
		for (Rule r:allRules) toReturn.add(getRule(r));
		return toReturn;
	}


	public void knowledgeBaseChanged() {
		clearCache();
	}


	public void setDatamodel(Datamodel dm) {
		DebuggerRuleStore.dm = dm;
	}


	public static void reload() throws IOException {
		dm.reload();
	}

	
}
