package de.fzi.ipe.trie.inference;

import java.util.HashMap;
import java.util.Map;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.Variable;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeElement;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFactory;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;


/** some util methods for unification. Should be moved into some other class, but for now don't know in which ... */
public class Unification {

	private static Map<Variable,ProofVariable> varsHelper = new HashMap<Variable,ProofVariable>();

	/**
	 * Replaces all Variables with ProofVariables
	 */
	public static void processAtom(Atom a) {
		varsHelper.clear();
		processAtomAsRulePart(a);
	}
	
	private static void processAtomAsRulePart(Atom a) {
		for (int i=0;i<3;i++) {
			if (a.getTerm(i) instanceof Variable) {
				ProofVariable v = varsHelper.get((Variable)a.getTerm(i));
				if (v == null) {
					v = new ProofVariable(((Variable)a.getTerm(i)).getName());
					varsHelper.put((Variable) a.getTerm(i),v);
				}
				a.replace((Variable)a.getTerm(i), v);
			}
		}
	}

	private static void processRule(ExecutionTreeRule rule) {
		varsHelper.clear();
		processAtomAsRulePart(rule.getHead());
		for (Atom a:rule.getBody()) {
			processAtomAsRulePart(a);
		}
	}
	
	
	//! the atom other is changed! (the variables are replaced). 
	public static boolean unify(Atom goal, Atom other, ExecutionTreeElement elem, VariableBindings vb) {
		processAtom(other);
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof ProofVariable && other.getTerm(i) instanceof ProofVariable) {
				other.replace((ProofVariable)other.getTerm(i), (ProofVariable) goal.getTerm(i));
			}
		}
		return unifyCore(goal,other,elem,vb);
	}

	public static boolean unify(Atom goal, ExecutionTreeRule rule, VariableBindings vb) {
		processRule(rule);
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof ProofVariable && rule.getHead().getTerm(i) instanceof ProofVariable) {
				rule.replace((ProofVariable)rule.getHead().getTerm(i), (ProofVariable) goal.getTerm(i));
			}
		}
		return unifyCore(goal,rule.getHead(),rule,vb);		
	}
	
	
	private static boolean unifyCore(Atom goal, Atom other, ExecutionTreeElement elem, VariableBindings vb) {
		boolean failed = false;
		for (int i=0;i<3;i++) {
			Term goalTerm = vb.getCurrentTerm(goal.getTerm(i));
			Term otherTerm = vb.getCurrentTerm(other.getTerm(i));
			if (goalTerm instanceof ProofVariable && otherTerm instanceof ProofVariable) {
				if (goalTerm != otherTerm) failed = true; 
			}
			else if (goalTerm instanceof GroundTerm && otherTerm instanceof GroundTerm) {
				if (!goalTerm.equals(otherTerm)) failed = true;
			}
			else if (goalTerm instanceof ProofVariable) {
				vb.addVariableBinding((ProofVariable)goalTerm, (GroundTerm) otherTerm, elem);
			}
			else if (otherTerm instanceof ProofVariable) {
				vb.addVariableBinding((ProofVariable) otherTerm, (GroundTerm) goalTerm, elem);
			}
		}
		if (failed) {
			vb.removeBindings(elem);
			return false;
		}
		else return true;
	}
	
	
	public static boolean canUnify(Atom goal, Atom other, ExecutionTreeFactory f) {
		goal = goal.clone();
		processAtom(goal);
		other = other.clone();
		return unify(goal,other, f.createExecutionTreeElement(), new VariableBindings());		
	}
	
}
