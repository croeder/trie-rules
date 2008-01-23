package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import static de.fzi.ipe.trie.debugger.gui.DebugGuiUtil.COLOR_CLAUSE_NOT_SATISFIED;
import static de.fzi.ipe.trie.debugger.gui.DebugGuiUtil.COLOR_CLAUSE_NO_BINDINGS;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.debugger.gui.events.AtomSelectedEvent;
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
		clauses.setLayout(new FillLayout());
		
		styledText = new StyledTextView(clauses);
	}


	private void updateDisplay(DebuggerRule rule) {
		styledText.reset();
		makeHeadClauses(styledText,rule);
		styledText.addNewLine();
		TextPart ifPart = new TextPart("IF ");
		styledText.add(ifPart);
		styledText.addNewLine();
		makeBodyClauses(styledText,rule);
		styledText.updateTextText();
	}
	
	private void makeHeadClauses(StyledTextView parent, DebuggerRule currentRule) {
		if (currentRule != null) {
			DebuggerAtom[] headPredicates = currentRule.getHeadClauses();
			for (int i = 0; i < headPredicates.length; i++) {
				DebuggerAtom currentClause = headPredicates[i];

				TextPart currentLabel = new TextPart(currentClause.toString());
				parent.add(currentLabel);

				if (i < (headPredicates.length - 1)) {
					TextPart and = new TextPart(" AND ");
					parent.add(and);
				}
				parent.addNewLine();
			}
		}
	}

	private void makeBodyClauses(StyledTextView parent,DebuggerRule currentRule){
		if (currentRule != null) {
			DebuggerAtom[] bodyPredicates = currentRule.getBodyClauses();
			for (int i = 0; i < bodyPredicates.length; i++) {
				final DebuggerAtom currentClause = bodyPredicates[i];
				TextPart currentTextPart = new TextPart(currentClause.toString());
				currentTextPart.addSelectionListener(new ClauseSelectionListener(currentClause));
				parent.add(currentTextPart);

//TODO
//				if ((contentProvider.getCurrentClause() != null) && (contentProvider.getCurrentClause().equals(currentClause))) {
//					currentTextPart.setBackground(COLOR_LABEL_SELECTED);
//					currentTextPart.setForeground(COLOR_LABEL_SELECTED_FOREGROUND);
//				} else {
//					currentTextPart.setBackground(COLOR_LABEL_SELECTABLE);
//					if ((doDynamic()) && (currentRule.hasCalculatedBindingForAllLiterals())) {
//						colorBodyClause(currentClause, currentTextPart); 
//					}
//				}
				if (i < (bodyPredicates.length - 1)) {
					TextPart and = new TextPart(" AND ");
					parent.add(and);
				}
				parent.addNewLine();
			}
		}
	}

	private void colorBodyClause(final DebuggerAtom currentClause, TextPart currentTextPart) {
		//color body clauses based on satisfiability.
		if (currentClause.getBindings().numberResults() == 0) {
			if (currentClause.getPossibilities().length == 0) {
				currentTextPart.setForeground(COLOR_CLAUSE_NOT_SATISFIED);
				currentTextPart.setToolTipText("This term currently has no variable bindings and there is no rule that could supply one");
			} else {
				currentTextPart.setForeground(COLOR_CLAUSE_NO_BINDINGS);
				currentTextPart.setToolTipText("This term currently has no variable bindings");
			}
		} else {
			currentTextPart.setToolTipText("This term has variable binding");
		}
	}
	
	private boolean doDynamic() {
		return true;
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

	private class ClauseSelectionListener implements SelectionListener {

		DebuggerAtom currentClause;
		boolean selected = false;

		public ClauseSelectionListener(DebuggerAtom currentClause) {
			this.currentClause = currentClause;
		}

		public void widgetSelected(SelectionEvent e) {
			if (selected) {
				selected = false;
				eventBus.sendEvent(new AtomSelectedEvent(null));
			} else {
				eventBus.sendEvent(new AtomSelectedEvent(currentClause));
				selected = true;
			}
		}

		/* (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		public void widgetDefaultSelected(SelectionEvent e) {
			;
		}
	}
	
}
