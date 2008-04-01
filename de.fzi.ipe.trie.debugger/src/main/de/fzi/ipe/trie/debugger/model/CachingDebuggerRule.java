package de.fzi.ipe.trie.debugger.model;

import java.util.List;
import java.util.Set;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Result;

public class CachingDebuggerRule extends DebuggerRule {

	private Result result;
	private List<DebuggerRule> possibilities;
	private Set<DebuggerRule> rulesThatSupplyBinding;
	
	private boolean[] atomActiveStatus; 
	
	
	protected CachingDebuggerRule(Rule rule) {
		super(rule);
		atomActiveStatus = new boolean[getBodyClauses().size()];
	}

	private void checkCacheCurrent() {
		boolean isCacheCurrent = true;
		List<DebuggerAtom> bodyClauses = getBodyClauses();
		for (int i=0;i<bodyClauses.size();i++) {
			if (atomActiveStatus[i] != bodyClauses.get(i).isActive()) {
				isCacheCurrent = false;
				atomActiveStatus[i] = bodyClauses.get(i).isActive();
			}
		}
		if (!isCacheCurrent) {
			result = null;
			possibilities = null;
			rulesThatSupplyBinding = null;
		}
	}	
	
	@Override
	public Result getBindings() {
		checkCacheCurrent();
		if (result == null) result = super.getBindings();
		return result;
	}
	
	@Override
	public List<DebuggerRule> getAllPossibilities() {
		checkCacheCurrent();
		if (possibilities == null) possibilities = super.getAllPossibilities();
		return possibilities;
	}
	
	@Override
	public Set<DebuggerRule> getRulesThatSupplyBindings() {
		checkCacheCurrent();
		if (rulesThatSupplyBinding == null) return super.getRulesThatSupplyBindings();
		return rulesThatSupplyBinding;
	}
	
	
	
}
