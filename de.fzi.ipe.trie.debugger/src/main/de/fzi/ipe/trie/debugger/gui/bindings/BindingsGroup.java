package de.fzi.ipe.trie.debugger.gui.bindings;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;
import de.fzi.ipe.trie.debugger.gui.events.AtomActivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomDeactivatedEvent;
import de.fzi.ipe.trie.debugger.gui.events.AtomFocussedEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedResultLineEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;
import de.fzi.ipe.trie.inference.Result;

public class BindingsGroup implements DebuggerEventBusListener{

	private TableViewer bindingsViewer;
	private DebuggerEventBus eventBus;
	private DebuggerRuleStore ruleStore;
	private BindingsTableLabelProvider labelProvider; 
	
	private DebuggerRule rule;
	
	public BindingsGroup(Composite parent, boolean dynamic_b, DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		this.ruleStore = ruleStore;
		eventBus.addListener(this);
		Group bindings = new Group(parent, SWT.NONE);
		bindings.setText("The Current Rule Fires For: ");

		if (dynamic_b == true) {
			makeBindingsTable(bindings);
		} else {
			makeNonDynamicMessage(bindings);
		}
	}

	private void makeBindingsTable(Group bindings) {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		bindings.setLayout(layout);

		Composite holder = new Composite(bindings, SWT.NONE);
		holder.setLayout(new FillLayout());
		holder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		Table table = new Table(holder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayout(new TableLayout());
		bindingsViewer = new TableViewer(table);

		labelProvider = new BindingsTableLabelProvider();
		bindingsViewer.setContentProvider(new BindingsTableContentProvider());
		bindingsViewer.setLabelProvider(labelProvider);
		bindingsViewer.getTable().setLinesVisible(true);
		bindingsViewer.getTable().setHeaderVisible(true);

		bindingsViewer.addSelectionChangedListener(new SelectionChangedListener());
	}

	private void setResult(Result result) {
		String[] sortedVariables = result.getSortedVariableNames().toArray(new String[0]);
		bindingsViewer.setFilters(new ViewerFilter[]{new ThresholdFilter()});
		labelProvider.setSortedVariables(sortedVariables);
		for (TableColumn tc:bindingsViewer.getTable().getColumns()) {
			tc.dispose();
		}
		if (sortedVariables != null) {
			bindingsViewer.setColumnProperties(sortedVariables);
			for (int i = 0; i < sortedVariables.length; i++) {
				TableColumn column = new TableColumn(bindingsViewer.getTable(), SWT.NONE, i);
				column.setText(sortedVariables[i]);
				column.setWidth(150);
			}
		}
		bindingsViewer.setInput(result);
		Object elementAt = bindingsViewer.getElementAt(0);
		if (elementAt !=null) bindingsViewer.setSelection(new StructuredSelection(elementAt));
	}


	private void makeNonDynamicMessage(Group bindings) {
		bindings.setLayout(new RowLayout(SWT.HORIZONTAL));
		bindings.setEnabled(false);
		Label label = new Label(bindings, SWT.NONE);
		label.setImage(DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_DYNAMIC));
		Label label2 = new Label(bindings, SWT.NONE);
		label2.setText("Enable \"Dynamic Relations\" to show variable bindings");
	}
	
	public void eventNotification(DebuggerEvent event) {
		if (event == RedrawEvent.RULE_BINDINGS) {
			if (rule != null) setResult(rule.getBindings());
		}
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent eventS = (SelectedRuleEvent) event;
			rule = eventS.getRule();
			eventBus.sendEvent(RedrawEvent.RULE_BINDINGS);
		}
		else if (event instanceof AtomFocussedEvent) {
			AtomFocussedEvent afe = (AtomFocussedEvent) event;
			rule = afe.getRule();
			eventBus.sendEvent(RedrawEvent.RULE_BINDINGS);
		}
		else if (event instanceof AtomActivatedEvent) {
			AtomActivatedEvent ave = (AtomActivatedEvent) event;
			rule = ave.getAtom().getRule();
			eventBus.sendEvent(RedrawEvent.RULE_BINDINGS);
		}
		else if (event instanceof AtomDeactivatedEvent) {
			AtomDeactivatedEvent ave = (AtomDeactivatedEvent) event;
			rule = ave.getAtom().getRule();
			eventBus.sendEvent(RedrawEvent.RULE_BINDINGS);
		}
		else if (event instanceof RefreshEvent) {
			if (rule != null) {
				rule = ruleStore.getRule(rule.getName());
				eventBus.sendEvent(RedrawEvent.RULE_BINDINGS);
			}
		}
	}
	
	private class SelectionChangedListener implements ISelectionChangedListener {
		
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			ResultLineProvider node = (ResultLineProvider) sel.getFirstElement();
			eventBus.sendEvent(new SelectedResultLineEvent(node));
		}
	}
	
	private static class ThresholdFilter extends ViewerFilter {

		Set<ResultLineProvider> providers = new HashSet<ResultLineProvider>();
		static final double THRESHOLD = 0.7d;
		static final double THRESHOLD_DIFF = 0.01d;
		
		boolean sameResultAlreadyBetter(ResultLineProvider currentLine) {
			for (ResultLineProvider older:providers) {
				if (currentLine.sameBindings(older)) {
					if (!older.basedOnAssumption()) return true;
					else if (older.getProoftree().getGrounding() - currentLine.getProoftree().getGrounding() > THRESHOLD_DIFF) {
						return true;
					}
				}
			}
			providers.add(currentLine);
			return false;
		}
		
		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			ResultLineProvider currentLine = (ResultLineProvider) element;
			if (!currentLine.basedOnAssumption()) {
				providers.add(currentLine);
				return true;
			}
			else if (currentLine.getProoftree().getGrounding() > THRESHOLD){
				return !sameResultAlreadyBetter(currentLine);
			}
			else return false;
		}
	}

}
