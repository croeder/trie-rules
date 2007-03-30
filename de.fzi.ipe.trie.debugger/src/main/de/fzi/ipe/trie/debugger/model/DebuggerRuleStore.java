package de.fzi.ipe.trie.debugger.model;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.KnowledgeBase;


public class DebuggerRuleStore {

	private static KnowledgeBase kb = new KnowledgeBase();
	private static Map<Rule,DebuggerRule> ruleCache = new HashMap<Rule,DebuggerRule>();

	
	
	public static void clearCache() {
		kb.clear();
		ruleCache.clear();
	}


	public void init(KnowledgeBase kb) {
		DebuggerRuleStore.kb = kb;
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
		Rule rule = kb.getRuleBase().getRule(ruleName);
		return getRule(rule);
	}
	
	public static KnowledgeBase getKnowledgeBase() {
		return kb;
	}
	
	public static Set<DebuggerRule> getPossibilities(DebuggerAtom bodyClause) {
		List<Rule> matchingRules = kb.getRuleBase().getMatchingRules(bodyClause.getAtom());
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
	
	public static DebuggerRule[] getRules() {
		Collection<Rule> allRules = kb.getRuleBase().getAllRules();
		for (Rule r:allRules) {
			getRule(r);
		}
		return ruleCache.values().toArray(new DebuggerRule[ruleCache.values().size()]);
	}

	
}
