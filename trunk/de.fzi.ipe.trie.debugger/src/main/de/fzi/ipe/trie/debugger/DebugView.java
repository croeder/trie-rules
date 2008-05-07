package de.fzi.ipe.trie.debugger;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

import com.hp.hpl.jena.shared.JenaException;

import de.fzi.ipe.trie.debugger.gui.DebugLabelProvider;
import de.fzi.ipe.trie.debugger.gui.HeadlineComposite;
import de.fzi.ipe.trie.debugger.gui.actions.BackAction;
import de.fzi.ipe.trie.debugger.gui.actions.ForwardAction;
import de.fzi.ipe.trie.debugger.gui.actions.GoToQueryAction;
import de.fzi.ipe.trie.debugger.gui.actions.SelectRuleDropDownAction;
import de.fzi.ipe.trie.debugger.gui.bindings.BindingsGroup;
import de.fzi.ipe.trie.debugger.gui.dependsOn.DependsOnGroup;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.EvaluationEventLogger;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.gui.events.RefreshEvent;
import de.fzi.ipe.trie.debugger.gui.events.SelectedRuleEvent;
import de.fzi.ipe.trie.debugger.gui.prooftree.ProoftreeGroup;
import de.fzi.ipe.trie.debugger.gui.ruleDetails.RuleDetailsGroup;
import de.fzi.ipe.trie.debugger.model.DebuggerRule;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;

public class DebugView extends ViewPart implements DebuggerEventBusListener, Observer {


	protected static final Font FONT_CLAUSES = new Font(null, "Tahoma", 10, SWT.BOLD);
	protected static final Font FONT_SYMBOLS = new Font(null, "Tahoma", 10, SWT.BOLD);
	protected static final Font FONT_BUILTIN_HEADLINE = FONT_CLAUSES;
	protected static final Font FONT_BUILTIN = new Font(null, "Tahoma", 10, SWT.NONE);
	protected static final Font FONT_BUILTIN_SYNTAX = new Font(null, "Courier New", 10, SWT.NONE);
	protected static final Font FONT_CONTEXT_HEADLINE = new Font(null, "Tahoma", 10, SWT.NONE);
	protected static final Font Font_CONTEXT_NAME = new Font(null, "Tahoma", 10, SWT.NONE);


	protected static final Color COLOR_BLACK = new Color(Display.getDefault(), 0, 0, 0);
	protected static final Color COLOR_DISABLED = new Color(Display.getDefault(), 120, 120, 120);

	public static final String VIEW_ID = "de.fzi.ipe.trie.debugger.InferenceExplorer";
	
	

	private static DebugView singleton;
	
	private DebuggerEventBus eventBus = new DebuggerEventBus(); 
	private DebuggerRuleStore debuggerRuleStore;
	
	private Composite mainComposite, parent;
	private ScrolledComposite scrolledComposite;

	public static DebugLabelProvider labelProvider = new DebugLabelProvider();
	
	private Action refresh;
	private Action showNamespaces, showModules;
	private ForwardAction forward;
	private BackAction back;
	private boolean showNamespaces_b = false, showModules_b = false, dynamic_b = true, showProoftree_b = true;
	private boolean inNeedOfRefresh = false;

	/**
	 * The constructor.
	 */
	public DebugView() {
		singleton = this;
		eventBus.addListener(this);
		new EvaluationEventLogger(eventBus);
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
		SelectedRuleEvent event = new SelectedRuleEvent(null,SelectedRuleEvent.Source.INTERNAL);
		singleton.eventBus.sendEvent(event);
	}

	public static void handleException(Exception e) {
		handleException(e, "An error occured in the rule debug view: \n " + e.getMessage() + "\n\n You can continue to work, just select a new rule.");
	}

	public void createPartControl(Composite parent) {
		this.parent = parent;
		debuggerRuleStore = new DebuggerRuleStore(DatamodelAccess.getDatamodel());
		debuggerRuleStore.addObserver(this);
		
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
		
		if (debuggerRuleStore.getRule("Query") != null) {
			DebuggerRule query = debuggerRuleStore.getRule("Query");
			eventBus.sendEvent(new SelectedRuleEvent(query,SelectedRuleEvent.Source.INTERNAL));
		}
	}

	private void createPartControl() {
		GridLayout mainLayout = new GridLayout();
		mainLayout.verticalSpacing = 15;
		mainComposite.setLayout(mainLayout);

		new HeadlineComposite(mainComposite,eventBus);

		RuleDetailsGroup ruleDetails = new RuleDetailsGroup(mainComposite,debuggerRuleStore,eventBus);
		Point point = ruleDetails.getSize();
		scrolledComposite.setMinSize(Math.max(point.x, 500), point.y + 600);
		
		Composite data = new Composite(mainComposite, SWT.NONE);
		GridData dataGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		dataGD.heightHint = 200;
		data.setLayoutData(dataGD);
		data.setLayout(new FillLayout(SWT.HORIZONTAL));

		new BindingsGroup(data,dynamic_b,debuggerRuleStore,eventBus);

		Composite data2 = new Composite(mainComposite,SWT.NONE);
		GridData data2GD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		data2GD.heightHint = 200;
		data2.setLayoutData(data2GD);
		FillLayout data2Layout = new FillLayout(SWT.HORIZONTAL);
		data2Layout.spacing = 4;
		data2.setLayout(data2Layout);
		new DependsOnGroup(data2,debuggerRuleStore, eventBus);
		new ProoftreeGroup(data2,showProoftree_b,debuggerRuleStore, eventBus);
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

		back = new BackAction(debuggerRuleStore, eventBus);
		back.setToolTipText("Back to last rule");
		mgr.add(back);
		forward = new ForwardAction(debuggerRuleStore,eventBus);
		forward.setToolTipText("Forward");
		mgr.add(forward);
		mgr.add(new Separator());
		mgr.add(new GoToQueryAction(debuggerRuleStore,eventBus));
		mgr.add(new SelectRuleDropDownAction(this,debuggerRuleStore, eventBus));
		mgr.add(new Separator());
		mgr.add(refresh);
	}

	public void createActions() {
		refresh = new Action("Refresh") {
			public void run() {
				try {
			    	debuggerRuleStore.reload();
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


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		mainComposite.setFocus();
		if (inNeedOfRefresh) {
			eventBus.sendEvent(new RefreshEvent());
			inNeedOfRefresh = false;
		}
	}

	/**
	 * Refresh what is shown.
	 */
	public void refresh() {
		if (mainComposite != null) {
			if (Display.findDisplay(Thread.currentThread()) == null) { //not a UI thread?
				Display.getCurrent().asyncExec(new Runnable() {
					public void run() {
						refresh();
					}
				}); //call method from ui thread
			}


			scrolledComposite.dispose();

			scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			mainComposite = new Composite(scrolledComposite, SWT.NONE);
			scrolledComposite.setContent(mainComposite);

			if (forward != null) forward.refresh();
			if (back != null) back.refresh();
			createPartControl();

			parent.layout(true);
		}
	}

	public void eventNotification(DebuggerEvent event) {
		if (event instanceof SelectedRuleEvent) {
			eventBus.sendEvent(RedrawEvent.DEBUG_VIEW);
		}
		else if (event == RedrawEvent.DEBUG_VIEW) {
			mainComposite.pack(true);
			parent.layout(true);
			parent.redraw();
		}
	}

	public void update(Observable arg0, Object arg1) {
		if (mainComposite.isVisible()) {
			eventBus.sendEvent(new RefreshEvent());
			inNeedOfRefresh  = false;
		}
		else inNeedOfRefresh = true;
	}


}