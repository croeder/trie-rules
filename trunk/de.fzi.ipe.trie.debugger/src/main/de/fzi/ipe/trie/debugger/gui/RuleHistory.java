package de.fzi.ipe.trie.debugger.gui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;


public class RuleHistory {

    private List<DebuggerRule> list;
    private int max_size = 20;
    
    public RuleHistory() {
        list = new LinkedList<DebuggerRule>();
    }
    
    
    public void add(DebuggerRule string) {
        if (string != null) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                if (it.next().equals(string)) it.remove();
            }
            list.add(string);
            if (list.size() > max_size) list.remove(0);
        }
    }
    
    public DebuggerRule remove() {
        if (list.size() >0) return (DebuggerRule) list.remove(list.size()-1);
        else return null;
    }
    
    public void clear() {
        list.clear();
    }
    
    public boolean empty() {
        return list.size() == 0;
    }
    
    public Iterator<DebuggerRule> iterator() {
        return list.iterator();
    }
    
}
