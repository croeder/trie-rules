package de.fzi.ipe.trie.debugger.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredContentProvider;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;
import de.fzi.ipe.trie.inference.Result;



public class RuleDebugContentProvider implements DebuggerEventBusListener {
    
    public static int DEPENDS_MODE_NONE = 0; //show nothing
    public static int DEPENDS_MODE_RULES = 1; //the depends on part should display a list of rules
    public static int DEPENDS_MODE_BUILTIN = 2; //the depends on part should display the description of a builtin
    
    private static DebuggerRule[] EMPTY_LIST = new DebuggerRule[0];
    
    private RuleHistory backRules = new RuleHistory(), forwardRules = new RuleHistory();
    
    
    private DebuggerRule currentRule = null;
    private DebuggerAtom currentClause = null;
    private ResultLineProvider currentResult = null;
    
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
        currentClause = null;
        currentResult = null;
//        viewPart.refresh(); 
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
                
    public DebuggerRule getCurrentRule() {
        return currentRule;
    }
    
    public DebuggerRule[] getAllRules() {
        return DebuggerRuleStore.getRules();
    }
    
    
    public boolean hasBackRules() {
        return (!backRules.empty());
    }
    
    public boolean hasForwardRules() {
        return (!forwardRules.empty());
    }
    
    
    /**
     * Gets a list of rules. The array starts with rules that had been recently accessed. "numberRules" returns the max number
     * of rules that is returned. 
     * @return
     * @throws OntobrokerException
     */
    public DebuggerRule[] getLastRules(int numberRules) {
        ArrayList<DebuggerRule> tempList = new ArrayList<DebuggerRule>();
        Iterator<DebuggerRule> it = forwardRules.iterator();
        while ((it.hasNext()) && (tempList.size() < numberRules)) {
            DebuggerRule currentRuleId = (DebuggerRule) it.next();
            if (!tempList.contains(currentRuleId)) {
                tempList.add(currentRuleId);
            }
        }

        it = backRules.iterator();
        while ((it.hasNext()) && (tempList.size() < numberRules)) {
            DebuggerRule currentRuleId = (DebuggerRule) it.next();
            if (!tempList.contains(currentRuleId)) {
                tempList.add(currentRuleId);
            }
        }
        
        if (tempList.size() < numberRules) {
            DebuggerRule[] allRules = getAllRules();
            int i=0;
            while ((tempList.size() < numberRules) && (i<allRules.length)) {
                if (!tempList.contains(allRules[i])) {
                    tempList.add(allRules[i]);
                }
                i++;
            }
        }
        
        DebuggerRule[] toReturn = new DebuggerRule[tempList.size()];
        toReturn = (DebuggerRule[]) tempList.toArray(toReturn);
        return toReturn;
    }
    
    public IStructuredContentProvider getDependsOnContentProvider() {
        if (currentClause != null) {
            return new RuleListContentProvider(currentClause.getPossibilities());
        }
        else if (currentRule != null) {
            return new RuleListContentProvider(currentRule.getAllPossibilities());
        }
        else return new RuleListContentProvider(EMPTY_LIST);
    }
    
    public Result getBindings()  {
        if (currentClause != null) {
            if (currentClause.getBindings() != null) {
                return currentClause.getBindings();
            }
            else return null;
        }
        else if (currentRule != null) {
            return currentRule.getBindings();
        }
        else  {
            return null;
        }
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

    public ResultLineProvider getCurrentResult() {
        return currentResult;
    }
        
    public DebuggerAtom getCurrentClause() {
        return currentClause;
    }

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sre = (SelectedRuleEvent) event;
        	selectRule(sre.getRule().getName());
			
		}
		
	}
    

}
