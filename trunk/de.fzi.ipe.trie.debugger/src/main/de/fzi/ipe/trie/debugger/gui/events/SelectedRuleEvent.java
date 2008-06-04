package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class SelectedRuleEvent implements DebuggerEvent {

	public enum Source {FORWARD, BACKWARD, QUERY_BUTTON, RULE_BUTTON, PROOFTREE, DEPENDS_ON,INTERNAL, RULE_LIST}
	
	private boolean isForward = false, isBackward=false; 

	private DebuggerRule rule;
	private Source source;
	
	public SelectedRuleEvent(DebuggerRule debuggerRule, Source source) {
		this.rule = debuggerRule;
		this.source = source;
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
	
	public Source getSource() {
		return source;
	}
	
	public DebuggerRule getRule() {
		return rule;
	}
	
	public String toString() {
		return "Selected Rule" +rule.getName()+" from "+source;
	}
	
	
}
