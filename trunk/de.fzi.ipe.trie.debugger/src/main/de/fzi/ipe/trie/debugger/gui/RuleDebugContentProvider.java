package de.fzi.ipe.trie.debugger.gui;

import java.io.IOException;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;



public class RuleDebugContentProvider {
    
    public static int DEPENDS_MODE_NONE = 0; //show nothing
    public static int DEPENDS_MODE_RULES = 1; //the depends on part should display a list of rules
    public static int DEPENDS_MODE_BUILTIN = 2; //the depends on part should display the description of a builtin
    
    
    
    
    public RuleDebugContentProvider() {
    }
    
 
    public DebuggerRule[] getAllRules() {
        return DebuggerRuleStore.getRules().toArray(new DebuggerRule[0]);
    }
        
    /**
     * Invalidates the caches and reloads everything from the knowledgeBase.
     * @throws OntobrokerException
     */
    public void reload() throws IOException{ 
    	DebuggerRuleStore.clearCache();
    	DebuggerRuleStore.reload();
    }

}
