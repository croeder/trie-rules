package de.fzi.trie.visualization;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import de.fzi.trie.visualization.model.RuleGraph.RuleNode;

public class RuleGraphLabelProvider extends LabelProvider implements IEntityStyleProvider, IConnectionStyleProvider{ 

	
	@Override
	public String getText(Object element) {
		if (element instanceof RuleNode) {
			RuleNode currentNode = (RuleNode) element;
			return currentNode.getRule().getName();			
		}
		else if (element instanceof EntityConnectionData) {
			return "";
		}
		else return "huh? "+ element.getClass();
	}

	public boolean fisheyeNode(Object entity) {
		return false;
	}

	public Color getBackgroundColour(Object entity) {
		return null;
	}

	public Color getBorderColor(Object entity) {
		return null;
	}

	public Color getBorderHighlightColor(Object entity) {
		return null;
	}

	public int getBorderWidth(Object entity) {
		return -1;
	}

	public Color getForegroundColour(Object entity) {
		return null;
	}

	public Color getNodeHighlightColor(Object entity) {
		return null;
	}

	public IFigure getTooltip(Object entity) {
		return null;
	}

	public Color getColor(Object rel) {
		return null;
	}

	public int getConnectionStyle(Object rel) {
		return ZestStyles.CONNECTIONS_DIRECTED;
	}

	public Color getHighlightColor(Object rel) {
		return null;
	}

	public int getLineWidth(Object rel) {
		return -1;
	}


}
