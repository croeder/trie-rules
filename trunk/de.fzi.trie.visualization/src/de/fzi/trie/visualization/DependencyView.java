package de.fzi.trie.visualization;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;


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

public class DependencyView extends ViewPart {
	
	public static String VIEW_ID = "de.fzi.trie.visualization.dependency.DependencyView";
	
	private Composite root;

	public DependencyView() {
	}

	public void createPartControl(Composite parent) {
		root = new Composite(parent, SWT.DEFAULT);
		root.setLayout(new FillLayout()); 
		
		Graph g = new Graph(root, SWT.NONE);

		GraphNode n = new GraphNode(g, SWT.NONE, "Paper");
		GraphNode n2 = new GraphNode(g, SWT.NONE, "Rock");
		GraphNode n3 = new GraphNode(g, SWT.NONE, "Scissors");
		new GraphConnection(g, SWT.NONE, n, n2);
		new GraphConnection(g, SWT.NONE, n2, n3);
		new GraphConnection(g, SWT.NONE, n3, n);
		g.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
		
		Label testLabel = new Label(root,SWT.None);
		testLabel.setText("So weit so gut");
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		;
	}
}