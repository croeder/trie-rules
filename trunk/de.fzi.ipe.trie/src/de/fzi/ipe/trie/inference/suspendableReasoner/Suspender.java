package de.fzi.ipe.trie.inference.suspendableReasoner;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;


/**
 * Class responsible for stopping and restarting the backward chainer (needed for the suspendable
 * backward chainer when it is run as part of the procedural debugger.
 * @author zach
 */
public class Suspender {

	public enum Action { TryingGoal } ;

	private boolean run = false;

	
	/**
	 * Method that stops the thread if the current state matches the conditions specified in the suspender. Restarts the thred on 
	 * actions by the user. This implementation stops always, other behavior can be achieved by overriding it. 
	 * @param a
	 * @param goal
	 * @param r
	 */
	public void performedAction(Action a, Atom goal, Rule r) {
		suspend(a,goal,r);
	}
	
	
	protected synchronized void suspend(Action a, Atom goal, Rule r) {
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
