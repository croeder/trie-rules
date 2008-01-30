package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.graphics.Point;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class ClauseFocusButton extends CustomButton implements DebuggerEventBusListener {

	private DebuggerAtom atom;
	private boolean isFocussed = false;
	private DebuggerEventBus eventBus;
	
	
	
	public ClauseFocusButton(Point upperLeftCorner,DebuggerAtom atom, DebuggerEventBus eventBus) {
		super(upperLeftCorner, "Focus on this clause");
		this.atom = atom;
		this.eventBus = eventBus;
		eventBus.addListener(this);
		setImage(DebuggerPlugin.IMAGE_FOCUS_PURPLE);
	}

	@Override
	public void click() {
		isFocussed = !isFocussed;
		if (isFocussed) {
			setImage(DebuggerPlugin.IMAGE_REMOVE_GRAY_SMALL);
			eventBus.sendEvent(new AtomFocussedEvent(atom));
		}
		else {
			setImage(DebuggerPlugin.IMAGE_FOCUS_PURPLE);
			eventBus.sendEvent(new AtomFocussedEvent(null));
		}
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent ase = (AtomFocussedEvent) event;
			if (ase.getAtom() == null) {
				atom.setIsActive(true);
				setImage(DebuggerPlugin.IMAGE_FOCUS_PURPLE);
				isFocussed = false;
			}
			else if (ase.getAtom() != null && ase.getAtom() != atom) {
				atom.setIsActive(false);
				setImage(DebuggerPlugin.IMAGE_FOCUS_PURPLE);
			}
		}
		else if (event instanceof AtomActivatedEvent) {
			isFocussed = false;
			setImage(DebuggerPlugin.IMAGE_FOCUS_PURPLE);
		}
	}
	
	
	@Override
	public void deregister() {
		eventBus.removeListener(this);
	}
	
}
