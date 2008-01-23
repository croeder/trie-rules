package de.fzi.ipe.trie.debugger.gui;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Term;

public class DebugLabelProvider {
	
	public boolean hideBeforeHash = true;
	
	
	public String getLabel(Term term) {
		if (term == null) return null;
		else if (hideBeforeHash && term instanceof GroundTerm) {
			String temp = term.toString();
			if (temp.indexOf('#') != -1) return temp.substring(temp.indexOf('#')+1);
			else return temp;
		}
		return term.toString();
	}
	
	public String getLabel(Atom atom) {
		return getLabel(atom.getSubject())+", "+getLabel(atom.getPredicate())+", " +getLabel(atom.getObject())+"";
	}
	

}
