package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ProoftreeAtomNode extends ProoftreeNode{

	public ProoftreeAtomNode(ExecutionTreeGoal goal) {
		super(goal.toString());
		ExecutionTreeElement currentlyProcessed = goal.getCurrentlyProcessed();
		if (currentlyProcessed instanceof ExecutionTreeRule) {
			addChild(new ProoftreeRuleNode((ExecutionTreeRule) currentlyProcessed));
		}
		else if (currentlyProcessed instanceof ExecutionTreeAssumption) {
			addChild(new ProoftreeAssumptionNode((ExecutionTreeAssumption) currentlyProcessed));
		}
		else {
			addChild(new ProoftreeFactNode((ExecutionTreeFacts)currentlyProcessed));
		}
	}

}
