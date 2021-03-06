package de.fzi.ipe.trie.inference.executionTree.simple;

import java.util.HashMap;
import java.util.Map;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.inference.ProofVariable;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeQuery;

public class ExecutionTreeQueryImpl extends ExecutionTreeInstantiationImpl implements ExecutionTreeQuery{	

	protected Map<Variable,ProofVariable> vars = new HashMap<Variable,ProofVariable>();
		
	public Map<Variable,ProofVariable> getVariableMap() {
		return vars;
	}	
	
	protected ExecutionTreeQueryImpl (Atom[] body) {
		body = body.clone();
		for (int i=0;i<body.length;i++) {
			body[i] = processAtom(body[i]);
		}
		this.body = body;
	}
	
	private Atom processAtom(Atom a) {
		a = a.clone();
		for (int i=0;i<3;i++) {
			if (a.getTerm(i) instanceof Variable) {
				ProofVariable v = vars.get((Variable)a.getTerm(i));
				if (v == null) {
					v = new ProofVariable(((Variable)a.getTerm(i)).getName());
					vars.put((Variable) a.getTerm(i),v);
				}
				a.replace((Variable)a.getTerm(i), v);
			}
		}
		return a;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[query:\n");
		builder.append("<-\n");
		for (Atom a:body) builder.append("\t"+a+"\n");
		builder.append("]");
		return builder.toString();
	}
	
	
}
