package de.fzi.ipe.trie.inference.prooftree;

import java.util.ArrayList;
import java.util.List;

public class ProoftreeNode {
	
	private List<ProoftreeNode> children = new ArrayList<ProoftreeNode>();
	private String name;
	
	
	public ProoftreeNode(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
	
	public String getTooltip() {
		return null;
	}
	
	public void addChild(ProoftreeNode child) {
		children.add(child);
	}

	public List<ProoftreeNode> getChildren() {
		return children;
	}
		

}
