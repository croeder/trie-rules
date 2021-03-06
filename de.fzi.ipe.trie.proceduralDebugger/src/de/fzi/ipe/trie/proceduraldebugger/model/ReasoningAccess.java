package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.BackwardChainer;
import de.fzi.ipe.trie.inference.ProofVariable;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.executionTree.simple.SimpleBackwardChainer;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.LabelUtil;

public class ReasoningAccess {

	//	needs to be final, because components are attaching listeners to it (and they won't know when it's replaced)
	private static final ListenableSuspender suspender = new ListenableSuspender(); 
	private static BackwardChainer reasoner;
	private static Rule startingPoint;
	private static ReasoningThread reasoningThread;
	
	
	public static void setStartingPoint(Rule startingPoint) {
		ReasoningAccess.startingPoint = startingPoint;
	}
	
	public static BackwardChainer getReasoner() {
		return reasoner;
	}
	
	public static ListenableSuspender getSuspender() {
		return suspender;
	}

	public static void startDebugging() {
		stopDebugging();
		ProofVariable.resetCounter();
		LabelUtil.resetUsedVariableNames();
		reasoner = new SimpleBackwardChainer(DatamodelAccess.getKnowledgeBase(),suspender);
		reasoningThread = new ReasoningThread();
		reasoningThread.start();
	}
	
	public static void stopDebugging() {
		if (reasoningThread != null && reasoningThread.isAlive()) {
			suspender.stop();
			reasoningThread.interrupt();
		}
	}
	
	private static class ReasoningThread extends Thread {
		
		public void run() {
			try {
				reasoner.hasProof(startingPoint.getBody()); 
			} catch (Suspender.HardTerminate ht) {
				; //thats just the way the reasoner gets stopped. 
			}
		}
		
	}

	
}
