package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;


/**
 * Class used to manage the textual representation of a rule. 
 * it main purpose is enable the higlighting of currently active rule parts, towards
 * this end it keeps the lines of the rule in separate strings. 
 *
 */
public class TextualRule {
	
	private List<String> lines = new LinkedList<String>();
	private int goalLine = -1;
	
	public TextualRule(ExecutionTreeElement element) {
		if (element.getParent() instanceof ExecutionTreeQuery) {
			makeQuery((ExecutionTreeQuery) element.getParent());
		}
		else if (element.getParent() instanceof ExecutionTreeRule) {
			ExecutionTreeRule eRule = (ExecutionTreeRule) element.getParent();
			makeRule(eRule);
		}
		else if (element instanceof ExecutionTreeRule) {
			ExecutionTreeRule eRule = (ExecutionTreeRule) element;
			makeRule(eRule);
		}
		else {
			lines.add("??? "+element.getParent().getClass().toString());
		}
		if (element instanceof ExecutionTreeGoal) {
			findGoal((ExecutionTreeGoal) element);
		}
	}
	

	public TextualRule(Rule rule, ExecutionTreeGoal goal) {
		makeRule(rule);
		if (goal != null) findGoal(goal);
	}

	private void findGoal(ExecutionTreeGoal element) {
		String goalString = LabelUtil.toString(element.getGoal()).trim();
		for (int i=0;i<lines.size();i++) {
			if (lines.get(i).trim().equals(goalString)) {
				goalLine = i;
				break;
			}
		}
	}

	public int getActiveGoalLine() {
		return goalLine;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (String l:lines) {
			builder.append(l);
			builder.append("\n");
		}
		return builder.toString();
	}
	
	private void makeRule(ExecutionTreeRule rule) {
		lines.add("["+rule.getRule().getName());
		lines.add("\t"+LabelUtil.toString(rule.getHead()));
		lines.add("<-");
		appendAtomArray(rule.getBody());
		lines.add("]");		
	}
	
	private void makeRule(Rule rule) {
		lines.add("["+rule.getName());
		appendAtomArray(rule.getHead());
		lines.add("<-");
		appendAtomArray(rule.getBody());
		lines.add("]");
	}
	
	private void makeQuery(ExecutionTreeQuery query) {
		lines.add("Query:");
		lines.add("  <-");
		appendAtomArray(query.getBody());
	}
	
	private void appendAtomArray(Atom[] atoms) {
		for (Atom a: atoms) {
			lines.add("\t"+LabelUtil.toString(a));
		}
	}


	
	

}
