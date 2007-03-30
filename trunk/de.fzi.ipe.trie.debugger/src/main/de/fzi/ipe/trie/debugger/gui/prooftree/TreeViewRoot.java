package de.fzi.ipe.trie.debugger.gui.prooftree;

import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;

public class TreeViewRoot {

	
	private ProoftreeNode rootNode;
	

	public TreeViewRoot(Prooftree rootNode) {
		this.rootNode = rootNode.getRootNode();
	}	
	
	public ProoftreeNode getRootNode() {
		return rootNode;
	}
	
		
}
