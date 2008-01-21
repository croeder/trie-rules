package de.fzi.ipe.trie.debugger.gui.bindings;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.SelectedResultLineEvent;
import de.fzi.ipe.trie.inference.Result;

public class BindingsGroup {

	private TableViewer bindingsViewer;
	private DebuggerEventBus eventBus;
	
	public BindingsGroup(Composite parent, boolean dynamic_b, final RuleDebugContentProvider contentProvider, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
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
		Result result = contentProvider.getBindings();
		String[] sortedVariables = result.getSortedVariableNames().toArray(new String[0]);
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
		TableViewer bindingsViewer = new TableViewer(table);

		if (sortedVariables != null) {
			bindingsViewer.setColumnProperties(sortedVariables);
			for (int i = 0; i < sortedVariables.length; i++) {
				TableColumn column = new TableColumn(table, SWT.NONE, i);
				column.setText(sortedVariables[i]);
				column.setWidth(100);
			}
		}

		bindingsViewer.setContentProvider(new BindingsTableContentProvider());
		bindingsViewer.setLabelProvider(new BindingsTableLabelProvider(sortedVariables));

		bindingsViewer.setSorter(new ViewerSorter());

		bindingsViewer.setInput(result);
		bindingsViewer.getTable().setLinesVisible(true);
		bindingsViewer.getTable().setHeaderVisible(true);

		if (contentProvider.getCurrentResult() != null) {
			bindingsViewer.setSelection(new StructuredSelection(contentProvider.getCurrentResult()));
		}
		bindingsViewer.addSelectionChangedListener(new SelectionChangedListener());

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
					contentProvider.deselectResult();
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
	
	private class SelectionChangedListener implements ISelectionChangedListener {
		
		public void selectionChanged(SelectionChangedEvent event) {
			IStructuredSelection sel = (IStructuredSelection) event.getSelection();
			ResultLineProvider node = (ResultLineProvider) sel.getFirstElement();
			eventBus.sendEvent(new SelectedResultLineEvent(node));
		}
	}

}
