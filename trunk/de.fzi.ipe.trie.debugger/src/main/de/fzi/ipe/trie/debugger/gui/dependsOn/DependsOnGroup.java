package de.fzi.ipe.trie.debugger.gui.dependsOn;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.debugger.gui.RuleListContentProvider;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class DependsOnGroup implements DebuggerEventBusListener{

	private TableViewer dependsOnViewer;
	private DependsOnLabelProvider labelProvider = new DependsOnLabelProvider();
	
	public DependsOnGroup(Composite parent, final DebuggerEventBus eventBus) {
		eventBus.addListener(this);
		
		Group dependsOn = new Group(parent, SWT.NONE);
		dependsOn.setText("Depends on");
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		dependsOn.setLayout(layout);

		Composite holder = new Composite(dependsOn, SWT.NONE);
		holder.setLayout(new FillLayout());
		holder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		dependsOnViewer = new TableViewer(holder);
		dependsOnViewer.setContentProvider(new RuleListContentProvider());
		dependsOnViewer.setLabelProvider(labelProvider);
		dependsOnViewer.setInput(null);
		dependsOnViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				DebuggerRule rule = (DebuggerRule) sel.getFirstElement();
				eventBus.sendEvent(new SelectedRuleEvent(rule));
			}
		});
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sre = (SelectedRuleEvent) event;
			dependsOnViewer.setInput(sre.getRule().getAllPossibilities());
			labelProvider.setRulesThatSupplyBindings(sre.getRule().getRulesThatSupplyBindings());
		}
	}
	
}
