package de.fzi.ipe.trie.debugger.gui.events;

/**
 * Event that is send when the data in the knowledge base is reloaded from disk. Upon receiving this 
 * event all object must discard remaining DebuggerRule, DebuggerAtoms etc. and retrieve new ones. 
 */
public class ReloadEvent implements DebuggerEvent{	
	
	
}
