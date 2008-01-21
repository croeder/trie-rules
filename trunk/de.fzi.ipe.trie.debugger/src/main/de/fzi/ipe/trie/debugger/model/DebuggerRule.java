package de.fzi.ipe.trie.debugger.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.SimpleBackwardChainer;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeRuleNode;

/**
 * @author zach
 */
public class DebuggerRule  {
	
	private Rule rule;
	private List<DebuggerAtom> bodyClauses = new ArrayList<DebuggerAtom>(),headClauses=new ArrayList<DebuggerAtom>();	
	private Result result;
		
	private Set<DebuggerRule> rulesThatSupplyBindings = new HashSet<DebuggerRule>();	
	private DebuggerRule[] allPossibilities;	
	
	
	public String getName() {
		return rule.getName();
	}
	
	public Rule getRule() {
		return rule;
	}
	
	public boolean hasCalculatedBindingForAllLiterals() { 
		for (DebuggerAtom atom: bodyClauses) { 
			if (atom.hasCalculatedBinding()) return false;
		}
		return true;
	}

	public void calculateBindingsForAllLiterals() {
		for (DebuggerAtom atom: bodyClauses) {
			atom.getBindings();
		}		
	}
	
	protected DebuggerRule(Rule rule) {
		this.rule = rule;
		for (Atom a: rule.getHead()) {
			headClauses.add(new DebuggerAtom(a,this));
		}
		for (Atom a: rule.getBody()) {
			bodyClauses.add(new DebuggerAtom(a,this));
		}	
	}
			
	protected void init() {
		for (DebuggerAtom a: headClauses) a.getPossibilities();
		for (DebuggerAtom a: bodyClauses) a.getPossibilities();

		HashSet<DebuggerRule> toReturn = new HashSet<DebuggerRule>();
		for (DebuggerAtom a: bodyClauses) {
			for (DebuggerRule r: a.getPossibilities()) toReturn.add(r);
		}

		allPossibilities = toReturn.toArray(new DebuggerRule[toReturn.size()]);		
	}
	
	public Result getBindings() {
		if (result != null) {
			return result;
		}
		else {
			SimpleBackwardChainer reasoner = new SimpleBackwardChainer(DebuggerRuleStore.getKnowledgeBase());
			result = reasoner.hasProof(rule.getBody());
			calculateRulesThatSupplyBindings();
			return result;
		}
	}

	private void calculateRulesThatSupplyBindings() {
		for (int i=0;i<result.numberResults();i++) {
			Prooftree prooftree = result.getProoftree(i);
			List<ProoftreeNode> children = prooftree.getRootNode().getChildren();
			for (ProoftreeNode pn: children) {
				for (ProoftreeNode candidate: pn.getChildren()) {
					if (candidate instanceof ProoftreeRuleNode) {
						rulesThatSupplyBindings.add(DebuggerRuleStore.getRule(((ProoftreeRuleNode)candidate).getRule()));
					}
				}
			}
		}
	}
	
	public DebuggerRule[] getAllPossibilities() {
	    if (allPossibilities == null) init();
		return allPossibilities;
	}
	
	
	public ProoftreeNode getProofTreeRoot(int resultLine) {
		return result.getProoftree(resultLine).getRootNode();
	}

	
	public Set<DebuggerRule> getRulesThatSupplyBindings() {
	    return rulesThatSupplyBindings;
	}
	
	public DebuggerAtom[] getBodyClauses() {
	    return bodyClauses.toArray(new DebuggerAtom[bodyClauses.size()]);
	}
	
	public DebuggerAtom[] getHeadClauses() {
	    return headClauses.toArray(new DebuggerAtom[headClauses.size()]);
	}
	
	
	public String toString() {
	    return rule.toString();
	}
	
	
}
