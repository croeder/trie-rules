package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.LabelUtil;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.TextualRule;

public class CurrentRuleWidget implements ILabelProviderListener, ISelectionChangedListener{

	private Group group;
	private StyledText ruleText;
	private Color white = new Color(Display.getDefault(), 255, 255, 255);
	private Color commentGray = new Color(Display.getDefault(), 220,220,220);
	private Color highlight = new Color(Display.getDefault(), 232,242,254);
	
	private ExecutionTreeElement lastElement;
	private Rule lastRule;
	
	public CurrentRuleWidget(Composite parent,int height) {
		createGroup(parent,height);
		createRuleText();
//		ReasoningAccess.getSuspender().addListener(this);
		LabelUtil.addListener(null, this);
	}
	
	
	private void createRuleText() {
		ruleText = new StyledText(group,SWT.BORDER |SWT.MULTI | SWT.V_SCROLL | SWT.WRAP);
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

	public void suspending(Action a, ExecutionTreeElement element, Rule r) {
		lastRule = r;
		lastElement = element;
		TextualRule tr = null; 
		if (r != null) {
			if (element instanceof ExecutionTreeGoal) {
				tr = new TextualRule(r, (ExecutionTreeGoal)element);
			}
			else tr = new TextualRule(r,null); 
		}
		else if (element != null) { 
			tr = new TextualRule(element);
		}
		
		if (tr != null) {
			ruleText.setText(tr.toString());
			if (tr.getActiveGoalLine() != -1) {
				ruleText.setLineBackground(tr.getActiveGoalLine(), 1, highlight);
			}
			if (tr.getCommentBegin() != -1) {
				int begin = tr.getCommentBegin();
				int lineCount = (ruleText.getLineCount()-begin)-1;
				ruleText.setLineIndent(begin, lineCount, 5);
				ruleText.setLineBackground(begin, lineCount, commentGray);
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
		suspending(null,lastElement,lastRule);
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
