package de.fzi.ipe.trie.debugger.model;

import java.util.Collection;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.inference.Result;
import de.fzi.ipe.trie.inference.SimpleBackwardChainer;

public class DebuggerAtom {

	private Atom atom;
	private DebuggerRule rule;
	
	private DebuggerRule[] possibilities;
	private Result results;
	
	private boolean hasCalculatedBindings = false;
		
	public Collection<Variable> getVariables() {
		return atom.getVariables();
	}
	
	public boolean hasCalculatedBinding() {
		return hasCalculatedBindings;
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

	public DebuggerRule[] getPossibilities() {
		if (possibilities == null) {
			Set<DebuggerRule> possibilities2 = DebuggerRuleStore.getPossibilities(this);
			possibilities = possibilities2.toArray(new DebuggerRule[possibilities2.size()]);
		}
		return possibilities;
	}

	public Result getBindings() {
		if (results != null) {
			return results;
		}
		else {
			SimpleBackwardChainer reasoner = new SimpleBackwardChainer(DebuggerRuleStore.getKnowledgeBase());
			results = reasoner.hasProof(new Atom[] {atom});
			return results;
		}
	}

	public String toString() {
		return DebugView.labelProvider.getLabel(atom);
	}

	public DebuggerAtom(Atom atom, DebuggerRule rule) {
		this.atom = atom;
		this.rule = rule;
	}

	
}
