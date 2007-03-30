package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;

public class Prooftree {
	
	ProoftreeQueryNode rootNode = new ProoftreeQueryNode("query");
	
	
	public Prooftree(ExecutionTreeQuery query) {
		for (ExecutionTreeElement el: query.getChildren()) {
			ProoftreeAtomNode atomNode = new ProoftreeAtomNode((ExecutionTreeGoal) el);
			rootNode.addChild(atomNode);
		}
	}

	public ProoftreeNode getRootNode() {
		return rootNode;
	}
	
	

}
