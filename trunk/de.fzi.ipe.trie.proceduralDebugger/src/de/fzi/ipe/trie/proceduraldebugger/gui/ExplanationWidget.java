package de.fzi.ipe.trie.proceduraldebugger.gui;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.Suspender;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider.LabelUtil;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;

public class ExplanationWidget implements SuspendListener, ILabelProviderListener{

	private Group group;
	private Label title;
	private Label explanation;
	
	private ExecutionTreeGoal lastGoal;
	private Suspender.Action lastAction;
	private Rule lastRule;
	
	private Font largeFont;
	
	public ExplanationWidget(Composite parent) {
		createGroup(parent);
		createTitle(); 
		createExplanation();

		LabelUtil.addListener(null, this);
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
	
	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		this.lastAction = a;
		this.lastGoal = goal;
		this.lastRule = r;
		if (a == Action.CALLING_GOAL)  {
			title.setText("Trying to prove "+LabelUtil.toString(goal.getGoal()));
			StringBuilder text = new StringBuilder();
			text.append("The inference engine is trying to prove the goal ");
			text.append(LabelUtil.toString(goal.getGoal()));
			text.append(".");
			explanation.setText(text.toString());
		}
		else if (a == Action.RETRY_GOAL) {
			title.setText("Retrying to prove "+LabelUtil.toString(goal.getGoal()));
			StringBuilder text = new StringBuilder();
			text.append("The inference engine is again trying to prove the goal ");
			text.append(LabelUtil.toString(goal.getGoal()));
			text.append(" to find more terms satisfying it. ");
			explanation.setText(text.toString());
		}
		else if (a == Action.SUCCESS) {
			title.setText("Found answer to the query!");
			explanation.setText("The Variable Bindings field shows the result. The inference engine will continue to try to find more answers.");
		}
		else if (a == Action.END) {
			title.setText("Finished");
			explanation.setText("The inference engine has tried everything and cannot find more results.	");
		}
		else if (a == Action.FAIL_GOAL) {
			title.setText("Failed to prove "+LabelUtil.toString(goal.getGoal()));
			explanation.setText("Failed to get any (more) results for this goal. The inference engine will backtrack to try to get more results for a previous goal (or give up, if there's nothing else to try).");
		}
		else if (a==Action.EXIT_GOAL) {
			title.setText("Found (another) way to prove "+LabelUtil.toString(goal.getGoal()));
			explanation.setText("The inference engine found another way to prove this goal. This may need to be investigated further in the next steps.");
		}
		else if (a == Action.ADD_RULE_TO_EXECUTION_TREE) {
			title.setText("It looks like rule "+r.getName()+" could be useful in proving the goal "+LabelUtil.toString(goal.getGoal()));
			explanation.setText("For now this information is only saved for later. The inference engine will later investigate wheter it actually can provide a result.");			
		}
		else if (a == Action.STOPPED) {
			title.setText("Stopped");
			explanation.setText("Inference engine stopped - press Start to start again or Select to choose a new rule/query.");
		}
		else {
			title.setText("mmmh?");
			explanation.setText(a.toString());
		}
	}

	public void waking() {
		title.setText("Running ...");
		explanation.setText("Currently there is nothing to explain!");
	}

	public void labelProviderChanged(LabelProviderChangedEvent event) {
		suspending(lastAction,lastGoal,lastRule);
	}

}
