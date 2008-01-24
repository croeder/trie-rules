package de.fzi.ipe.trie.debugger.gui;

import java.io.IOException;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;



public class RuleDebugContentProvider implements DebuggerEventBusListener {
    
    public static int DEPENDS_MODE_NONE = 0; //show nothing
    public static int DEPENDS_MODE_RULES = 1; //the depends on part should display a list of rules
    public static int DEPENDS_MODE_BUILTIN = 2; //the depends on part should display the description of a builtin
    
    private RuleHistory backRules = new RuleHistory(), forwardRules = new RuleHistory();
    
    
    private DebuggerRule currentRule = null;
    
    public RuleDebugContentProvider(DebuggerEventBus eventBus) {
    	eventBus.addListener(this);
    }
                
    public void selectRule(String ruleName) {
        if (ruleName == null) {
            setupRule(null);
        }
        else if ((currentRule == null) || (!currentRule.getName().equals(ruleName))) {
            if (currentRule != null) {
                backRules.add(currentRule);
                forwardRules.clear();
            }
            setupRule(ruleName);
        }
    }
    
    private void setupRule(String ruleId) {        
        if (ruleId != null) currentRule = DebuggerRuleStore.getRule(ruleId);
        else currentRule = null;
    }
        
    public void back() {
        DebuggerRule rule = backRules.remove();
        if (rule != null) {
        	if (currentRule != null) forwardRules.add(currentRule);
        	setupRule(rule.getName());
        }
     }
    
    public void forward() {
    	DebuggerRule rule = forwardRules.remove();
        if (rule != null) {        
            if (currentRule != null) backRules.add(currentRule);
            setupRule(rule.getName());
        }
    }
                
 
    public DebuggerRule[] getAllRules() {
        return DebuggerRuleStore.getRules().toArray(new DebuggerRule[0]);
    }
    
    
    public boolean hasBackRules() {
        return (!backRules.empty());
    }
    
    public boolean hasForwardRules() {
        return (!forwardRules.empty());
    }
    
    
    /**
     * Invalidates the caches and reloads everything from the knowledgeBase.
     * @throws OntobrokerException
     */
    public void reload() throws IOException{ 
    	DebuggerRuleStore.clearCache();
    	DebuggerRuleStore.reload();
        if (currentRule != null) setupRule(currentRule.getName());
    }


	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sre = (SelectedRuleEvent) event;
        	selectRule(sre.getRule().getName());
			
		}
	}

}
