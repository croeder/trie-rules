package de.fzi.ipe.trie.debugger.model;

import java.util.Collection;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.inference.BackwardChainer;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.executionTree.abductive.AbductiveBackwardChainer;

public class DebuggerAtom {

	private Atom atom;
	private DebuggerRule rule;
	
	private boolean isActive = true;
	
	private DebuggerRule[] possibilities;
	private Result results;
	
	private boolean hasCalculatedBindings = false;
		
	public Collection<Variable> getVariables() {
		return atom.getVariables();
	}
	
	public boolean hasCalculatedBinding() {
		return hasCalculatedBindings;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public DebuggerRule getRule() {
		return rule;
	}

	public Atom getAtom() {
		return atom;
	}
	
	public boolean equals(Object other) {
		if (!(other instanceof DebuggerAtom)) return false;
		else {
			DebuggerAtom otherP = (DebuggerAtom) other;
			return this.atom.equals(otherP.atom);
		}
	}
	
	@Override
	public int hashCode() {
		return atom.hashCode();
	}
	
	public DebuggerRule[] getPossibilities() {
		if (possibilities == null) {
			Set<DebuggerRule> possibilities2 = rule.getDebuggerRuleStore().getPossibilities(this);
			possibilities = possibilities2.toArray(new DebuggerRule[possibilities2.size()]);
		}
		return possibilities;
	}

	public Result getBindings() {
		if (results != null) {
			return results;
		}
		else {
			BackwardChainer reasoner = new AbductiveBackwardChainer(rule.getDebuggerRuleStore().getKnowledgeBase());
			results = reasoner.hasProof(new Atom[] {atom});
			return results;
		}
	}

	public String toString() {
		return DebugView.LABEL_PROVIDER.getLabel(atom);
	}

	public DebuggerAtom(Atom atom, DebuggerRule rule) {
		this.atom = atom;
		this.rule = rule;
	}

	
}
