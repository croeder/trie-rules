package de.fzi.ipe.trie.proceduraldebugger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.fzi.ipe.trie.proceduraldebugger.ctrl.JumpAction;
import de.fzi.ipe.trie.proceduraldebugger.ctrl.StartDebuggingAction;
import de.fzi.ipe.trie.proceduraldebugger.ctrl.StepAction;
import de.fzi.ipe.trie.proceduraldebugger.ctrl.StopAction;
import de.fzi.ipe.trie.proceduraldebugger.gui.ConfigurationWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.CurrentRuleWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.ExecutionTreeWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.ExplanationWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.VariableBindingsWidget;


public class ProceduralDebugView extends ViewPart {
	
	public static final String VIEW_ID = "de.fzi.ipe.trie.proceduraldebugger.ProceduralDebugView";

	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite mainComposite = createMainComposite(parent);
		
		createTopButtonPanel(mainComposite);
		new ConfigurationWidget(mainComposite,60);
		new ExplanationWidget(mainComposite);
		CurrentRuleWidget currentRule = new CurrentRuleWidget(mainComposite,200);
		ExecutionTreeWidget et = new ExecutionTreeWidget(mainComposite,400);
		et.getLayoutData().verticalSpan = 2;
		et.getLayoutData().verticalAlignment = SWT.FILL;
		new VariableBindingsWidget(mainComposite,200);

		et.addSelectionChangedListener(currentRule);

		parent.layout(true);
	}


	private Composite createMainComposite(Composite parent) {
		ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		Composite mainComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(mainComposite);

		GridLayout layout = new GridLayout(2,true);
		mainComposite.setLayout(layout);
		return mainComposite;
	}
	
	
	private void createTopButtonPanel(Composite parent) {
		Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		RowData buttonLD = new RowData();
		buttonLD.height = 50;
		buttonLD.width = 50;
		
//		Button button = SelectRuleAction.getInstance(parent.getShell()).createButton(panel);
//		button.setLayoutData(buttonLD);
		
		Button button = StartDebuggingAction.getInstance(parent.getShell()).createButton(panel);
		button.setLayoutData(buttonLD);
		
		button = StepAction.getInstance().createButton(panel);
		button.setLayoutData(buttonLD);
		
		button = StopAction.getInstance().createButton(panel);
		button.setLayoutData(buttonLD);
		
		button = JumpAction.getInstance().createButton(panel);
		button.setLayoutData(buttonLD);
	}

	@Override
	public void setFocus() {
		;
	}

	
	
	
}
