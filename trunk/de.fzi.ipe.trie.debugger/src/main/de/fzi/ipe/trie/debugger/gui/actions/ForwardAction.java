package de.fzi.ipe.trie.debugger.gui.actions;

import org.eclipse.jface.action.Action;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.RuleDebugContentProvider;


public class ForwardAction extends Action {
    
    private RuleDebugContentProvider contentProvider;
    
    public ForwardAction(RuleDebugContentProvider contentProvider) {
        super("Text ...",Action.AS_PUSH_BUTTON);
        this.contentProvider = contentProvider;
        setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_FORWARD));
        setDisabledImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_FORWARD_D));
        refresh();
    }
    
    /**
     * called when the displayed rule is changed. 
     */
    public void refresh() {
        if (contentProvider.hasForwardRules()) setEnabled(true);
        else setEnabled(false);
    }

    public void run() {
        contentProvider.forward();
    }
}
