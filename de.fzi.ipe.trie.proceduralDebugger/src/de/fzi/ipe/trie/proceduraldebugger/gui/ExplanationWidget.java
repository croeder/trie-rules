package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.suspendableReasoner.Suspender.Action;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;

public class ExplanationWidget implements SuspendListener{

	private Group group;
	private Label title;
	private Label explanation;
	
	private Font largeFont;
	
	public ExplanationWidget(Composite parent) {
		createGroup(parent);
		createTitle(); 
		createExplanation();

		ReasoningAccess.getSuspender().addListener(this);
	}

	private void createExplanation() {
		explanation = new Label(group, SWT.WRAP);
		
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.heightHint = 50;
		explanation.setLayoutData(data);
	}

	private void createGroup(Composite parent) {
		group = new Group(parent, SWT.NONE);	
		group.setText("Current Action");

		GridLayout layout = new GridLayout(1,false);
		group.setLayout(layout);

		GridData layoutData = new GridData();
		layoutData.heightHint = 100;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.horizontalSpan = 2;
		group.setLayoutData(layoutData);
	}
	
	private void createTitle() {		
		title = new Label(group,SWT.NONE);

		GridData data = new GridData ();
		data.horizontalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		title.setLayoutData (data);
		
		FontData defaultFontData = title.getFont().getFontData()[0];
		FontData largeFontData = new FontData(defaultFontData.getName(),14,SWT.BOLD);
		largeFont = new Font(Display.getDefault(),largeFontData);
		title.setFont(largeFont);
	}

	public void setLayoutData(Object layoutData) {
		group.setLayoutData(layoutData);
	}
	
	public void suspending(Action a, Atom goal, Rule r) {
		title.setText(a.toString());
		explanation.setText("This here explains nothing!This here explains nothing!This here explains nothing!");
	}

	public void waking() {
		title.setText("Running ...");
		explanation.setText("Currently there is nothing to explain!");
	}

}
