package de.fzi.ipe.trie.inference.prooftree;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;

public class ProoftreeFactNode extends ProoftreeNode{

	private Atom facts;
	
	public ProoftreeFactNode(ExecutionTreeFacts facts) {
		super(facts.getLastAtom().toString());
		
		this.facts = facts.getLastAtom().clone();
	}
	
	public Atom getFacts() {
		return facts;
	}

}
