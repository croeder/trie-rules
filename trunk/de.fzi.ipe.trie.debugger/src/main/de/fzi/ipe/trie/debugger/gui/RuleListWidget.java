package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent.Source;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;

public class RuleListWidget implements ISelectionChangedListener, DebuggerEventBusListener{
	
	private ListViewer ruleList; 
	private DebuggerRuleStore ruleStore;
	private DebuggerEventBus eventBus;
	
	public RuleListWidget(Composite mainComposite, DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
		this.ruleStore = ruleStore;
		this.eventBus = eventBus;
		
		eventBus.addListener(this);
		
		Group ruleListGroup = new Group(mainComposite,SWT.V_SCROLL|SWT.H_SCROLL);
		ruleListGroup.setText("All Rules");
		GridData ruleListGD = new GridData();
		ruleListGD.verticalSpan = 2;
		ruleListGD.widthHint = 200;
		ruleListGD.verticalAlignment = GridData.FILL;
		ruleListGroup.setLayoutData(ruleListGD);
		ruleListGroup.setLayout(new FillLayout());
		
		ruleList = new ListViewer(ruleListGroup,SWT.NONE);
		ruleList.setContentProvider(new RuleListContentProvider());
		ruleList.setLabelProvider(new RuleListLabelProvider());
		ruleList.addSelectionChangedListener(this);
		
		ruleList.setInput(ruleStore.getRules());
	}


	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		DebuggerRule rule = (DebuggerRule) sel.getFirstElement();
		if (rule !=null) {
			ruleList.setSelection(null);
			eventBus.sendEvent(new SelectedRuleEvent(rule, Source.RULE_LIST));
		}
		
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof RefreshEvent) {
			ruleList.setInput(ruleStore.getRules());
		}
	}
	
	
	

}
