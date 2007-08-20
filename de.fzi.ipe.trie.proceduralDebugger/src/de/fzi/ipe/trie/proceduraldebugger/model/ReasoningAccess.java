package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.SimpleBackwardChainer;

public class ReasoningAccess {

	//	needs to be final, because components are ataching listeners to it (and they won't know when it's replaced)
	private static final ListenableSuspender suspender = new ListenableSuspender(); 
	private static SimpleBackwardChainer reasoner;
	private static Rule startingPoint;
	private static ReasoningThread reasoningThread;
	
	
	public static void setStartingPoint(Rule startingPoint) {
		ReasoningAccess.startingPoint = startingPoint;
	}
	
	public static SimpleBackwardChainer getReasoner() {
		return reasoner;
	}
	
	public static ListenableSuspender getSuspender() {
		return suspender;
	}

	public static void startDebugging() {
		reasoner = new SimpleBackwardChainer(DatamodelAccess.getKnowledgeBase(),suspender);
		reasoningThread = new ReasoningThread();
		reasoningThread.start();
	}
	
	
	private static class ReasoningThread extends Thread {
		
		public void run() {
			reasoner.hasProof(startingPoint.getBody()); //new thread!
		}
		
	}
	
}
