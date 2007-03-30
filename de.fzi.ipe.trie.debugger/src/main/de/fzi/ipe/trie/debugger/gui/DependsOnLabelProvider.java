package de.fzi.ipe.trie.debugger.gui;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;


public class DependsOnLabelProvider implements ILabelProvider {

    private Set<DebuggerRule> rules;
    
    public DependsOnLabelProvider(Set<DebuggerRule> rulesThatSupplyBindings) {
        if (rulesThatSupplyBindings != null) rules = rulesThatSupplyBindings;
        else rules = new HashSet<DebuggerRule>();
    }
    
    
    public Image getImage(Object element) {
    	DebuggerRule rule = (DebuggerRule) element;
        if (rules.contains(rule)) {
            return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_GREEN);
        }
        else return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_BLACK);
    }

    public String getText(Object element) {
    	return ((DebuggerRule) element).getName();
    }

    public void addListener(ILabelProviderListener listener) {
        ;
    }

    public void dispose() {
        ;
    }

    public boolean isLabelProperty(Object element, String property) {
        return true;
    }

    public void removeListener(ILabelProviderListener listener) {
        ;
    }

}
