package de.fzi.ipe.trie.inference;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.Term;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFactory;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeRule;
import de.fzi.ipe.trie.util.SetMap;


public class RuleBase {

	private Set<Rule> rules = new HashSet<Rule>();
	
	//ground term indexes
	@SuppressWarnings("unchecked")
	private final SetMap<GroundTerm, Rule>[] groundTermIndexes = new SetMap[3];
	@SuppressWarnings("unchecked")
	private final Set<Rule>[] allGroundTerms = new Set[3];
	
	
	
	@SuppressWarnings("unchecked")
	public RuleBase() {
		for (int i=0;i<3;i++) {
			groundTermIndexes[i] = new SetMap();
			allGroundTerms [i] = new HashSet();
		}
	}
	
	/**
	 * Method to find all rules for which a head atom unifies with the goal. A 
	 * rule may be returned multiple times when it has more than one head, at least two of which unify with
	 * the goal. 
	 * @param newRules
	 */
	public List<Rule> getMatchingRules(Atom goal, ExecutionTreeFactory f) {
		List<Rule> toReturn = new ArrayList<Rule>();
		List<ExecutionTreeRule> base = getExecutionTreeRules(goal,f);
		for (ExecutionTreeRule r: base) {
			toReturn.add(r.getRule());
		}
		return toReturn;		
	}
	
	public Collection<Rule> getAllRules() {
		return Collections.unmodifiableCollection(rules);
	}
	
	
	/**
	 * Find all rules for which a head atom unifies with the goal. A 
	 * rule may be returned multiple times when it has more than one head, at least two of which unify with
	 * the goal. 
	 * @param newRules
	 */
	public List<ExecutionTreeRule> getExecutionTreeRules(Atom goal, ExecutionTreeFactory f) {
		List<ExecutionTreeRule> toReturn = new ArrayList<ExecutionTreeRule>();
		Set<Rule> candidateRules = getCandidateRules(goal);
		for (Rule r:candidateRules) {
			for (Atom a:r.getHead()) {
				if (Unification.canUnify(goal, a, f)) toReturn.add(f.createExecutionTreeRule(r, a));
			}
		}
		return toReturn;
	}
	
	private Set<Rule> getCandidateRules(Atom goal) {
		Set<Rule> toReturn = new HashSet<Rule>();
		toReturn.addAll(rules);
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof GroundTerm) {
				toReturn.removeAll(allGroundTerms[i]);
				toReturn.addAll(groundTermIndexes[i].get((GroundTerm)goal.getTerm(i)));
			}
		}
		return toReturn;
		
	}
	
	public Rule getRule(String name) {
		for (Rule r:rules) {
			if (r.getName().equals(name)) return r;
		}
		return null;
	}
	
	public void addRules(Collection<Rule> newRules) {
		rules.addAll(newRules);
		for (Rule r:newRules) {
			for (Atom head: r.getHead()) indexHead(head,r);
		}
	}
	
	private void indexHead(Atom head, Rule r) {
		for (int i=0;i<3;i++)  {
			Term currentTerm = head.getTerm(i);
			if (currentTerm instanceof GroundTerm) {
				groundTermIndexes[i].put((GroundTerm) currentTerm, r);
				allGroundTerms[i].add(r);
			}
		}
	}

	public void removeRules(Collection<Rule> toRemove) {
		rules.removeAll(toRemove);
	}
	
	
}
