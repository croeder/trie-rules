package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.LabelUtil;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;

public class CurrentRuleWidget implements ILabelProviderListener, ISelectionChangedListener{

	private Group group;
	private Text ruleText;
	private Color white = new Color(Display.getDefault(), 255, 255, 255);
	
	private ExecutionTreeElement lastGoal;
	private Rule lastRule;
	
	public CurrentRuleWidget(Composite parent,int height) {
		createGroup(parent,height);
		createRuleText();
//		ReasoningAccess.getSuspender().addListener(this);
		LabelUtil.addListener(null, this);
	}
	
	
	private void createRuleText() {
		ruleText = new Text(group,SWT.BORDER |SWT.MULTI |SWT.H_SCROLL | SWT.V_SCROLL);
		ruleText.setEditable(false);
		ruleText.setBackground(white);
	}
	
	private void createGroup(Composite parent, int height) {
		group = new Group(parent, SWT.NONE);
		group.setText("Current Rule");
		group.setLayout(new FillLayout());

		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.horizontalAlignment = GridData.FILL;
		layoutData.heightHint = height;
		group.setLayoutData(layoutData);
	}

	public void suspending(Action a, ExecutionTreeElement goal, Rule r) {
		lastRule = r;
		lastGoal = goal;
		if (r != null) {
			ruleText.setText(LabelUtil.toString(r));
		}
		else if (goal != null) { 
			if (goal.getParent() instanceof ExecutionTreeQuery) {
				ruleText.setText(LabelUtil.toString((ExecutionTreeQuery)goal.getParent()));
			}
			else if (goal.getParent() instanceof ExecutionTreeRule) {
				ExecutionTreeRule rule = (ExecutionTreeRule) goal.getParent();
				ruleText.setText(LabelUtil.toString(rule));
			}
			else if (goal instanceof ExecutionTreeRule) {
				ruleText.setText(LabelUtil.toString((ExecutionTreeRule)goal));
			}
			else {
				ruleText.setText("??? "+goal.getParent().getClass().toString());
			}
		}
		else ruleText.setText("");
	}

	public void waking() {
		;
	}
	
	@Override
	protected void finalize() throws Throwable {
		white.dispose();
		super.finalize();
	}

	public GridData getLayoutData() {
		return (GridData) group.getLayoutData();
	}


	/**
	 * Makes a redraw of this componen after the state of the LabelProperty object changed.
	 */
	public void labelProviderChanged(LabelProviderChangedEvent event) {
		suspending(null,lastGoal,lastRule);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sel = (IStructuredSelection) event.getSelection();
		if (sel.getFirstElement() instanceof ExecutionTreeGoal) {
			suspending(null,(ExecutionTreeGoal) sel.getFirstElement() ,null);
		}
		else if (sel.getFirstElement() instanceof ExecutionTreeRule) {
			ExecutionTreeRule eRule = (ExecutionTreeRule) sel.getFirstElement() ;
			suspending(null, eRule, null);
		}
	}
	
}
