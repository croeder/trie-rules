package de.fzi.ipe.trie.debugger.gui;

import static de.fzi.ipe.trie.debugger.gui.DebugGuiUtil.FONT_HEADLINE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;

public class HeadlineComposite implements DebuggerEventBusListener{
	
	private Label headline; 
	
	public HeadlineComposite(Composite mainComposite, DebuggerEventBus eventBus) {
		eventBus.addListener(this);
		
		Composite headlineComposite = new Composite(mainComposite, SWT.NONE);
		headlineComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		headlineComposite.setLayout(new FillLayout());
		
		headline = new Label(headlineComposite, SWT.NONE);
		headline.setFont(FONT_HEADLINE);
		headline.setText("No Rule Selected");
	}
	
	
	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent selectedEvent = (SelectedRuleEvent) event;
			if (selectedEvent.getRule() != null) {
				headline.setText(selectedEvent.getRule().getName());				
			}
			else {
				headline.setText("No Rule Selected");
			}
		}
	} 
	

}
