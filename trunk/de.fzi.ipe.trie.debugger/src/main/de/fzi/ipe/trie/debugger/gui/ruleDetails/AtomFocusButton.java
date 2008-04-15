package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.graphics.Point;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class AtomFocusButton extends CustomButton implements DebuggerEventBusListener {

	private DebuggerAtom atom;
	private boolean isFocussed = false;
	private DebuggerEventBus eventBus;
	
	
	
	public AtomFocusButton(Point upperLeftCorner,DebuggerAtom atom, DebuggerEventBus eventBus) {
		super(upperLeftCorner, "Focus on this atom");
		this.atom = atom;
		this.eventBus = eventBus;
		eventBus.addListener(this);
		setImage(DebuggerPlugin.IMAGE_FOCUS);
	}

	@Override
	public void click() {
		isFocussed = !isFocussed;
		if (isFocussed) {
			setImage(DebuggerPlugin.IMAGE_REMOVE_GRAY_SMALL);
			eventBus.sendEvent(new AtomFocussedEvent(atom));
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
		else {
			setImage(DebuggerPlugin.IMAGE_FOCUS);
			eventBus.sendEvent(new AtomFocussedEvent(null,atom.getRule()));
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent ase = (AtomFocussedEvent) event;
			if (ase.getAtom() == null) {
				setImage(DebuggerPlugin.IMAGE_FOCUS);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
				isFocussed = false;
			}
			else if (ase.getAtom() != null && ase.getAtom() != atom) {
				isFocussed = false;
				setImage(DebuggerPlugin.IMAGE_FOCUS);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
			}
		}
		else if (event instanceof AtomActivatedEvent) {
			isFocussed = false;
			setImage(DebuggerPlugin.IMAGE_FOCUS);
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
	}
	
	
	@Override
	public void deregister() {
		eventBus.removeListener(this);
	}
	
}
