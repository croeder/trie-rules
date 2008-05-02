package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;

public class Prooftree {
	
	ProoftreeQueryNode rootNode = new ProoftreeQueryNode("query");

	
	private int allElements = 0;
	private int kbElements = 0;
	private double kbGrounding = -1;
	
	
	public Prooftree(ExecutionTreeQuery query) {
		for (ExecutionTreeElement el: query.getChildren()) {
			ProoftreeAtomNode atomNode = new ProoftreeAtomNode((ExecutionTreeGoal) el);
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
		if ((node instanceof ProoftreeRuleNode) || (node instanceof ProoftreeFactNode)){
			allElements ++;
			kbElements ++;
		}
		else if (node instanceof ProoftreeAssumptionNode) {
			allElements ++;
		}
		for (ProoftreeNode child: node.getChildren()) {
			calculateGrounding(child);
		}
		kbGrounding = ((double) kbElements) / ((double) allElements);
	}

	
}
