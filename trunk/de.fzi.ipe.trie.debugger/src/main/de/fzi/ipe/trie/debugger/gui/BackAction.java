package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.action.Action;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;


public class BackAction extends Action {
    
    private RuleDebugContentProvider contentProvider;
    
    public BackAction(RuleDebugContentProvider contentProvider) {
        super("Text ...",Action.AS_PUSH_BUTTON);
        this.contentProvider = contentProvider;
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_BACK));
        setDisabledImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_BACK_D));
        refresh();
    }
    
    /**
     * called when the displayed rule is changed. 
     */
    public void refresh() {
        if (contentProvider.hasBackRules()) setEnabled(true);
        else setEnabled(false);
    }

    public void run() {
        contentProvider.back();
    }
}
