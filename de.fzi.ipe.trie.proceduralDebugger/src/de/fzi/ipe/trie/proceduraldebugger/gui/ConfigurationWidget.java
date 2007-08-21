package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.LabelUtil;
import de.fzi.ipe.trie.proceduraldebugger.model.ConfigurableSuspender;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;

public class ConfigurationWidget implements SelectionListener {

	private Group group;
	
	private Button hideUri,disableFoundRule,disableExit,disableFail;
	
	public ConfigurationWidget(Composite parent,int height) {
		createGroup(parent, height);
		
		hideUri = new Button(group,SWT.CHECK);
		hideUri.setText("Hide URI before #");
		hideUri.setSelection(LabelUtil.getHideBeforeHash());
		hideUri.addSelectionListener(this);

		ConfigurableSuspender suspender = ReasoningAccess.getSuspender();
		disableFoundRule = new Button(group,SWT.CHECK);
		disableFoundRule.setText("Ignore \"Found rule\" events");
		disableFoundRule.setSelection(suspender.isIgnored(Action.ADD_RULE_TO_EXECUTION_TREE));
		disableFoundRule.addSelectionListener(this);
		
		disableExit = new Button(group,SWT.CHECK);
		disableExit.setText("Ignore \"Found way to prove\" events");
		disableExit.setSelection(suspender.isIgnored(Action.EXIT_GOAL));
		disableExit.addSelectionListener(this);
		
		disableFail = new Button(group,SWT.CHECK);
		disableFail.setText("Ignore \"Failed to prove\" events");
		disableFail.setSelection(suspender.isIgnored(Action.FAIL_GOAL));
		disableFail.addSelectionListener(this);
		
	}
	
	public void widgetSelected(SelectionEvent e) {
		if (e.widget == hideUri) {
			LabelUtil.setHideBeforeHash(hideUri.getSelection());
		}
		else if (e.widget == disableFoundRule) {
			ReasoningAccess.getSuspender().setIgnore(Action.ADD_RULE_TO_EXECUTION_TREE, disableFoundRule.getSelection());
		}
		else if (e.widget == disableExit) {
			ReasoningAccess.getSuspender().setIgnore(Action.EXIT_GOAL, disableExit.getSelection());
		}
		else if (e.widget == disableFail) {
			ReasoningAccess.getSuspender().setIgnore(Action.FAIL_GOAL, disableFail.getSelection());
		}
	}

	private void createGroup(Composite parent, int height) {
		group = new Group(parent,SWT.NONE);
		group.setText("Configuration");
		group.setLayout(new GridLayout(2,true));

		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.heightHint = height;
		group.setLayoutData(layoutData);
	}

	public void widgetDefaultSelected(SelectionEvent e) {
		;
	}

	
}
