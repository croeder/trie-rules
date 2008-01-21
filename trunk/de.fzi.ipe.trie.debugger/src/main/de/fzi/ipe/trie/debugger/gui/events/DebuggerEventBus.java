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
 * @author zach
 *
 */
public class DebuggerEventBus implements Runnable {

	private Queue<DebuggerEvent> eventsToDistribute = new ConcurrentLinkedQueue<DebuggerEvent>();
	private Set<DebuggerEventBusListener> listeners = new HashSet<DebuggerEventBusListener>();
	
	
	public void sendEvent(DebuggerEvent event) {
		eventsToDistribute.add(event);
		Display.getCurrent().asyncExec(this);
	}
	
	public void addListener(DebuggerEventBusListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(DebuggerEventBusListener listener) {
		this.listeners.remove(listener);
	}
	
	public void run() {
		DebuggerEvent e = eventsToDistribute.poll();
		while (e != null) {
			for (DebuggerEventBusListener listener:listeners) {
				listener.eventNotification(e);
			}
			e = eventsToDistribute.poll();
		}
	}
	
}
