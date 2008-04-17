package de.fzi.ipe.trie;

import java.util.ArrayList;
import java.util.List;

import de.fzi.ipe.trie.inference.ProofVariable;


/**
 * An atom - or in fact a rdf tripple.   
 * @author zach
 *
 */
public class Atom {
	
	private Term s,p,o;
	
	public Atom(Term s,Term p, Term o) {
		this.p = p;
		this.o = o;
		this.s = s;
	}
	
	public Term getPredicate() {
		return p;
	}
	
	public Term getObject() {
		return o;
	}
	
	public Term getSubject() {
		return s;
	}
	
	public List<Variable> getVariables() {
		List<Variable> toReturn = new ArrayList<Variable>();
		for (int i=0;i<3;i++) {
			if (getTerm(i) instanceof Variable) toReturn.add((Variable) getTerm(i));
		}
		return toReturn;
	}
	
	public String toString() {
		return "("+s+","+p+","+o+")";
	}

	public Atom clone() {
		return new Atom(s,p,o);
	}
	
	public Term getTerm(int i) {
		switch (i) {
			case 0: return s; 
			case 1: return p;
			case 2: return o;
		}
		return null;
	}

	public void replace(Variable a, ProofVariable b) {
		if (s.equals(a)) s=b;
		if (p.equals(a)) p=b;
		if (o.equals(a)) o=b;
	}

	//probably remove this later.
	public void replace(ProofVariable a, ProofVariable b) {
		if (s.equals(a)) s=b;
		if (p.equals(a)) p=b;
		if (o.equals(a)) o=b;
	}
	
}