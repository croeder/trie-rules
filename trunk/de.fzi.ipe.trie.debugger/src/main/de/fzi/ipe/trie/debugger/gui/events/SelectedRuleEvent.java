package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class SelectedRuleEvent implements DebuggerEvent {
	
	
	private DebuggerRule rule;
	
	public SelectedRuleEvent(DebuggerRule debuggerRule) {
		this.rule = debuggerRule;
	}
	
	public DebuggerRule getRule() {
		return rule;
	}
	
	

}
