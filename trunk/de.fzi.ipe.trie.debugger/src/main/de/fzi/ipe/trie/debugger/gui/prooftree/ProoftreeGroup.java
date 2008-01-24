package de.fzi.ipe.trie.debugger.gui.prooftree;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedResultLineEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeRuleNode;

public class ProoftreeGroup implements DebuggerEventBusListener {

	private ProoftreeTreeViewer prooftreeTreeViewer;
	private DebuggerEventBus eventBus;
	
	public ProoftreeGroup(Composite parent, boolean showProoftree, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.addListener(this);
		
		Group bindings = new Group(parent, SWT.NONE);
		bindings.setText("Prooftree");
		bindings.setLayout(new FillLayout());

		if (showProoftree) {
			prooftreeTreeViewer= new ProoftreeTreeViewer(bindings);
			prooftreeTreeViewer.addDoubleClickListener(new IDoubleClickListener() {
				public void doubleClick(DoubleClickEvent event) {
					IStructuredSelection sel = (IStructuredSelection) event.getSelection();
					ProoftreeNode node = (ProoftreeNode) sel.getFirstElement();
					if (node instanceof ProoftreeRuleNode) {
						Rule rule = ((ProoftreeRuleNode) node).getRule();
						SelectedRuleEvent e = new SelectedRuleEvent(DebuggerRuleStore.getRule(rule));
						ProoftreeGroup.this.eventBus.sendEvent(e);
					}
				}
			});
			prooftreeTreeViewer.displayProoftree(null);
		} else {
			bindings.setLayout(new RowLayout(SWT.HORIZONTAL));
			bindings.setEnabled(false);
			Label label = new Label(bindings, SWT.NONE);
			label.setImage(DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_PROOFTREE));
			Label label2 = new Label(bindings, SWT.WRAP);
			label2.setText("Enable \"Show Prooftree\" to see the inference \nchain that led to the current result.");
		}
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedResultLineEvent) {
			SelectedResultLineEvent resultLineEvent = (SelectedResultLineEvent) event;
			prooftreeTreeViewer.displayProoftree(resultLineEvent.getResultLineProvider().getProoftree());
		}
		else if (event instanceof SelectedRuleEvent) {
			prooftreeTreeViewer.displayProoftree(null);
		}
	}
	
}
