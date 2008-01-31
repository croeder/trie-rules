package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.graphics.Point;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomDeactivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class AtomActivateButton extends CustomButton implements DebuggerEventBusListener{

	private DebuggerAtom atom;
	private boolean isActive = true;
	private DebuggerEventBus eventBus;
	
	
	public AtomActivateButton(Point upperLeftCorner, DebuggerAtom atom, DebuggerEventBus eventBus) {
		super(upperLeftCorner,"(De-)Activate this atom");
		this.atom = atom;
		isActive = atom.isActive();
		this.eventBus = eventBus;
		eventBus.addListener(this);
		setImage(DebuggerPlugin.IMAGE_MINUS);
	}
	
	@Override
	public void click() {
		isActive = !isActive;
		if (isActive) {
			setImage(DebuggerPlugin.IMAGE_MINUS);
			eventBus.sendEvent(new AtomActivatedEvent(atom));
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
		else {
			setImage(DebuggerPlugin.IMAGE_PLUS);
			eventBus.sendEvent(new AtomDeactivatedEvent(atom));
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
	}
		
	public void eventNotification(DebuggerEvent event) {
		if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent ase = (AtomFocussedEvent) event;
			if (ase.getAtom() == null) {
				setImage(DebuggerPlugin.IMAGE_MINUS);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
				isActive= true;
			}
			else if (ase.getAtom() != null && ase.getAtom() != atom) {
				isActive = false;
				setImage(DebuggerPlugin.IMAGE_PLUS);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
			}
		}
	}
	
	
	@Override	
	public void deregister() {
		eventBus.removeListener(this);
	}
	
}
