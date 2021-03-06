package de.fzi.ipe.trie.inference.prooftree;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;

public class Prooftree implements Iterable<ProoftreeNode>{
	
	ProoftreeQueryNode rootNode = new ProoftreeQueryNode("query");

	
	private int allElements = 0;
	private int kbElements = 0;
	private double kbGrounding = -1;
	
	
	public Prooftree(ExecutionTreeQuery query, VariableBindings vb) {
		for (ExecutionTreeElement el: query.getChildren()) {
			ProoftreeAtomNode atomNode = new ProoftreeAtomNode((ExecutionTreeGoal) el,vb);
			rootNode.addChild(atomNode);
		}
		calculateGrounding(rootNode);
	}

	public ProoftreeNode getRootNode() {
		return rootNode;
	}
	

	/**
	 * Calculates the grounding of this prooftree in the knowledge base. 
	 * The grounding is the proportion of facts+rules to facts+rule+assumptions. 
	 * A prooftree without any assumption has 1, a lower score is computed for prooftrees with assumptions. 
	 */
	public double getGrounding() {
		return kbGrounding;
	}
	
	
	private void calculateGrounding(ProoftreeNode node) {
		if (node instanceof ProoftreeRuleNode) {
			ProoftreeRuleNode ruleNode = (ProoftreeRuleNode) node;
			if (!ruleNode.isAlmostMatch()) kbElements++;
			kbElements ++;
			allElements +=2;
		}
		else if (node instanceof ProoftreeFactNode){
			allElements +=2;
			kbElements +=2 ;
		}
		else if (node instanceof ProoftreeAssumptionNode) {
			allElements +=2;
		}
		for (ProoftreeNode child: node.getChildren()) {
			calculateGrounding(child);
		}
		kbGrounding = ((double) kbElements) / ((double) allElements);
	}

	public Iterator<ProoftreeNode> iterator() {
		List<ProoftreeNode> nodes = new LinkedList<ProoftreeNode>();
		collect(rootNode,nodes);
		return nodes.iterator();
	}
	
	private void collect(ProoftreeNode start, List<ProoftreeNode> collect) {
		collect.add(start);
		for (ProoftreeNode node:start.getChildren()) {
			collect(node,collect);
		}
	}

}
