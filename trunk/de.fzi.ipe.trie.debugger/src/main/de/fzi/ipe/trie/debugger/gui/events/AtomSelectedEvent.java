package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class AtomSelectedEvent implements DebuggerEvent {

	private DebuggerAtom atom;
	
	public AtomSelectedEvent(DebuggerAtom atom) {
		this.atom = atom;
	}
	
	public DebuggerAtom getAtom() {
		return atom;
	}
	
}
