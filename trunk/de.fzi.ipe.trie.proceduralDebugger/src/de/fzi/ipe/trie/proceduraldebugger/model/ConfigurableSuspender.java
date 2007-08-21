package de.fzi.ipe.trie.proceduraldebugger.model;

import java.util.HashSet;
import java.util.Set;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;


/**
 * Class responsible for stopping and restarting the backward chainer (needed for the suspendable
 * backward chainer when it is run as part of the procedural debugger.
 * @author zach
 */
public class ConfigurableSuspender extends Suspender{

	private boolean run = false, toStop = false;

	private ExecutionTreeGoal lastGoal;	
	private ExecutionTreeElement jumpTarget;
	
	private Set<Action> ignoredActions = new HashSet<Action>();
	
	public boolean isIgnored(Action a) {
		return ignoredActions.contains(a);
	}
	
	public void setIgnore(Action a, boolean ignore) {
		if (ignore) ignoredActions.add(a);
		else ignoredActions.remove(a);
	}
		
	public ConfigurableSuspender() {
		ignoredActions.add(Action.ADD_RULE_TO_EXECUTION_TREE);
	}
	
	/**
	 * Method that stops the thread if the current state matches the conditions specified in the suspender. Restarts the thred on 
	 * actions by the user. This implementation stops always, other behavior can be achieved by overriding it. 
	 * @param a
	 * @param goal
	 * @param r
	 */
	public void performedAction(Action a, ExecutionTreeGoal goal, Rule r) {
		if (toStop) {
			toStop = false;
			throw new HardTerminate();
		}
		if (ignoredActions.contains(a)) return;
		if (goal != null && jumpTarget != null) {
			if (a != Action.CALLING_GOAL && a != Action.RETRY_GOAL) return; 
			if (goal.getParent() != jumpTarget && goal != jumpTarget.getParent()) {
				return;
			}
			jumpTarget = null;
		}
		lastGoal = goal;
		suspend(a,goal,r);
	}
	
	
	protected synchronized void suspend(Action a, ExecutionTreeGoal goal, Rule r) {
		while (!run) {
			try {
				this.wait();
			} catch (InterruptedException e) {
				if (toStop) {
					toStop = false;
					throw new HardTerminate();
				}
			}
		}
		run = false;
	}
	

	/**
	 * Resumes processing until the next Action with the same parent goal as the last one 
	 * (or alternatively: the next action that has a goal equal to that of the parent of the last goal)
	 */
	public synchronized void jump() {
		if (lastGoal != null) jumpTarget = lastGoal.getParent();
		wake();
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


	public void stop() {
		toStop = true;
		run = false;
		jumpTarget = null;
		super.stop();
	}
	
		
	
}
