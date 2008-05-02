package de.fzi.ipe.trie.proceduraldebugger.gui.contentProvider;

import java.util.HashSet;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.simple.SimpleBackwardChainer;

public class ExecutionTreeContentProvider implements ITreeContentProvider{

	private ExecutionTreeQuery query;
	
	public Object[] getChildren(Object element) {
		if (element instanceof ExecutionTreeGoal) {
			ExecutionTreeElement treeElement = (ExecutionTreeElement) element;
			HashSet<Object> children = new HashSet<Object>();
			for (ExecutionTreeElement current:treeElement.getChildren()) {
				if (current instanceof ExecutionTreeFacts) {
					children.addAll( ((ExecutionTreeFacts) current).getFacts());
				}
				else children.add(current);
			}
			return children.toArray();
		}
		else if (element instanceof ExecutionTreeElement){
			ExecutionTreeElement treeElement = (ExecutionTreeElement) element;
			return treeElement.getChildren().toArray();
		}
		else return null;
	}

	public Object getParent(Object element) {
		if (element instanceof ExecutionTreeElement){
			ExecutionTreeElement treeElement = (ExecutionTreeElement) element;
			return treeElement.getParent();
		}
		else return null;
	}

	public boolean hasChildren(Object element) {
		if (element instanceof ExecutionTreeElement){
			ExecutionTreeElement treeElement = (ExecutionTreeElement) element;
			return (treeElement.getChildren().size()>0);
		}
		else return false;
	}

	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof SimpleBackwardChainer) {
			return new Object[]{query};
		}
		else return new Object[0];
	}

	public void dispose() {
		;		
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) query = ((SimpleBackwardChainer) newInput).getExecutionTree();
		else query = null;
	}

	
	
}
