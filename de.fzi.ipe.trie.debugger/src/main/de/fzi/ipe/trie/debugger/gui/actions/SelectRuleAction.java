/**
 * 
 */
package de.fzi.ipe.trie.debugger.gui.actions;

import org.eclipse.jface.action.Action;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class SelectRuleAction extends Action {
	private DebuggerRule rule;
	private DebuggerEventBus eventBus;

	public SelectRuleAction(DebuggerRule rule, DebuggerEventBus eventBus) {
		super(rule.getName());
		this.eventBus = eventBus;
		this.rule = rule;
	}

	public void run() {
		eventBus.sendEvent(new SelectedRuleEvent(rule));
	}
}