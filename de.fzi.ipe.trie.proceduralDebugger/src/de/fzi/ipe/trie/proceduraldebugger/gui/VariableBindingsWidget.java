package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableColumn;

import de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider.VariableBindingsContentProvider;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.VariableBindingsTreeLabelProvider;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class VariableBindingsWidget extends ReasonerStateWidget{

	private TableViewer table;
	
	public VariableBindingsWidget(Composite parent) {
		super(parent,"Variable Bindings",305);
		table = new TableViewer(group);
		
		table.getTable().setHeaderVisible(true);
		TableColumn name = new TableColumn(table.getTable(), SWT.NONE);
		name.setText("Name");
		name.setWidth(80);
		TableColumn value = new TableColumn(table.getTable(), SWT.NONE);
		value.setText("Value");
		value.setWidth(250);
		
		table.setContentProvider(new VariableBindingsContentProvider());
		table.setLabelProvider(new VariableBindingsTreeLabelProvider());
		
	}

	@Override
	public void changedReasoner() {
		table.setInput(ReasoningAccess.getReasoner().getVariableBindings());
	}

	@Override
	public void refresh() {
		table.refresh();
	}
	
}
