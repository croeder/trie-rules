package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerAtom;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class AtomFocussedEvent implements DebuggerEvent {

	private DebuggerAtom atom;
	private DebuggerRule rule;
	
	public AtomFocussedEvent(DebuggerAtom atom) {
		this.atom = atom;
		rule = atom.getRule();
	}
	
	public AtomFocussedEvent(DebuggerAtom atom, DebuggerRule rule) {
		this.atom = atom;
		this.rule = rule;
	}
	
	public DebuggerAtom getAtom() {
		return atom;
	}
	
	public DebuggerRule getRule() {
		return rule;
	}
	
	public String toString() {
		return "- AtomFocussedEvent "+atom;
	}
	
}
