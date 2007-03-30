package de.fzi.ipe.trie.inference.executionTree;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.LiteralTerm;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.URITerm;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Unification;
import de.fzi.ipe.trie.inference.VariableBindings;

public class ExecutionTreeFacts extends ExecutionTreeElement {

	private StmtIterator iterator;
	private Atom goal;
	
	private Atom lastAtom=null;
	
	public ExecutionTreeFacts(Atom goal,KnowledgeBase kb) {
		this.goal = goal;
		iterator = kb.getStatements(this);
	}
	
	public Atom getGoal() {
		return goal;
	}
	
	public boolean next(VariableBindings vb) {
		vb.removeBindings(this);
		Atom a;
		while ((a = getNextAtom()) != null) {
			if (Unification.unify(goal, a, this, vb)) return true;
			else vb.removeBindings(this);
		}
		return false;
	}
	
	public Atom getLastAtom() {
		return lastAtom;
	}
	
	private Atom getNextAtom() {
		if (iterator.hasNext()) {
			Statement statement = iterator.nextStatement();
			Term s = new URITerm(statement.getSubject());
			Term p = new URITerm(statement.getPredicate());
			Term o = null;
			if (statement.getObject() instanceof Resource) {
				o = new URITerm((Resource) statement.getObject());
			}
			else {
				o = new LiteralTerm((Literal) statement.getObject());
			}
			lastAtom = new Atom(s,p,o);
			return lastAtom;
		}
		return null;
	}
	
	public String toString() {
		return "Facts matching: "+goal;
	}
	
}
