package de.fzi.ipe.trie.debugger.gui.bindings;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;
import de.fzi.ipe.trie.debugger.gui.RuleDebugContentProvider;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedResultLineEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.inference.Result;

public class BindingsGroup implements DebuggerEventBusListener{

	private TableViewer bindingsViewer;
	private DebuggerEventBus eventBus;
	private BindingsTableLabelProvider labelProvider; 
	
	public BindingsGroup(Composite parent, boolean dynamic_b, final RuleDebugContentProvider contentProvider, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.addListener(this);
		Group bindings = new Group(parent, SWT.NONE);
		if (contentProvider.getCurrentClause() != null) bindings.setText("The Selected Rule Part Fires For: ");
		else bindings.setText("The Current Rule Fires For: ");

		if (dynamic_b == true) {
			makeBindingsTable(contentProvider, bindings);
		} else {
			makeNonDynamicMessage(bindings);
		}
	}

	private void makeBindingsTable(final RuleDebugContentProvider contentProvider, Group bindings) {
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		bindings.setLayout(layout);
		makeToolbar(contentProvider, bindings);

		Composite holder = new Composite(bindings, SWT.NONE);
		holder.setLayout(new FillLayout());
		holder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		Table table = new Table(holder, SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		table.setLayout(new TableLayout());
		bindingsViewer = new TableViewer(table);

		labelProvider = new BindingsTableLabelProvider();
		bindingsViewer.setContentProvider(new BindingsTableContentProvider());
		bindingsViewer.setLabelProvider(labelProvider);
		bindingsViewer.setSorter(new ViewerSorter());
		bindingsViewer.getTable().setLinesVisible(true);
		bindingsViewer.getTable().setHeaderVisible(true);

		bindingsViewer.addSelectionChangedListener(new SelectionChangedListener());
	}

	private void setResult(Result result) {
		String[] sortedVariables = result.getSortedVariableNames().toArray(new String[0]);
		labelProvider.setSortedVariables(sortedVariables);
		for (TableColumn tc:bindingsViewer.getTable().getColumns()) {
			tc.dispose();
		}
//		bindingsViewer.getTable().removeAll();
		if (sortedVariables != null) {
			bindingsViewer.setColumnProperties(sortedVariables);
			for (int i = 0; i < sortedVariables.length; i++) {
				TableColumn column = new TableColumn(bindingsViewer.getTable(), SWT.NONE, i);
				column.setText(sortedVariables[i]);
				column.setWidth(100);
			}
		}
		
		bindingsViewer.setInput(result);
	}

	private void makeToolbar(final RuleDebugContentProvider contentProvider, Group bindings) {
		ToolBar toolbar = new ToolBar(bindings, SWT.FLAT);
		GridData toolbarGD = new GridData();
		toolbarGD.horizontalAlignment = GridData.END;
		toolbarGD.grabExcessHorizontalSpace = true;
		toolbar.setLayoutData(toolbarGD);
		ToolItem deselect = new ToolItem(toolbar, SWT.NONE);
		deselect.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (!bindingsViewer.getSelection().isEmpty()) {
					eventBus.sendEvent(new SelectedResultLineEvent(null));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}
		});
		deselect.setImage(DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_DESELECT));
		deselect.setToolTipText("Deselect variable Bindings");
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
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent eventS = (SelectedRuleEvent) event;
			DebuggerRule rule = eventS.getRule();
			setResult(rule.getBindings());
		}
	}
	
	private class SelectionChangedListener implements ISelectionChangedListener {
		
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			ResultLineProvider node = (ResultLineProvider) sel.getFirstElement();
			eventBus.sendEvent(new SelectedResultLineEvent(node));
		}
	}

}
