/**
 * 
 */
package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.LabelProvider;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class RuleListLabelProvider extends LabelProvider {
    public String getText(Object element) {
        return ((DebuggerRule)element).getName();
    }
}