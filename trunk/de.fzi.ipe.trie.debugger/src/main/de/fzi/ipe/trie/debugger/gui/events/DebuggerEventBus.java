package de.fzi.ipe.trie.debugger.gui.events;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.swt.widgets.Display;

/**
 * A simple event bus that helps organize the ui. It guarantees that:
 *  - the notification methods are all called from the UI thread
 *  - that all elements receive the events in the same order
 * 
 * Its a bit tricky to have it work even if listeners are added during event distribution (frequently encountered in this program)
 * @author zach
 *
 */
public class DebuggerEventBus implements Runnable {

	private Queue<DebuggerEvent> eventsToDistribute = new ConcurrentLinkedQueue<DebuggerEvent>();
	private Set<DebuggerEventBusListener> listeners = new HashSet<DebuggerEventBusListener>();
	
	private Set<DebuggerEventBusListener> listeners_toAdd = new HashSet<DebuggerEventBusListener>(), listeners_toRemove=new HashSet<DebuggerEventBusListener>();
	
	public void sendEvent(DebuggerEvent event) {
		eventsToDistribute.add(event);
		Display.getCurrent().asyncExec(this);
	}
	
	public void addListener(DebuggerEventBusListener listener) {
		listeners_toAdd.add(listener);
	}
	
	public void removeListener(DebuggerEventBusListener listener) {
		listeners_toRemove.add(listener);
	}
	
	private void processListenerAddsRemoves() {
		if (listeners_toAdd.size()>0) {
			listeners.addAll(listeners_toAdd);
			listeners_toAdd.clear();
		}
		if (listeners_toRemove.size()>0) {
			listeners.removeAll(listeners_toRemove);
			listeners_toRemove.clear();
		}
	}
	
	public void run() {
		DebuggerEvent e = eventsToDistribute.poll();
		while (e != null) {
			processListenerAddsRemoves();
			for (DebuggerEventBusListener l:listeners) {
				try {
					l.eventNotification(e);
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			e = eventsToDistribute.poll();
		}
	}
	
}
