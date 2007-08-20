package de.fzi.ipe.trie.proceduraldebugger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.fzi.ipe.trie.proceduraldebugger.ctrl.SelectRuleAction;
import de.fzi.ipe.trie.proceduraldebugger.ctrl.StartDebuggingAction;
import de.fzi.ipe.trie.proceduraldebugger.ctrl.StepAction;
import de.fzi.ipe.trie.proceduraldebugger.gui.ExplanationWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.GoalStackWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.ProofTraceWidget;
import de.fzi.ipe.trie.proceduraldebugger.gui.VariableBindingsWidget;


public class ProceduralDebugView extends ViewPart {
	
	public static final String VIEW_ID = "de.fzi.ipe.trie.proceduraldebugger.ProceduralDebugView";

	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		Composite mainComposite = createMainComposite(parent);
		
		createTopButtonPanel(mainComposite);
		new ExplanationWidget(mainComposite);
		VariableBindingsWidget vb = new VariableBindingsWidget(mainComposite);
		vb.getLayoutData().verticalSpan = 2;
		vb.getLayoutData().verticalAlignment = SWT.FILL;
		new ProofTraceWidget(mainComposite);
		new GoalStackWidget(mainComposite);
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
		
		GridData layoutData = new GridData();
		layoutData.horizontalSpan = 2;
		panel.setLayoutData(layoutData);
		
		SelectRuleAction.getInstance(parent.getShell()).createButton(panel);
		StartDebuggingAction.getInstance().createButton(panel);
		StepAction.getInstance().createButton(panel);
	}

	@Override
	public void setFocus() {
		;
	}

	
	
	
}