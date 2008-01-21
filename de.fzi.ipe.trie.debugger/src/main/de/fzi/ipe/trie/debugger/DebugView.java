package de.fzi.ipe.trie.debugger;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.hp.hpl.jena.shared.JenaException;

import de.fzi.ipe.trie.debugger.gui.DebugLabelProvider;
import de.fzi.ipe.trie.debugger.gui.ResultLineProvider;
import de.fzi.ipe.trie.debugger.gui.RuleDebugContentProvider;
import de.fzi.ipe.trie.debugger.gui.StyledTextView;
import de.fzi.ipe.trie.debugger.gui.TextPart;
import de.fzi.ipe.trie.debugger.gui.actions.BackAction;
import de.fzi.ipe.trie.debugger.gui.actions.ForwardAction;
import de.fzi.ipe.trie.debugger.gui.actions.SelectRuleAction;
import de.fzi.ipe.trie.debugger.gui.actions.SelectRuleDropDownAction;
import de.fzi.ipe.trie.debugger.gui.bindings.BindingsGroup;
import de.fzi.ipe.trie.debugger.gui.dependsOn.DependsOnGroup;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.prooftree.ProoftreeGroup;
import de.fzi.ipe.trie.debugger.gui.prooftree.ProoftreeTreeViewer;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;

public class DebugView extends ViewPart {

	protected static final Font FONT_HEADLINE = new Font(null, "Tahoma", 14, SWT.BOLD);
	protected static final Font FONT_CLAUSES = new Font(null, "Tahoma", 10, SWT.BOLD);
	protected static final Font FONT_SYMBOLS = new Font(null, "Tahoma", 10, SWT.BOLD);
	protected static final Font FONT_BUILTIN_HEADLINE = FONT_CLAUSES;
	protected static final Font FONT_BUILTIN = new Font(null, "Tahoma", 10, SWT.NONE);
	protected static final Font FONT_BUILTIN_SYNTAX = new Font(null, "Courier New", 10, SWT.NONE);
	protected static final Font FONT_CONTEXT_HEADLINE = new Font(null, "Tahoma", 10, SWT.NONE);
	protected static final Font Font_CONTEXT_NAME = new Font(null, "Tahoma", 10, SWT.NONE);

	protected static final Color COLOR_CLAUSE_NOT_SATISFIED = new Color(Display.getDefault(), 255, 0, 0);
	protected static final Color COLOR_CLAUSE_NO_BINDINGS = new Color(Display.getDefault(), 185, 30, 66);
	protected static final Color COLOR_BLACK = new Color(Display.getDefault(), 0, 0, 0);
	protected static final Color COLOR_DISABLED = new Color(Display.getDefault(), 120, 120, 120);
	protected static final Color COLOR_LABEL_SELECTED = new Color(Display.getDefault(),0 , 0, 160);
	protected static final Color COLOR_LABEL_SELECTED_FOREGROUND = new Color(Display.getDefault(), 255, 255, 255);
	protected static final Color COLOR_LABEL_SELECTABLE = new Color(Display.getDefault(), 255, 255, 255);
	protected static final Color COLOR_BUILTIN = new Color(Display.getDefault(), 0, 128, 255);

	public static final String VIEW_ID = "de.fzi.ipe.trie.debugger.InferenceExplorer";
	
	
	private RuleDebugContentProvider contentProvider;

	private static DebugView singleton;
	
	private DebuggerEventBus eventBus = new DebuggerEventBus(); 
	

	private Composite mainComposite, parent;
	private ScrolledComposite scrolledComposite;
	private Label headline;
	private TableViewer bindingsViewer, dependsOnViewer;
	private ProoftreeTreeViewer pView;
	private ResultLineProvider lastResult; //stores that last result that was selected, used to decide if the 
	// state of the prooftreeview should be restored

	public static DebugLabelProvider labelProvider = new DebugLabelProvider();
	
	private Action[] ruleActions;
	private Action refresh;
	private Action showNamespaces, showModules;
	private ForwardAction forward;
	private BackAction back;
	private boolean showNamespaces_b = false, showModules_b = false, dynamic_b = true, showProoftree_b = true;

	/**
	 * The constructor.
	 */
	public DebugView() {
		contentProvider = new RuleDebugContentProvider(eventBus);
		contentProvider.setView(this);
		singleton = this;
	}

	public static DebugView getInstance() { 
		if (singleton == null) singleton = new DebugView(); 
		return singleton; 
	}
	
	public static boolean doDynamic() { return singleton.dynamic_b; }
	public static boolean showProoftree() { return singleton.showProoftree_b; }

	public boolean showNamespaces() { return showNamespaces_b; }
	public boolean showModules() { return showModules_b; }
	
	
	public Shell getShell() {
		if (mainComposite != null) return mainComposite.getShell();
		else return null;
	}

	
	public void init(IViewSite site, IMemento rootMemento) throws PartInitException {
		super.init(site);

		if (rootMemento != null) {
			IMemento memento = rootMemento.getChild("labelCreators");
			if (memento != null) {
				String ns = memento.getString("showNamespaces");
				if ((ns != null) && (ns.equals("true"))) showNamespaces_b = true;
				String prooftree = memento.getString("showProoftrees");
				if ((prooftree != null) && (prooftree.equals("true"))) showProoftree_b = true;
				String module = memento.getString("showModules");
				if ((module != null) && ((module.equals("true")))) showModules_b = true;
				String dyn = memento.getString("dynamic");
				if ((dyn != null) && (dyn.equals("true"))) dynamic_b = true;
				else if (dyn != null) dynamic_b = false;
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.ViewPart#saveState(org.eclipse.ui.IMemento)
	 */
	public void saveState(IMemento rootMemento) {
		super.saveState(rootMemento);
		IMemento memento = rootMemento.createChild("labelCreators");
		memento.putString("showNamespaces", "" + showNamespaces());
		memento.putString("showProoftrees", ""+showProoftree_b );
		String prooftree = memento.getString("showProoftrees");
		if ((prooftree != null) && (prooftree.equals("true"))) showProoftree_b = true;

		memento.putString("showModules", "" + showModules());
		memento.putString("dynamic", "" + dynamic_b);
	}

	public static void handleException(Exception e, String msg) {
		MessageDialog.openError(singleton.getShell(), "Rule Debugger Error", msg);
		singleton.contentProvider.selectRule(null);
	}

	public static void handleException(Exception e) {
		handleException(e, "An error occured in the rule debug view: \n " + e.getMessage() + "\n\n You can continue to work, just select a new rule.");
	}

	public void createPartControl(Composite parent) {
		this.parent = parent;
		createActions();
		createMenu();
		createToolbar();
		parent.setLayout(new FillLayout());

		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		mainComposite = new Composite(scrolledComposite, SWT.NONE);
		scrolledComposite.setContent(mainComposite);

		createPartControl();
	}

	private void createPartControl() {
		GridLayout mainLayout = new GridLayout();
		mainLayout.verticalSpacing = 15;
		mainComposite.setLayout(mainLayout);

		createHeadline(mainComposite);

		if (contentProvider.getCurrentRule() != null) {
			Composite clauses = createRuleDetails();
			
			Composite data = new Composite(mainComposite, SWT.NONE);
			GridData dataGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
			dataGD.heightHint = 200;
			data.setLayoutData(dataGD);
			data.setLayout(new FillLayout(SWT.HORIZONTAL));

			new BindingsGroup(data,dynamic_b,contentProvider,eventBus);

			Point point = clauses.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			scrolledComposite.setMinSize(Math.max(point.x, 500), point.y + 600);

			Composite data2 = new Composite(mainComposite,SWT.NONE);
			GridData data2GD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
			data2GD.heightHint = 200;
			data2.setLayoutData(data2GD);
			FillLayout data2Layout = new FillLayout(SWT.HORIZONTAL);
			data2Layout.spacing = 4;
			data2.setLayout(data2Layout);
			new DependsOnGroup(data2,eventBus,contentProvider);
			new ProoftreeGroup(data2,showProoftree_b,eventBus, contentProvider);
		}
	}
	
	private Composite createRuleDetails() {
		Group clauses = new Group(mainComposite, SWT.NONE);
		clauses.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		clauses.setText("Rule Details");
		clauses.setLayout(new GridLayout());

		StyledTextView styledText = new StyledTextView();

		makeHeadClauses(styledText);
		styledText.addNewLine();
		TextPart ifPart = new TextPart("IF ");
		styledText.add(ifPart);
		styledText.addNewLine();

		makeBodyClauses(styledText);
		styledText.createStyledText(clauses);
		return clauses;

	}

	private void createHeadline(Composite mainComposite) {
		Composite headlineComposite = new Composite(mainComposite, SWT.NONE);
		headlineComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		headlineComposite.setLayout(new GridLayout(2, true));

		headline = new Label(headlineComposite, SWT.NONE);
		headline.setFont(FONT_HEADLINE);
		if (contentProvider.getCurrentRule() != null) {
			headline.setText(contentProvider.getCurrentRule().getName());
		}
		else {
			headline.setText("No Rule Selected");
		}

	}


	private void makeHeadClauses(StyledTextView parent) {
		DebuggerRule currentRule = contentProvider.getCurrentRule();
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

	private void makeBodyClauses(StyledTextView parent){
		DebuggerRule currentRule = contentProvider.getCurrentRule();
		if (currentRule != null) {
			DebuggerAtom[] bodyPredicates = currentRule.getBodyClauses();
			for (int i = 0; i < bodyPredicates.length; i++) {
				final DebuggerAtom currentClause = bodyPredicates[i];
				TextPart currentTextPart = new TextPart(currentClause.toString());
				currentTextPart.addSelectionListener(new ClauseSelectionListener(currentClause));
				parent.add(currentTextPart);

				if ((contentProvider.getCurrentClause() != null) && (contentProvider.getCurrentClause().equals(currentClause))) {
					currentTextPart.setBackground(COLOR_LABEL_SELECTED);
					currentTextPart.setForeground(COLOR_LABEL_SELECTED_FOREGROUND);
				} else {
					currentTextPart.setBackground(COLOR_LABEL_SELECTABLE);
					if ((doDynamic()) && (currentRule.hasCalculatedBindingForAllLiterals())) {
						colorBodyClause(currentClause, currentTextPart); 
					}
				}
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

	public void createMenu() {
		IMenuManager mgr = getViewSite().getActionBars().getMenuManager();
		mgr.add(refresh);
		mgr.add(new Separator());

		showNamespaces = new Action("Hide Uri before #", Action.AS_CHECK_BOX) {
			public void run() {
				if (showNamespaces.isChecked()) labelProvider.hideBeforeHash = true;
				else labelProvider.hideBeforeHash = false;
				refresh();
			}
		};
		if (labelProvider.hideBeforeHash) showNamespaces.setChecked(true);
		mgr.add(showNamespaces);
		showModules = new Action("Show Modules", Action.AS_CHECK_BOX) {
			public void run() {
				if (showModules.isChecked()) showModules_b = true;
				else showModules_b = false;
				refresh();
			}
		};
		if (showModules_b) showModules.setChecked(true);
//		mgr.add(showModules);
		
	}

	public void createToolbar() {
		IToolBarManager mgr = getViewSite().getActionBars().getToolBarManager();

		back = new BackAction(contentProvider);
		back.setToolTipText("Back to last rule");
		mgr.add(back);
		forward = new ForwardAction(contentProvider);
		forward.setToolTipText("Forward");
		mgr.add(forward);
		mgr.add(new Separator());
		mgr.add(new SelectRuleDropDownAction(contentProvider, this,eventBus));
		mgr.add(new Separator());
		mgr.add(refresh);
	}

	public void createActions() {
		createRuleActions();
		refresh = new Action("Refresh") {
			public void run() {
				try {
					contentProvider.reload();
				} catch (JenaException je) {
					handleException(je, "Could not reload files");
				} catch (IOException e) {
					handleException(e, "Could not reload files - "+ e.getMessage());
				}
			}
		};
		refresh.setImageDescriptor(DebuggerPlugin.loadImage(DebuggerPlugin.IMAGE_REFRESH));
		refresh.setToolTipText("Reload Data and Refresh View");
	}

	private void createRuleActions() {
		DebuggerRule[] rules = contentProvider.getAllRules();
		ruleActions = new Action[rules.length];
		for (int i = 0; i < rules.length; i++) {
			ruleActions[i] = new SelectRuleAction(rules[i],eventBus);
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		//		viewer.getControl().setFocus();
	}

	/**
	 * Refresh what is shown.
	 */
	public void refresh() {
		Object[] proofTreeCache = null; 
		if (mainComposite != null) {
			if (Display.findDisplay(Thread.currentThread()) == null) { //not a UI thread?
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						refresh();
					}
				}); //call method from ui thread
			}
			if (pView != null) proofTreeCache = pView.getExpandedElements();
			pView = null;

			scrolledComposite.dispose();

			scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			mainComposite = new Composite(scrolledComposite, SWT.NONE);
			scrolledComposite.setContent(mainComposite);

			if (forward != null) forward.refresh();
			if (back != null) back.refresh();
			createPartControl();
			if ((pView != null) && (proofTreeCache != null) && (lastResult != null) && (lastResult == contentProvider.getCurrentResult())) {
				pView.setExpandedElements(proofTreeCache);
			}
			parent.layout(true);
		}
	}

	private class ClauseSelectionListener implements SelectionListener {

		DebuggerAtom currentClause;
		boolean selected = false;

		public ClauseSelectionListener(DebuggerAtom currentClause) {
			this.currentClause = currentClause;
			if ((contentProvider.getCurrentClause() != null) && (contentProvider.getCurrentClause().equals(currentClause))) {
				selected = true;
			}
		}

		public void widgetSelected(SelectionEvent e) {
			if (selected) {
				selected = false;
				contentProvider.deselectClause();
			} else {
				contentProvider.selectClause(currentClause);
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