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
		else if (event instanceof SelectedResultLineEvent) {
			SelectedResultLineEvent srl = (SelectedResultLineEvent) event;
			if (srl.getResultLineProvider() != null) {
				DebugLogger.log("SelectedResultLine",srl.getResultLineProvider().toString());
			}
		}
		else if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent afe = (AtomFocussedEvent) event;
			DebugLogger.log("AtomFocussedEvent",""+afe.getAtom());
		}
		else if (event instanceof AtomActivatedEvent) {
			AtomActivatedEvent aae = (AtomActivatedEvent) event;
			DebugLogger.log("AtomActivatedEvent",""+aae.getAtom());
		}
		else if (event instanceof AtomDeactivatedEvent) {
			AtomDeactivatedEvent ade = (AtomDeactivatedEvent) event;
			DebugLogger.log("AtomDeactivatedEvent",""+ade.getAtom());
		}
		else if (event instanceof RefreshEvent) {
			DebugLogger.log("RefreshEvent");
		}
	}
	
	
}
