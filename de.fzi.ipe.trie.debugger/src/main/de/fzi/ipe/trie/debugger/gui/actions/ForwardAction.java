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


public class ForwardAction extends Action implements DebuggerEventBusListener{
    
	private DebuggerEventBus eventBus;
	private DebuggerRuleStore ruleStore;
	private DebuggerRule currentRule;
	private RuleHistory history = new RuleHistory();
	
    
    public ForwardAction(DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
        super("Text ...",Action.AS_PUSH_BUTTON);
        this.eventBus = eventBus;
        this.ruleStore = ruleStore;
        eventBus.addListener(this);
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_FORWARD));
        setDisabledImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_FORWARD_D));
        refresh();
    }
    
    /**
     * called when the displayed rule is changed. 
     */
    public void refresh() {
        if (history.hasElements()) setEnabled(true);
        else setEnabled(false);
    }

    public void run() {
    	SelectedRuleEvent event = new SelectedRuleEvent(history.popLast(),SelectedRuleEvent.Source.FORWARD);
    	event.setIsForwardNavigation(true);
    	eventBus.sendEvent(event);
    	refresh();
    }

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sel = (SelectedRuleEvent) event;
			if (sel.isBackwardNavigation() && currentRule != null) {
				history.add(currentRule);
			}
			else if (!sel.isForwardNavigation()){
				history.clearAll();
			}
			currentRule = sel.getRule();
			refresh();
		}
		else if (event instanceof RefreshEvent) {
			history.clearAll();
			if (currentRule != null) currentRule = ruleStore.getRule(currentRule.getName());
			refresh();
		}
	}
}
