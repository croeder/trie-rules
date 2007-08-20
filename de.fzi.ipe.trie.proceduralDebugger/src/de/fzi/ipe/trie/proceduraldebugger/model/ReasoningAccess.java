package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.suspendableReasoner.SuspendableBackwardChainer;

public class ReasoningAccess {

	//	needs to be final, because components are ataching listeners to it (and they won't know when it's replaced)
	private static final ListenableSuspender suspender = new ListenableSuspender(); 
	private static SuspendableBackwardChainer reasoner;
	private static Rule startingPoint;
	private static ReasoningThread reasoningThread;
	
	
	public static void setStartingPoint(Rule startingPoint) {
		ReasoningAccess.startingPoint = startingPoint;
	}
	
	public static SuspendableBackwardChainer getReasoner() {
		return reasoner;
	}
	
	public static ListenableSuspender getSuspender() {
		return suspender;
	}

	public static void startDebugging() {
		reasoner = new SuspendableBackwardChainer(DatamodelAccess.getKnowledgeBase(),suspender);
		reasoningThread = new ReasoningThread();
		reasoningThread.start();
	}
	
	
	private static class ReasoningThread extends Thread {
		
		public void run() {
			reasoner.hasProof(startingPoint.getBody()); //new thread!
		}
		
	}
	
}
