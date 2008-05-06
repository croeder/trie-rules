package de.fzi.ipe.trie.inference;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
	public static int unify(Atom goal, Atom other, ExecutionTreeElement elem, VariableBindings vb, int maxEdits) {
		processAtom(other);
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof ProofVariable && other.getTerm(i) instanceof ProofVariable) {
				other.replace((ProofVariable)other.getTerm(i), (ProofVariable) goal.getTerm(i));
			}
		}
		return unifyCore(goal,other,elem,vb,maxEdits);
	}

	public static int unify(Atom goal, ExecutionTreeRule rule, VariableBindings vb, int maxEdits) {
		processRule(rule);
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof ProofVariable && rule.getHead().getTerm(i) instanceof ProofVariable) {
				rule.replace((ProofVariable)rule.getHead().getTerm(i), (ProofVariable) goal.getTerm(i));
			}
		}
		return unifyCore(goal,rule.getHead(),rule,vb,maxEdits);		
	}
	
	
	private static int unifyCore(Atom goal, Atom other, ExecutionTreeElement elem, VariableBindings vb, int maxEdits) {
		int edits = 0;
		for (int i=0;i<3;i++) {
			Term goalTerm = vb.getCurrentTerm(goal.getTerm(i));
			Term otherTerm = vb.getCurrentTerm(other.getTerm(i));
			if (goalTerm instanceof ProofVariable && otherTerm instanceof ProofVariable) {
				if (goalTerm != otherTerm) edits = 10000; 
			}
			else if (goalTerm instanceof GroundTerm && otherTerm instanceof GroundTerm) {
				if (goalTerm.equals(otherTerm)) edits +=0; 
				else edits += almostEqual((GroundTerm)goalTerm, (GroundTerm) otherTerm);
			}
			else if (goalTerm instanceof ProofVariable) {
				vb.addVariableBinding((ProofVariable)goalTerm, (GroundTerm) otherTerm, elem);
			}
			else if (otherTerm instanceof ProofVariable) {
				vb.addVariableBinding((ProofVariable) otherTerm, (GroundTerm) goalTerm, elem);
			}
		}
		if (edits > maxEdits) {
			vb.removeBindings(elem);
			return -1;
		}
		else return edits;
	}
	

	/**
	 * Returns true iff 1) a and b are not equal and 2) the edit distance
	 * between the uris of a and b is very small. 
	 */
	private static int almostEqual(GroundTerm a, GroundTerm b) {
		return StringUtils.getLevenshteinDistance(a.toString(), b.toString());
	}
	
	
	/**
	 * 
	 * @return the number of edits that is necessary to match the goal to the atom other
	 */
	public static int canUnify(Atom goal, Atom other, ExecutionTreeFactory f, int maxEdits) {
		goal = goal.clone();
		processAtom(goal);
		other = other.clone();
		return unify(goal,other, f.createExecutionTreeElement(), new VariableBindings(),maxEdits);		
	}
	
}
