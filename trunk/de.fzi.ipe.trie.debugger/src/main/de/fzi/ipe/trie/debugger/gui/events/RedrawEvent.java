package de.fzi.ipe.trie.debugger.gui.events;


/**
 * Redraw events are treated differently from other events - redraw events are executed only after all other events, after
 * no other non-Redraw events are awaiting distribution.
 * In addition, each redraw events gets added only once to the event queue, if there's one thats equal to the one being 
 * added, then the new one is simply ignored. Two redraw events are equal iff their type is equal. 
 * @author zach
 *
 */
public class RedrawEvent implements DebuggerEvent{

	public static final RedrawEvent DEBUG_VIEW = new RedrawEvent("DEBUG_VIEW");
	public static final RedrawEvent RULE_DETAILS = new RedrawEvent("RULE_DETAILS");
	public static final RedrawEvent RULE_BINDINGS = new RedrawEvent("RULE_BINDINGS");
	public static final RedrawEvent DEPENDS_ON = new RedrawEvent("DEPENDS_ON");
	
	
	private String type;
	
	private RedrawEvent(String type) {
		this.type = type;
	}
	
	public String toString() {
		return type;
	}
	
	
}
