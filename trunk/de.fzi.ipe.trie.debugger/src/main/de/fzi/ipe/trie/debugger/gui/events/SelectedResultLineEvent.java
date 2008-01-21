package de.fzi.ipe.trie.debugger.gui.events;

import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;

public class SelectedResultLineEvent implements DebuggerEvent{

	private ResultLineProvider resultLineProvider;
	
	public SelectedResultLineEvent(ResultLineProvider resultLineProvider) {
		this.resultLineProvider = resultLineProvider;
	}

	public ResultLineProvider getResultLineProvider() {
		return resultLineProvider;
	}
	
}
