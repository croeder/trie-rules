package de.fzi.ipe.trie.proceduraldebugger.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;

public class ListenableSuspender extends ConfigurableSuspender {
	
	private Set<SuspendListener> listeners = new HashSet<SuspendListener>();
		
	@Override
	public synchronized void suspend(final Action a, final ExecutionTreeGoal goal, final Rule r) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (SuspendListener s: listeners) {
					try {
						s.suspending(a, goal, r);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}});
				
		super.suspend(a, goal, r);
	}
	
	@Override
	public synchronized void wake() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					for (SuspendListener s: listeners) s.waking();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
		super.wake();
	}
	
	public void addListener(SuspendListener list) {
		listeners.add(list);
	}
	
	public void removeListener(SuspendListener list) {
		listeners.remove(list);
	}

	public void stop() {
		super.stop(); 
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				try {
					for (SuspendListener s: listeners) s.suspending(Action.STOPPED,null, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}});
	}
}
