package de.fzi.ipe.trie.debugger.gui.actions;



import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.dialogs.ListDialog;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.RuleDebugContentProvider;
import de.fzi.ipe.trie.debugger.gui.RuleListContentProvider;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;


public class SelectRuleDropDownAction extends Action implements IMenuCreator {

    private static final int MAX_NUMBER_RULES = 10;
    
    private DebugView viewPart;
    private Menu fMenu;

    private RuleDebugContentProvider contentProvider;
    private DebuggerEventBus eventBus;
    
    
    public SelectRuleDropDownAction(RuleDebugContentProvider contentProvider, DebugView viewPart, DebuggerEventBus eventBus) {
        this.viewPart = viewPart;
        this.contentProvider = contentProvider;
        this.eventBus = eventBus;
        fMenu = null;
        setToolTipText("Select rule");
        setText("Select Rule");
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_RULE));
        setMenuCreator(this);
    }

    public Menu getMenu(Menu parent) {
        return null;
    }

    public Menu getMenu(Control parent) {
        if (fMenu != null) {
            fMenu.dispose();
        }
        fMenu = new Menu(parent);
        DebuggerRule[] rules = contentProvider.getLastRules(MAX_NUMBER_RULES);
        for (int i = 0; i < rules.length; i++) {
            Action action = new SelectRuleAction(rules[i]);
            ActionContributionItem item = new ActionContributionItem(action);
            item.fill(fMenu, -1);
        }
        if (rules.length == MAX_NUMBER_RULES) {
            IContributionItem item = new Separator();
            item.fill(fMenu,-1);
            Action action = new Action() { public void run() {SelectRuleDropDownAction.this.run(); }};
            action.setText("More Rules ... ");
            ActionContributionItem a_item = new ActionContributionItem(action);
            a_item.fill(fMenu,-1);
        }
        
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
        
        //sort rule List
        ArrayList<DebuggerRule> temp = new ArrayList<DebuggerRule>();
        DebuggerRule[] allRules = contentProvider.getAllRules();
        for (int i=0;i<allRules.length;i++) temp.add(allRules[i]);
        Collections.sort(temp, new Comparator<DebuggerRule>() {

			public int compare(DebuggerRule o1, DebuggerRule o2) {
				DebuggerRule r1 = (DebuggerRule) o1; 
				DebuggerRule r2 = (DebuggerRule) o2;
				return r1.getName().compareTo(r2.getName());
			}});
        allRules = (DebuggerRule[]) temp.toArray(new DebuggerRule[allRules.length]);
        
        IStructuredContentProvider listContentProvider = new RuleListContentProvider(allRules);
        listDialog.setContentProvider(listContentProvider);
        listDialog.setLabelProvider(new RuleListLabelProvider());
        listDialog.setInput("egal");
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
    
    private class RuleListLabelProvider implements ILabelProvider {

            public Image getImage(Object element) {
                return null;
            }

            public String getText(Object element) {
                return ((DebuggerRule)element).getName();
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
        
    
    
}
