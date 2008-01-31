package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class AtomDeactivatedEvent implements DebuggerEvent{
	
	DebuggerAtom atom;
	
	
	public AtomDeactivatedEvent(DebuggerAtom atom) {
		this.atom = atom;
	}
	
	public DebuggerAtom getAtom() {
		return atom;
	}
	

}
