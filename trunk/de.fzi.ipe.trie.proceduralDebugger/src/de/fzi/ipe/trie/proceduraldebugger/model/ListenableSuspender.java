package de.fzi.ipe.trie.proceduraldebugger.model;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.widgets.Display;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.suspendableReasoner.Suspender;

public class ListenableSuspender extends Suspender {
	
	private Set<SuspendListener> listeners = new HashSet<SuspendListener>();
	
	@Override
	public synchronized void suspend(final Action a, final Atom goal, final Rule r) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (SuspendListener s: listeners) s.suspending(a, goal, r);
			}});
				
		super.suspend(a, goal, r);
	}
	
	@Override
	public synchronized void wake() {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (SuspendListener s: listeners) s.waking();
			}});
		super.wake();
	}
	
	public void addListener(SuspendListener list) {
		listeners.add(list);
	}
	
	public void removeListener(SuspendListener list) {
		listeners.remove(list);
	}

}
