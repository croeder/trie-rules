/**
 * 
 */
package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import de.fzi.ipe.trie.Rule;

public class RuleNameLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if (element instanceof Rule) {
			Rule rule = (Rule) element;
			return rule.getName();
		}
		else return "No Rule Object - Internatl Error";
	}

	public void addListener(ILabelProviderListener listener) {
		; //never changes state - hence no need for listener;
	}

	public void dispose() {
		;
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		; //never changes state - hence no need for listener;
	}
	
}