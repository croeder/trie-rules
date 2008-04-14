package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;

public class RuleDetailsGroup implements DebuggerEventBusListener {
	
	private DebuggerEventBus eventBus;
	private DebuggerRuleStore ruleStore; 
	private DebuggerRule rule;
	private Group clauses;
	
	private StyledTextView styledText;
	private Text comment;
	
	public RuleDetailsGroup(Composite parent, DebuggerRuleStore ruleStore, DebuggerEventBus eventBus) {
		this.eventBus = eventBus;
		this.ruleStore = ruleStore;
		eventBus.addListener(this);
		clauses = new Group(parent, SWT.NONE);
		clauses.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		clauses.setText("Rule Details");
		GridLayout layout = new GridLayout();
		layout.marginWidth = 10;
		layout.marginHeight = 10;
		clauses.setLayout(layout);
		
		styledText = new StyledTextView(clauses,eventBus);
		styledText.setLayoutData(new GridData(SWT.FILL, SWT.FILL,true,true));
		
		createCommentField();
	}


	private void createCommentField() {
		comment = new Text(clauses,SWT.MULTI | SWT.WRAP | SWT.READ_ONLY);
		comment.setBackground(clauses.getBackground());
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.heightHint = comment.getLineHeight()*2;
		comment.setLayoutData(gd);
	}


	private void updateDisplay(DebuggerRule rule) {
		this.rule = rule;
		styledText.reset();
		makeHeadClauses(styledText,rule);
		TextPart ifPart = new TextPartWord("IF ");
		styledText.addClause(ifPart, null);
		styledText.addNewLine();
		makeBodyClauses(styledText,rule);
		styledText.updateText();

		comment.setText(rule.getComment());
		
		GridData gridData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		gridData.minimumHeight = styledText.getSize().y + comment.getLineHeight()*2;
		gridData.minimumWidth = styledText.getSize().x;
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
		}
		else if (event instanceof RefreshEvent) {
			if (rule != null) {
				DebuggerRule newRule = ruleStore.getRule(rule.getRule().getName());
				updateDisplay(newRule);
				eventBus.sendEvent(RedrawEvent.DEBUG_VIEW);
			}
		}
	}

	
}
