package de.fzi.ipe.trie.inference.executionTree;

public class GroundingNumbers {
	
	private int allElements;
	private int elementsInKB;
	
	
	public GroundingNumbers(int allElements, int elementsInKb) {
		this.allElements = allElements;
		this.elementsInKB = elementsInKb;
	}
	
	public double getFraction() {
		return ((double) elementsInKB+10) / ((double) allElements+10);
	}
	
	public static GroundingNumbers add(GroundingNumbers one, GroundingNumbers two) {
		int allElements = one.allElements + two.allElements;
		int elementsKB = one.elementsInKB + two.elementsInKB;
		return new GroundingNumbers(allElements, elementsKB);
	}
	
	
}
