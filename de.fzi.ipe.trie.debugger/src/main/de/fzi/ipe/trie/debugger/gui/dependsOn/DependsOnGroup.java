package de.fzi.ipe.trie.debugger.gui.dependsOn;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.RuleProxy;
import de.fzi.ipe.trie.debugger.gui.RuleListContentProvider;
import de.fzi.ipe.trie.debugger.gui.TableTooltipManager;
import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomDeactivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;

public class DependsOnGroup implements DebuggerEventBusListener{

	private DebuggerRule rule;
	private DebuggerEventBus eventBus;
	private DebuggerRuleStore ruleStore;
	
	private TableViewer dependsOnViewer;
	private DependsOnLabelProvider labelProvider = new DependsOnLabelProvider();
	
	private static class TooltipLabelProvider extends LabelProvider {
		
		@Override
		public String getText(Object element) {
			DebuggerRule debuggerRule = (DebuggerRule) element;
			if (debuggerRule.getRule() instanceof RuleProxy) {
				RuleProxy rule = (RuleProxy) debuggerRule.getRule();
				return  "This rule was included based on the assumption that \n"+
						rule.getExplanation() + 
						"actually mean the same thing. ";				
			}
			else return null;
		}
		
	}
	
	
	public DependsOnGroup(Composite parent, DebuggerRuleStore ruleStore, final DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		this.ruleStore = ruleStore;
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
				eventBus.sendEvent(new SelectedRuleEvent(rule, SelectedRuleEvent.Source.DEPENDS_ON));
			}
		});
		
		new TableTooltipManager(dependsOnViewer.getTable(),new TooltipLabelProvider());
	}

	public void eventNotification(DebuggerEvent event) {
		if (event == RedrawEvent.DEPENDS_ON) {
			setRuleInGUIElements();
		}
		else if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent eventS = (SelectedRuleEvent) event;
			rule = eventS.getRule();
			eventBus.sendEvent(RedrawEvent.DEPENDS_ON);
		}
		else if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent afe = (AtomFocussedEvent) event;
			rule = afe.getRule();
			eventBus.sendEvent(RedrawEvent.DEPENDS_ON);
		}
		else if (event instanceof AtomActivatedEvent) {
			AtomActivatedEvent ave = (AtomActivatedEvent) event;
			rule = ave.getAtom().getRule();
			eventBus.sendEvent(RedrawEvent.DEPENDS_ON);
		}
		else if (event instanceof AtomDeactivatedEvent) {
			AtomDeactivatedEvent ave = (AtomDeactivatedEvent) event;
			rule = ave.getAtom().getRule();
			eventBus.sendEvent(RedrawEvent.DEPENDS_ON);
		}
		else if (event instanceof RefreshEvent) {
			if (rule != null) {
				rule = ruleStore.getRule(rule.getName());
				setRuleInGUIElements();
			}
		}
	}
	
	private void setRuleInGUIElements() {
		if (rule != null) {
			dependsOnViewer.setInput(rule.getAllPossibilities());
			labelProvider.setRulesThatSupplyBindings(rule.getRulesThatSupplyBindings());
		}
	}
	
}
