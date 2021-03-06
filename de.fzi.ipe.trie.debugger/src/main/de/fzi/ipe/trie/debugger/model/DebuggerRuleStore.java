package de.fzi.ipe.trie.debugger.model;


import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;
import java.util.Stack;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.KnowledgeBaseListener;


public class DebuggerRuleStore extends Observable implements KnowledgeBaseListener {

	private Datamodel dm;
	private Map<Rule,DebuggerRule> ruleCache = new HashMap<Rule,DebuggerRule>();

	
	public DebuggerRuleStore(Datamodel dm) {
		this.dm = dm;
		this.dm.getKnowledgeBase().addListener(this);
	}
	
	
	private DebuggerRule getRule(Rule rule) {
		DebuggerRule toReturn = ruleCache.get(rule);
		if (toReturn == null) {
			toReturn = new CachingDebuggerRule(rule,this);
			ruleCache.put(rule,toReturn);
		}
		return toReturn;
	}
	
	public DebuggerRule getRule(String ruleName) {
		Rule rule = dm.getKnowledgeBase().getRuleBase().getRule(ruleName);
		return (rule != null) ? getRule(rule) : null;
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return dm.getKnowledgeBase();
	}
	
	public Set<DebuggerRule> getPossibilities(DebuggerAtom bodyClause) {
		List<Rule> matchingRules = dm.getKnowledgeBase().getRuleBase().getMatchingRules(bodyClause.getAtom(),Rule.DEFAULT_EDIT_DISTANCE);
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
	public Set<DebuggerRule> getCompleteRuleExtent(DebuggerRule rule) {
		Set<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		Stack<DebuggerRule> toDo = new Stack<DebuggerRule>();
		toDo.push(rule);
		while (!toDo.empty()) {
			DebuggerRule current = (DebuggerRule) toDo.pop();
			DebuggerRule[] allPossibilities = current.getAllPossibilities().toArray(new DebuggerRule[0]);
			for (int i=0;i<allPossibilities.length;i++) {
				if (!toReturn.contains(allPossibilities[i])) {
					toReturn.add(allPossibilities[i]);
					toDo.push(allPossibilities[i]);
				}
			}
		}
		return toReturn;
	}
	
	public Collection<DebuggerRule> getRules() {
		Set<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		Collection<Rule> allRules = dm.getKnowledgeBase().getRuleBase().getAllRules();
		for (Rule r:allRules) toReturn.add(getRule(r));
		return toReturn;
	}


	public void knowledgeBaseChanged() {
		ruleCache.clear();	
		setChanged();
		notifyObservers();
	}

	/**
	 * Forces a reload of everything. Actually causes the files from the disc to be reread
	 * @throws IOException
	 */
	public void reload() throws IOException {
		ruleCache.clear();
		dm.reload();
		notifyObservers();
	}
}
