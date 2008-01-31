package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomDeactivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;

public class TextPartAtom extends TextPartWord implements DebuggerEventBusListener{

	public static final Color COLOR_ACTIVE_DEFAULT = new Color(Display.getDefault(), 0, 0, 0);
	public static final Color COLOR_ACTIVE_NO_BINDINGS = new Color(Display.getDefault(), 253, 12, 0);	
	public static final Color COLOR_ACTIVE_NO_POSSIBILITIES = new Color(Display.getDefault(), 254, 0, 179);	

	public static final Color COLOR_INACTIVE_DEFAULT = new Color(Display.getDefault(), 178, 178, 178);
	public static final Color COLOR_INACTIVE_NO_BINDINGS = new Color(Display.getDefault(), 216, 153, 150);	
	public static final Color COLOR_INACTIVE_NO_POSSIBILITIES = new Color(Display.getDefault(), 251, 166, 226);	

	
	private DebuggerAtom atom;
	private Color background = COLOR_DEFAULT_BACKGROUND;
	private DebuggerEventBus eventBus;
	
	
	public TextPartAtom(DebuggerAtom atom, DebuggerEventBus eventBus) {
		super(atom.toString());
		this.atom = atom;
		this.eventBus = eventBus;
		this.eventBus.addListener(this);
	}

	public StyleRange getStyleRange() {
		StyleRange currentStyleRange = new StyleRange();
		currentStyleRange.start = getOffset();
		currentStyleRange.length = getLength();
		currentStyleRange.foreground = getForeground();
		currentStyleRange.background = background;
		return currentStyleRange;
	}

	
	private Color getForeground() {
		if (atom.isActive()) {
			if (atom.getBindings().numberResults()> 0) return COLOR_ACTIVE_DEFAULT;
			else if (atom.getPossibilities().length > 0) return COLOR_ACTIVE_NO_BINDINGS;
			else return COLOR_ACTIVE_NO_POSSIBILITIES;
		}
		else {
			if (atom.getBindings().numberResults()> 0) return COLOR_INACTIVE_DEFAULT;
			else if (atom.getPossibilities().length > 0) return COLOR_INACTIVE_NO_BINDINGS;
			else return COLOR_INACTIVE_NO_POSSIBILITIES;			
		}
	}
	
	private Color getBackground() {
		return COLOR_DEFAULT_BACKGROUND;
	}

	public void clicked() {
		//TODO
	}
	
	@Override
	public void deregister() {
		eventBus.removeListener(this);
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent afe = (AtomFocussedEvent) event;
			if (afe.getAtom() == null || afe.getAtom() == atom) atom.setIsActive(true);
			else atom.setIsActive(false);
			eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
		}
		else if (event instanceof AtomActivatedEvent) {
			AtomActivatedEvent ave = (AtomActivatedEvent) event;
			if (ave.getAtom() == atom) {
				atom.setIsActive(true);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
			}
		}
		else if (event instanceof AtomDeactivatedEvent) {
			AtomDeactivatedEvent ade = (AtomDeactivatedEvent) event;
			if (ade.getAtom() == atom) {
				atom.setIsActive(false);
				eventBus.sendEvent(RedrawEvent.RULE_DETAILS);
			}
		}
	}

}
