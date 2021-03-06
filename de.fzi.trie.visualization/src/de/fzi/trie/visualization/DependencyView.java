package de.fzi.trie.visualization;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;

import de.fzi.ipe.trie.inference.KnowledgeBaseListener;
import de.fzi.trie.visualization.model.RuleGraph;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class DependencyView extends ViewPart implements KnowledgeBaseListener {
	
	public static String VIEW_ID = "de.fzi.trie.visualization.dependency.DependencyView";
	
	private Composite root;
	private GraphViewer g;

	private boolean changed = false;
	
	public DependencyView() {
	}

	public void createPartControl(Composite parent) {
		DatamodelAccess.getDatamodel().getKnowledgeBase().addListener(this);
		
		root = new Composite(parent, SWT.DEFAULT);
		root.setLayout(new FillLayout()); 
		
		g = new GraphViewer(root,SWT.NONE);
		g.setLabelProvider(new RuleGraphLabelProvider());
		g.setContentProvider(new RuleGraphContentProvider());
		g.setInput(new RuleGraph(DatamodelAccess.getDatamodel().getKnowledgeBase().getRuleBase()));
		
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NONE), true);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		DebugLogger.log("OpeningVisualization");
		if (changed) {
			g.setInput(new RuleGraph(DatamodelAccess.getDatamodel().getKnowledgeBase().getRuleBase()));
			changed = false;
		}
	}

	public void knowledgeBaseChanged() {
		changed = true;
	}
}