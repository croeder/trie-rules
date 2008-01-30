package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class AtomActivatedEvent implements DebuggerEvent {
	
	private DebuggerAtom atom;
	
	public AtomActivatedEvent(DebuggerAtom atom) {
		this.atom = atom;
	}
	
	public DebuggerAtom getAtom() {
		return atom;
	}
	

}
