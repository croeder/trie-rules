package de.fzi.ipe.trie.debugger.gui.dependsOn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.graphics.Image;


import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;


public class DependsOnLabelProvider implements ILabelProvider {
	
    private Set<DebuggerRule> bindingSupplyingRules = new HashSet<DebuggerRule>();
    private Set<ILabelProviderListener> labelListeners = new HashSet<ILabelProviderListener>();
    
    public DependsOnLabelProvider() {
    }

    public void setRulesThatSupplyBindings(Collection<DebuggerRule> bindingSupplyingRules) {
    	this.bindingSupplyingRules.clear();
    	this.bindingSupplyingRules.addAll(bindingSupplyingRules);
    	notifyLabelProviderListener();
    }
    
    public void clearRulesThatSupplyBindings() {
    	bindingSupplyingRules.clear();
    	notifyLabelProviderListener();
    }
    
    
    public Image getImage(Object element) {
    	DebuggerRule rule = (DebuggerRule) element;
        if (bindingSupplyingRules.contains(rule)) {
            return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_GREEN);
        }
        else return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_BLACK);
    }

    public String getText(Object element) {
    	return ((DebuggerRule) element).getName();
    }
    
    private void notifyLabelProviderListener() {
    	LabelProviderChangedEvent event = new LabelProviderChangedEvent(this);
    	for (ILabelProviderListener l:labelListeners) {
    		l.labelProviderChanged(event);
    	}
    }

    public void addListener(ILabelProviderListener listener) {
        labelListeners.add(listener);
    }

    public void removeListener(ILabelProviderListener listener) {
    	labelListeners.remove(listener);
    }

    public void dispose() {
        ;
    }

    public boolean isLabelProperty(Object element, String property) {
        return true;
    }


}
