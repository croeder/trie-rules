package de.fzi.ipe.trie.debugger.gui.dependsOn;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.RuleDebugContentProvider;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class DependsOnGroup {

	private TableViewer dependsOnViewer;
	
	public DependsOnGroup(Composite parent, final DebuggerEventBus eventBus, final RuleDebugContentProvider contentProvider) {
		IStructuredContentProvider listContentProvider = contentProvider.getDependsOnContentProvider();

		Group dependsOn = new Group(parent, SWT.NONE);
		dependsOn.setText("Depends on");
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		dependsOn.setLayout(layout);

		makeToolbar(dependsOn);

		Composite holder = new Composite(dependsOn, SWT.NONE);
		holder.setLayout(new FillLayout());
		holder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.FILL_BOTH));

		dependsOnViewer = new TableViewer(holder);
		dependsOnViewer.setContentProvider(listContentProvider);
		dependsOnViewer.setLabelProvider(new DependsOnLabelProvider(contentProvider.getCurrentRule().getRulesThatSupplyBindings()));
		dependsOnViewer.setInput("something");
		dependsOnViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection sel = (IStructuredSelection) event.getSelection();
				DebuggerRule rule = (DebuggerRule) sel.getFirstElement();
				eventBus.sendEvent(new SelectedRuleEvent(rule));
			}
		});
	}

	private void makeToolbar(Group dependsOn) {
		ToolBar toolbar = new ToolBar(dependsOn, SWT.FLAT);
		GridData toolbarGD = new GridData();
		toolbarGD.horizontalAlignment = GridData.END;
		toolbarGD.grabExcessHorizontalSpace = true;
		toolbar.setLayoutData(toolbarGD);
		ToolItem deselect = new ToolItem(toolbar, SWT.NONE);
		deselect.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (!dependsOnViewer.getSelection().isEmpty()) {
					dependsOnViewer.setSelection(StructuredSelection.EMPTY);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}
		});
		deselect.setImage(DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_DESELECT));
		deselect.setToolTipText("Deselect variable Bindings");
	}
	
}
