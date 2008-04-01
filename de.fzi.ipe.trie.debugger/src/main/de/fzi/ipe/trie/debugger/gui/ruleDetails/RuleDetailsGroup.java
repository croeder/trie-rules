package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class RuleDetailsGroup implements DebuggerEventBusListener {
	
	private DebuggerEventBus eventBus;
	private Group clauses;
	
	private StyledTextView styledText;
	
	public RuleDetailsGroup(Composite parent, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		eventBus.addListener(this);
		clauses = new Group(parent, SWT.NONE);
		clauses.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		clauses.setText("Rule Details");
		FillLayout layout = new FillLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		clauses.setLayout(layout);
		
		styledText = new StyledTextView(clauses,eventBus);
	}


	private void updateDisplay(DebuggerRule rule) {
		styledText.reset();
		makeHeadClauses(styledText,rule);
		TextPart ifPart = new TextPartWord("IF ");
		styledText.addClause(ifPart, null);
		styledText.addNewLine();
		makeBodyClauses(styledText,rule);
		styledText.updateText();
	}
	
	private void makeHeadClauses(StyledTextView parent, DebuggerRule currentRule) {
		if (currentRule != null) {
			List<DebuggerAtom> headPredicates = currentRule.getHeadClauses();
			for (int i=0;i<headPredicates.size();i++) {
				DebuggerAtom currentClause = headPredicates.get(i);
				TextPart currentLabel = new TextPartWord(currentClause.toString());
				parent.addClause(currentLabel,null);

				if (i < (headPredicates.size ()- 1)) {
					TextPart and = new TextPartWord(" AND ");
					parent.addClause(and,null);
				}
				parent.addNewLine();
			}
		}
	}

	private void makeBodyClauses(StyledTextView parent,DebuggerRule currentRule){
		if (currentRule != null) {
			List<DebuggerAtom> bodyPredicates = currentRule.getBodyClauses();
			for (int i = 0; i < bodyPredicates.size(); i++) {
				final DebuggerAtom currentClause = bodyPredicates.get(i);
				TextPartWord currentTextPart = new TextPartAtom(currentClause,eventBus);
				parent.addClause(currentTextPart,currentClause);

				if (i < (bodyPredicates.size()- 1)) {
					TextPart and = new TextPartWord(" AND ");
					parent.addClause(and,null);
					parent.addNewLine();
				}
			}
		}
	}

	public Point getSize() {
		return clauses.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		
	}
	
	
	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			SelectedRuleEvent sEvent = (SelectedRuleEvent) event;
			updateDisplay(sEvent.getRule());
			GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
			gridData.minimumHeight = styledText.getSize().y;
			gridData.minimumWidth = styledText.getSize().x;
		}
	}

	
}
