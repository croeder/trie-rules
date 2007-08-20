package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;


/**
 * Class responsible for stopping and restarting the backward chainer (needed for the suspendable
 * backward chainer when it is run as part of the procedural debugger.
 * @author zach
 */
public class ConfigurableSuspender implements Suspender{

	private boolean run = false;

	
	/**
	 * Method that stops the thread if the current state matches the conditions specified in the suspender. Restarts the thred on 
	 * actions by the user. This implementation stops always, other behavior can be achieved by overriding it. 
	 * @param a
	 * @param goal
	 * @param r
	 */
	public void performedAction(Action a, ExecutionTreeGoal goal, Rule r) {
		suspend(a,goal,r);
	}
	
	
	protected synchronized void suspend(Action a, ExecutionTreeGoal goal, Rule r) {
		while (!run) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				;
			}
		}
		run = false;
	}
	

	/**
	 * Tells the inference engine to continue processing.
	 */
	public synchronized void wake() {
		run = true;
		notify();
	}
	
	public void step() {
		wake();
	}
		
	
}
