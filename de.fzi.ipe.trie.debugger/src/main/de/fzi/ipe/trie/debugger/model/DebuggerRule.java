package de.fzi.ipe.trie.debugger.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.BackwardChainer;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.executionTree.abductive.AbductiveBackwardChainer;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeRuleNode;

/**
 * @author zach
 * ! This class isn't caching anything, its strongly recommended to use the CachingDebuggerRule subclass. 
 */
public class DebuggerRule  {
	
	private Rule rule;
	private List<DebuggerAtom> bodyClauses = new ArrayList<DebuggerAtom>(),headClauses=new ArrayList<DebuggerAtom>();	
	private DebuggerRuleStore debuggerRuleStore;
	
	public String getName() {
		return rule.getName();
	}
	
	public String getComment() {
		return rule.getComment();
	}
	
	public Rule getRule() {
		return rule;
	}
		
	protected DebuggerRule(Rule rule, DebuggerRuleStore debuggerRuleStore) {
		this.debuggerRuleStore = debuggerRuleStore;
		this.rule = rule;
		for (Atom a: rule.getHead()) {
			headClauses.add(new DebuggerAtom(a,this));
		}
		for (Atom a: rule.getBody()) {
			bodyClauses.add(new DebuggerAtom(a,this));
		}	
	}
	
	protected DebuggerRuleStore getDebuggerRuleStore() {
		return debuggerRuleStore;
	}
	
	/**
	 * Returns the bindings for the variables only of the active literals. 
	 * @return
	 */
	public Result getBindings() {
		BackwardChainer reasoner = new AbductiveBackwardChainer(debuggerRuleStore.getKnowledgeBase());
		
		List<Atom> activeBodyAtoms = new ArrayList<Atom>();
		for (DebuggerAtom da: bodyClauses) {
			if (da.isActive()) activeBodyAtoms.add(da.getAtom());
		}
		Result toReturn = reasoner.hasProof(activeBodyAtoms.toArray(new Atom[0]));
		return toReturn; 
	}
	
	public List<DebuggerRule> getAllPossibilities() {
		List<DebuggerRule> allPossibilities = new ArrayList<DebuggerRule>();
		HashSet<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		for (DebuggerAtom a: bodyClauses) {
			if (a.isActive()) {
				for (DebuggerRule r: a.getPossibilities()) toReturn.add(r);
			}
		}
		allPossibilities.addAll(toReturn);
		return allPossibilities;
	}
		
	public Set<DebuggerRule> getRulesThatSupplyBindings() {
		Set<DebuggerRule> rulesThatSupplyBindings = new HashSet<DebuggerRule>();
		Result result = getBindings();
		for (int i=0;i<result.numberResults();i++) {
			Prooftree prooftree = result.getProoftree(i);
			List<ProoftreeNode> children = prooftree.getRootNode().getChildren();
			for (ProoftreeNode pn: children) {
				for (ProoftreeNode candidate: pn.getChildren()) {
					if (candidate instanceof ProoftreeRuleNode) {
						rulesThatSupplyBindings.add(debuggerRuleStore.getRule(((ProoftreeRuleNode)candidate).getRule().getName()));
					}
				}
			}
		}
		return rulesThatSupplyBindings;
	}
		
	public List<DebuggerAtom> getBodyClauses() {
	    return bodyClauses;
	}
	
	public List<DebuggerAtom> getHeadClauses() {
	    return headClauses;
	}
	
	
	public String toString() {
	    return rule.toString();
	}
	
	
}
