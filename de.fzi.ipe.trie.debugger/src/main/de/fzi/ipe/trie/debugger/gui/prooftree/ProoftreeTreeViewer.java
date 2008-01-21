package de.fzi.ipe.trie.debugger.gui.prooftree;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;


import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.DebuggerPlugin;
import de.fzi.ipe.trie.inference.prooftree.Prooftree;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeFactNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeQueryNode;
import de.fzi.ipe.trie.inference.prooftree.ProoftreeRuleNode;

public class ProoftreeTreeViewer extends TreeViewer{
	
	class MyTreeContentProvider implements ITreeContentProvider {

		private final String[] placeholder = {"Please select a result to show the prooftree for it"};
		
		
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
					return toReturn.toArray();
				}
				else {
					return ((ProoftreeNode)parentElement).getChildren().toArray();
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
	class MyTreeLabelProvider implements ILabelProvider {

	    public MyTreeLabelProvider() {
	    }
	    
		public Image getImage(Object element) {
			if(element instanceof ProoftreeNode){
			    ProoftreeNode node = (ProoftreeNode) element;
			    if (node instanceof ProoftreeFactNode) {
			        return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_FACT_GREEN);
			    }
			    else return DebuggerPlugin.getImage(DebuggerPlugin.IMAGE_RULE_GREEN);
			}
			else return null;
		}

		public String getText(Object element) {
			if(element instanceof ProoftreeFactNode) {
				ProoftreeFactNode node = (ProoftreeFactNode) element;
				return DebugView.labelProvider.getLabel(node.getFacts());
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

	
	public ProoftreeTreeViewer (Composite c) {
	    super(c);
	    setContentProvider(new MyTreeContentProvider());
	    setLabelProvider(new MyTreeLabelProvider());		
	}

	public void displayProoftree(Prooftree prooftree) {
        TreeViewRoot root = new TreeViewRoot(prooftree);
        setInput(root);
        refresh();	    
	}

	
}
