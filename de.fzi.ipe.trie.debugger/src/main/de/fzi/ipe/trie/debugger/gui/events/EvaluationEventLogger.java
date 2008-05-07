package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.DebugLogger;

public class EvaluationEventLogger implements DebuggerEventBusListener{

	public EvaluationEventLogger(DebuggerEventBus eventBus) {
		eventBus.addListener(this);
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sel = (SelectedRuleEvent) event;
			DebugLogger.log("SelectedRule",sel.getRule().getName(),sel.getSource().toString());
		}
		else {
			DebugLogger.log("           ", event.getClass().toString());
		}
	}
	
	
}
