package de.fzi.ipe.trie.debugger.gui.prooftree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeAssumptionNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeFactNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeQueryNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeRuleNode;

public class ProoftreeTreeViewer extends TreeViewer{
	
	static class ProoftreeNodeComparator implements Comparator<ProoftreeNode>, Serializable {

		private static final long serialVersionUID = 1L;

		public int compare(ProoftreeNode o1, ProoftreeNode o2) {
			String name1 = makeComparableString(o1);
			String name2 = makeComparableString(o2);
			return name1.compareTo(name2);
		}

		private String makeComparableString(ProoftreeNode o1) {
			String name1;
			if (o1 instanceof ProoftreeRuleNode) name1 = "a"+o1.toString();
			else if (o1 instanceof ProoftreeAssumptionNode) name1 = "b"+o1.toString();
			else name1 = "c"+o1.toString();
			return name1;
		}
		
	}
	
	
	static class MyTreeContentProvider implements ITreeContentProvider {

		private final String[] placeholder = {"Please select a result to show the prooftree for it"};
		private static final Comparator<ProoftreeNode> COMPARATOR = new ProoftreeNodeComparator();
		
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof TreeViewRoot) {
				parentElement = ((TreeViewRoot)parentElement).getRootNode();
				if (parentElement == null) return placeholder;
			}
			if(parentElement instanceof ProoftreeNode) {
				if (parentElement instanceof ProoftreeRuleNode || parentElement instanceof ProoftreeQueryNode) {
					Set<ProoftreeNode> toReturn = new HashSet<ProoftreeNode>();
					for (ProoftreeNode node:((ProoftreeNode)parentElement).getChildren()) {
						toReturn.addAll(node.getChildren());
					}
					ProoftreeNode[] toReturnArray = toReturn.toArray(new ProoftreeNode[0]);
					Arrays.sort(toReturnArray,COMPARATOR);
					return toReturnArray;
				}
				else {
					ProoftreeNode[] toReturnArray = ((ProoftreeNode)parentElement).getChildren().toArray(new ProoftreeNode[0]);
					Arrays.sort(toReturnArray,COMPARATOR);
					return toReturnArray;
				}
			}
			else return new Object[0];
		}

		public Object getParent(Object element) {
			return null;
		}

		public boolean hasChildren(Object parentElement) {
			return getChildren(parentElement).length > 0;
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
			;
		}
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}

		
		
	}
	static class MyTreeLabelProvider implements ILabelProvider {

	    public MyTreeLabelProvider() {
	    }
	    
		public Image getImage(Object element) {
			if(element instanceof ProoftreeNode){
			    ProoftreeNode node = (ProoftreeNode) element;
			    if (node instanceof ProoftreeFactNode) {
			        return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_FACT_GREEN);
			    }
			    else if (node instanceof ProoftreeAssumptionNode) {
			    	return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_FACT_RED);
			    }
			    else if (node instanceof ProoftreeRuleNode) {
			    	ProoftreeRuleNode ruleNode = (ProoftreeRuleNode) node;
			    	if (ruleNode.hasAlmostMatch()) return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_RED);
			    	else return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_GREEN);
			    }
			    else return null;
			}
			else return null;
		}

		public String getText(Object element) {
			if(element instanceof ProoftreeFactNode) {
				ProoftreeFactNode node = (ProoftreeFactNode) element;
				return DebugView.LABEL_PROVIDER.getLabel(node.getFacts());
			}
			else if (element instanceof ProoftreeAssumptionNode) {
				ProoftreeAssumptionNode node = (ProoftreeAssumptionNode) element;
				return "Assumption: "+ DebugView.LABEL_PROVIDER.getLabel(node.getGoal());
			}
			else if(element instanceof ProoftreeNode) {
			    ProoftreeNode node = (ProoftreeNode) element;
			    return node.getName();
			}
			else if (element instanceof String) {
				return (String) element;
			}
			else return "?";
		}
		

		public void addListener(ILabelProviderListener listener) {
			;
		}

		public void dispose() {
			;
		}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		
		public void removeListener(ILabelProviderListener listener) {
			;
		}
	}
	
	private class ToolTipCreator extends ShellAdapter implements MouseMoveListener, MouseTrackListener{
		private Shell tip;
		private Label label;
		
		public ToolTipCreator() {
        	addShellListener();
		}
		
		public void mouseHover(MouseEvent e) {
			TreeItem item = getTree().getItem(new Point(e.x,e.y));
	        if (item != null) {
	        	disposeTip();
	        	String tooltipText = getTooltipText(item);
	        	if (tooltipText != null) {
	        		createTooltip(tooltipText);
	        		Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
	        		Point pt = getTree().toDisplay(e.x, e.y);
	        		tip.setBounds(pt.x+15, pt.y+20, size.x, size.y);
	        		tip.setVisible(true);
	        	}
	        }
		}

		private void createTooltip(String tooltipText) {
			tip = new Shell(getControl().getShell(), SWT.ON_TOP | SWT.TOOL);
			tip.setLayout(new FillLayout());
			label = new Label(tip, SWT.NONE);
			label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
			label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
			label.setText(tooltipText);
		}

		private void addShellListener() {
			Shell activeShell = Display.getCurrent().getActiveShell();
			if (activeShell != null) activeShell.addShellListener(this);
		}
		
		private String getTooltipText(TreeItem item) {
			if (item.getData() instanceof ProoftreeNode) {
				ProoftreeNode node = (ProoftreeNode) item.getData();
				return node.getTooltip();
			}
			else return null;
		}

		private void disposeTip() {
			if (tip != null && !tip.isDisposed()) tip.dispose();
		}
		
		public void mouseMove(MouseEvent e) {
			disposeTip();
		}

		public void shellClosed(ShellEvent e) {
			disposeTip();
		}

		public void shellDeactivated(ShellEvent e) {
			disposeTip();
		}

		public void shellIconified(ShellEvent e) {
			disposeTip();
		}

		public void mouseEnter(MouseEvent e) {
			;
		}

		public void mouseExit(MouseEvent e) {
			disposeTip();
		}

	}

	
	public ProoftreeTreeViewer (Composite c) {
	    super(c);
	    setContentProvider(new MyTreeContentProvider());
	    setLabelProvider(new MyTreeLabelProvider());
	    ToolTipCreator ttc = new ToolTipCreator();
	    getTree().addMouseTrackListener(ttc);
	    getTree().addMouseMoveListener(ttc);
	}

	public void displayProoftree(Prooftree prooftree) {
        TreeViewRoot root = new TreeViewRoot(prooftree);
        setInput(root);
        expandToLevel(3);
        showAssumptions(prooftree);
        refresh();	    
	}
	
	
	private void showAssumptions(Prooftree prooftree) {
		if (prooftree != null && prooftree.getGrounding()<1d) {
			expandAll();
			List<ProoftreeNode> toShow = new ArrayList<ProoftreeNode>(50);
			for (ProoftreeNode node: prooftree) {
				if (node instanceof ProoftreeAssumptionNode) toShow.add(node);
			}
			for (int i=toShow.size()-1;i>=0;i--) {
				Item item = (Item) doFindItem(toShow.get(i));
				if (item != null) showItem(item); 
			}
		}
	}

	
}







