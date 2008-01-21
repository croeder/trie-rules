package de.fzi.ipe.trie.debugger.gui.prooftree;

import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;

public class TreeViewRoot {

	
	private ProoftreeNode rootNode;
	

	public TreeViewRoot(Prooftree rootNode) {
		if (rootNode != null) {
			this.rootNode = rootNode.getRootNode();
		}
		else rootNode = null;
	}	
	
	public ProoftreeNode getRootNode() {
		return rootNode;
	}
	
		
}
