package de.fzi.trie.visualization;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import de.fzi.trie.visualization.model.RuleGraph;
import de.fzi.trie.visualization.model.RuleGraph.RuleNode;

public class RuleGraphContentProvider implements IGraphEntityContentProvider{

	private RuleNode[] elements = new RuleNode[0];
	
	public Object[] getConnectedTo(Object entity) {
		Set<RuleNode> connectedTo = new HashSet<RuleNode>();
		RuleNode current = (RuleNode) entity;
		connectedTo.addAll(current.getDependsOn());
		return connectedTo.toArray(new RuleNode[0]);
	}

	public Object[] getElements(Object inputElement) {
		return elements;
	}

	public void dispose() {
		;
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput instanceof RuleGraph) {
			RuleGraph ruleGraph = (RuleGraph) newInput;
			elements = ruleGraph.getElements();
		}
		else {
			elements = new RuleNode[0];
		}
	}

}
