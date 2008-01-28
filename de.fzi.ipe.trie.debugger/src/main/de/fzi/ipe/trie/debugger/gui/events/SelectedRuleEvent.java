package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class SelectedRuleEvent implements DebuggerEvent {
	
	private boolean isForward = false, isBackward=false; 

	private DebuggerRule rule;
	
	public SelectedRuleEvent(DebuggerRule debuggerRule) {
		this.rule = debuggerRule;
	}
	
	public void setIsForwardNavigation(boolean isForward) {
		this.isForward = isForward;
		this.isBackward = !isForward;
	}
	
	public void setIsBackwardNavigation(boolean isBackward) {
		this.isBackward = isBackward;
		this.isForward = !isBackward;
	}
	
	public boolean isBackwardNavigation() {
		return isBackward;
	}
	
	public boolean isForwardNavigation() {
		return isForward;
	}
	
	public DebuggerRule getRule() {
		return rule;
	}
	
	
}
