package de.fzi.ipe.trie.debugger.gui.actions;

import org.eclipse.jface.action.Action;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;


public class BackAction extends Action implements DebuggerEventBusListener {
    
	private RuleHistory ruleHistory = new RuleHistory();
	private DebuggerEventBus eventBus;
	private DebuggerRule currentRule;
	
    public BackAction(DebuggerEventBus eventBus) {
        super("Text ...",Action.AS_PUSH_BUTTON);
        this.eventBus = eventBus;
        eventBus.addListener(this);
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_BACK));
        setDisabledImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_BACK_D));
        refresh();
    }
    
    /**
     * called when the displayed rule is changed. 
     */
    public void refresh() {
    	if (ruleHistory.hasElements()) {
    		setEnabled(true);
    	}
    	else setEnabled(false);
    }

    public void run() {
    	SelectedRuleEvent event = new SelectedRuleEvent(ruleHistory.popLast());
    	event.setIsBackwardNavigation(true);
    	eventBus.sendEvent(event);
    	refresh();
    }

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sel = (SelectedRuleEvent) event;
			if (!sel.isBackwardNavigation()) {
				if (currentRule != null) ruleHistory.add(currentRule);
				currentRule = sel.getRule();
			}
			else {
				currentRule = sel.getRule();
			}
			refresh();
		}
	}
}
