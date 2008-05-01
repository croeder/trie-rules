package de.fzi.ipe.trie.inference.executionTree.simple;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.LiteralTerm;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.URITerm;
import de.fzi.ipe.trie.inference.KnowledgeBase;
import de.fzi.ipe.trie.inference.Unification;
import de.fzi.ipe.trie.inference.VariableBindings;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;

public class ExecutionTreeFactsImpl extends ExecutionTreeElementImpl implements ExecutionTreeFacts {

	private Set<Atom> facts;
	private Iterator<Statement>  iterator;
	
	private Atom goal;
	
	private Atom lastAtom=null;
	
	@SuppressWarnings("unchecked")
	protected ExecutionTreeFactsImpl(Atom goal,KnowledgeBase kb) {
		this.goal = goal;
		Set<Statement> statements = kb.getStatements(this).toSet();
		facts = new HashSet<Atom>();
		for (Statement s:statements) facts.add(getAtom(s));
		iterator = statements.iterator();
	}
	
	public Set<Atom> getFacts() {
		return facts;
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
			Statement statement = iterator.next();
			lastAtom = getAtom(statement);
			return lastAtom;
		}
		return null;
	}

	private Atom getAtom(Statement statement) {
		Term s = new URITerm(statement.getSubject());
		Term p = new URITerm(statement.getPredicate());
		Term o = null;
		if (statement.getObject() instanceof Resource) {
			o = new URITerm((Resource) statement.getObject());
		}
		else {
			o = new LiteralTerm((Literal) statement.getObject());
		}
		return new Atom(s,p,o);
	}
	
	public String toString() {
		return "Facts matching: "+goal;
	}
	
}
