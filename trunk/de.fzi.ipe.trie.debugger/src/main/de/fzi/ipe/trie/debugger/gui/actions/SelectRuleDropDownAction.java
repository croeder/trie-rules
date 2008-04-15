package de.fzi.ipe.trie.debugger.gui.actions;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.dialogs.ListDialog;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.RuleListContentProvider;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;


public class SelectRuleDropDownAction extends Action implements IMenuCreator, DebuggerEventBusListener {

    private static final int MAX_NUMBER_RULES = 10;
    
    private DebugView viewPart;
    private Menu fMenu;

    private DebuggerEventBus eventBus;
    private DebuggerRuleStore ruleStore;
    
    private LastAccessedHistory history;
    
    
    public SelectRuleDropDownAction(DebugView viewPart, DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
        this.viewPart = viewPart;
        this.ruleStore = ruleStore;
        this.eventBus = eventBus;
        
        history = new LastAccessedHistory(MAX_NUMBER_RULES);
        
        eventBus.addListener(this);
        
        fMenu = null;
        setToolTipText("Select rule");
        setText("Select Rule");
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_RULE_BLACK));
        setMenuCreator(this);
    }

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sel = (SelectedRuleEvent) event;
			history.accessedRule(sel.getRule());
		}
		else if (event instanceof RefreshEvent) {
			history.clear();
		}
	}
    
    public Menu getMenu(Menu parent) {
        return null;
    }

    public Menu getMenu(Control parent) {
        if (fMenu != null) {
            fMenu.dispose();
        }
        fMenu = new Menu(parent);
        for (DebuggerRule r:history.getLastAccessedRules()) {
        	Action action = new SelectRuleAction(r);
        	ActionContributionItem item = new ActionContributionItem(action);
        	item.fill(fMenu, -1);
        }
        IContributionItem item = new Separator();
        item.fill(fMenu,-1);
        Action action = new Action() { public void run() {SelectRuleDropDownAction.this.run(); }};
        action.setText("Select Rule ... ");
        ActionContributionItem a_item = new ActionContributionItem(action);
        a_item.fill(fMenu,-1);
        
        return fMenu;
    }
        
    public void dispose() {
        if (fMenu != null) {
            fMenu.dispose();
            fMenu = null;
        }
    }

    public void run() {
        ListDialog listDialog = new ListDialog(viewPart.getShell());
        listDialog.setBlockOnOpen(true);
        
        listDialog.setContentProvider(new RuleListContentProvider());
        listDialog.setLabelProvider(new RuleListLabelProvider());
        listDialog.setInput(ruleStore.getRules());
        listDialog.setTitle("Choose Rule");
        listDialog.setMessage("Please choose the rule that you want to debug.");
        int code = listDialog.open();
        if ((code == Dialog.OK) && (listDialog.getResult().length > 0)){
        	eventBus.sendEvent(new SelectedRuleEvent((DebuggerRule)listDialog.getResult()[0]));
        }
    }
    
    private class SelectRuleAction extends Action {
        DebuggerRule rule;
        
        public SelectRuleAction(DebuggerRule rule) {
            super(rule.getName());
            this.rule = rule;
        }
        public void run() {
        	eventBus.sendEvent(new SelectedRuleEvent(rule));
        }
    }
    
    private static class RuleListLabelProvider extends LabelProvider {
        public String getText(Object element) {
            return ((DebuggerRule)element).getName();
        }
    }
        
    
    /**
     * Small class that stores information about the n last accessed rules. The most recently accessed 
     * is returned first.
     */    
    private class LastAccessedHistory {

    	private DebuggerRule currentRule = null;
    	private LinkedList<DebuggerRule> lastAccessedRules = new LinkedList<DebuggerRule>();
    	private int max_size;
    	
    	LastAccessedHistory(int size) {
    		this.max_size = size;
    		initialRulesForRuleHistory();
    	}

		private void initialRulesForRuleHistory() {
			Iterator<DebuggerRule> rules = ruleStore.getRules().iterator();
    		int i=0;
    		while (rules.hasNext()) {
    			lastAccessedRules.add((DebuggerRule)rules.next());
    			i++;
    			if (i==MAX_NUMBER_RULES) break;
    		}
		}
    	
    	void accessedRule(DebuggerRule rule) {
    		if (currentRule != rule) {
    			if (currentRule != null) {
	    			lastAccessedRules.remove(currentRule);
	    			lastAccessedRules.add(0,currentRule);
	    			if (lastAccessedRules.size() > max_size) lastAccessedRules.removeLast();
    			}
    			currentRule = rule;
    		}
    	}
    	
    	public List<DebuggerRule> getLastAccessedRules() {
    		return lastAccessedRules;
    	}
    	
    	public void clear() {
    		lastAccessedRules.clear();
    		currentRule = null;
    		initialRulesForRuleHistory();
    	}
    }
    
    
    
}
