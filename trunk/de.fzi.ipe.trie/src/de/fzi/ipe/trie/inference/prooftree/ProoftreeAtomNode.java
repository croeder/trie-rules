package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeAssumption;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

public class ProoftreeAtomNode extends ProoftreeNode{

	public ProoftreeAtomNode(ExecutionTreeGoal goal, VariableBindings vb) {
		super(goal.toString());
		ExecutionTreeElement currentlyProcessed = goal.getCurrentlyProcessed();
		if (currentlyProcessed instanceof ExecutionTreeRule) {
			addChild(new ProoftreeRuleNode((ExecutionTreeRule) currentlyProcessed,vb));
		}
		else if (currentlyProcessed instanceof ExecutionTreeAssumption) {
			addChild(new ProoftreeAssumptionNode((ExecutionTreeAssumption) currentlyProcessed,vb));
		}
		else {
			addChild(new ProoftreeFactNode((ExecutionTreeFacts)currentlyProcessed));
		}
	}

}
