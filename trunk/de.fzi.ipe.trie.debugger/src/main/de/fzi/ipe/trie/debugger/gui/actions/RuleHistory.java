package de.fzi.ipe.trie.debugger.gui.actions;

import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class RuleHistory {

	public static final int MAX_SIZE = 50; 
	
	private List<DebuggerRule> history = new LinkedList<DebuggerRule>();
	
	public void add(DebuggerRule rule) {
		if (peekLast() == null || (!peekLast().equals(rule))) {
			history.add(rule);
			if (history.size()>MAX_SIZE) history.remove(0);
		}
	}
	
	public boolean hasElements() {
		return history.size() > 0;
	}
	
	public DebuggerRule popLast() {
		if (history.size()>0) {
			return history.remove(history.size()-1);
		}
		else return null;
	}
	
	public DebuggerRule peekLast() {
		if (history.size() > 0) {
			return history.get(history.size()-1);
		}
		else return null;
	}
	
	public void clearAll() {
		history.clear();
	}
}
