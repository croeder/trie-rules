package de.fzi.ipe.trie.debugger.gui.actions;

import org.eclipse.jface.action.Action;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;

public class GoToQueryAction extends Action implements DebuggerEventBusListener {

	private DebuggerEventBus eventBus; 
	private DebuggerRuleStore ruleStore; 
	
	public GoToQueryAction(DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
		super("Go to Query", Action.AS_PUSH_BUTTON);
		this.eventBus = eventBus;
		this.ruleStore = ruleStore;
		this.eventBus.addListener(this);
		setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_QUERY_BLACK));
		setDisabledImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_QUERY_BLACK_GRAYED));
		setEnabledStatus(ruleStore);
	}


	private void setEnabledStatus(DebuggerRuleStore ruleStore) {
		if (ruleStore.getRule("Query") != null) setEnabled(true);
		else setEnabled(false);
	}
	
    public void run() {
		DebuggerRule query = ruleStore.getRule("Query");
		if (query != null) eventBus.sendEvent(new SelectedRuleEvent(query,SelectedRuleEvent.Source.QUERY_BUTTON));    
	}



	public void eventNotification(DebuggerEvent event) {
		if (event instanceof RefreshEvent) {
			setEnabledStatus(ruleStore);
		}
	}
	
	
	
}
