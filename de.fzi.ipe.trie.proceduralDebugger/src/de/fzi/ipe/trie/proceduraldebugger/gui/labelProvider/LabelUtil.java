package de.fzi.ipe.trie.proceduraldebugger.gui.labelProvider;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.URITerm;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;

/**
 * Collection of utilities for the creation of labels. Also offers the 
 * property to hide the URI before the hash. Node that LabelProvider that 
 * utilize the methods of this class have to register their LabelProviderListener 
 * this class, so that it can be notified when the hideBeforeHash property changes. 
 * @author zach
 *
 */
public class LabelUtil {

	private static boolean hideBeforeHash = true;
	private static Set<Tuple> listeners = new HashSet<Tuple>();
	
	private static NullLabelProvider NULL_LABEL_PROVIDER = new NullLabelProvider();
	
	public static String toString(ExecutionTreeQuery query) {
		StringBuilder builder = new StringBuilder();
		builder.append("Query:\n");
		builder.append("<-\n");
		appendAtomArray(query.getBody(),builder);
		return builder.toString();
	}
	
	public static String toString(ExecutionTreeRule eRule) {
		Rule rule = eRule.getRule();
		return toString(rule);
	}
	
	public static String toString(Rule rule) {
		StringBuilder builder = new StringBuilder();
		builder.append("["+rule.getName()+"\n");
		appendAtomArray(rule.getHead(),builder);
		builder.append("<-\n");
		appendAtomArray(rule.getBody(),builder);
		builder.append("]");
		return builder.toString();
	}
	
	private static void appendAtomArray(Atom[] atoms, StringBuilder builder) {
		for (Atom a: atoms) {
			builder.append("\t");
			builder.append(toString(a));
			builder.append("\n");
		}
	}
	
	public static String toString(Atom atom) {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(toString(atom.getSubject())); 
		builder.append(",");
		builder.append(toString(atom.getPredicate())); 
		builder.append(",");
		builder.append(toString(atom.getObject())); 
		builder.append(")");
		return builder.toString();
	}
	
	
	public static String toString(Term term) {
		if (hideBeforeHash && term instanceof URITerm ) {
			String temp = term.toString();
			int hashIndex = temp.indexOf('#');
			if (hashIndex != -1) {
				return temp.substring(hashIndex+1);
			}
			else return temp;
		}
		else {
			return term.toString();
		}
	}
	
	public static void setHideBeforeHash(boolean hide) {
		if (hideBeforeHash != hide) {
			hideBeforeHash = hide;
			notifyListener();
		}
	}
	
	public static boolean getHideBeforeHash() {
		return hideBeforeHash;
	}
	
	private static void notifyListener() {
		for (Tuple t:listeners) {
			LabelProviderChangedEvent event;
			if (t.labelProvider != null) event =new LabelProviderChangedEvent(t.labelProvider); 
			else event = new LabelProviderChangedEvent(NULL_LABEL_PROVIDER);
			t.listener.labelProviderChanged(event);
		}
	}
	
	public static void addListener(IBaseLabelProvider provider, ILabelProviderListener listener) {
		listeners.add(new Tuple(provider,listener));
	}
	
	public static void removeListener(ILabelProvider provider, ILabelProviderListener listener) {
		Iterator<Tuple> it = listeners.iterator();
		while (it.hasNext()) {
			Tuple current = it.next();
			if ((current.labelProvider == provider) && (current.listener == listener)) {
				it.remove();
			}
		}
	}
	
	private static class Tuple {
		IBaseLabelProvider labelProvider;
		ILabelProviderListener listener;
		
		
		Tuple(IBaseLabelProvider labelProvider, ILabelProviderListener listener) {
			this.labelProvider = labelProvider;
			this.listener = listener;
		}
	}
	
	private static class NullLabelProvider implements IBaseLabelProvider {

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
	
}
