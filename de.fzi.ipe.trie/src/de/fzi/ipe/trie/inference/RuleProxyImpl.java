package de.fzi.ipe.trie.inference;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.GroundTerm;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleProxy;



/**
 * Used to represent a rule that is only returned because it can be 'almost' unified
 */
public class RuleProxyImpl implements RuleProxy{
	
	private Rule rule;
	
	private Atom goal,head;
	
	
	public RuleProxyImpl(Rule rule, Atom goal, Atom head) {
		this.rule = rule;
		this.goal = goal;
		this.head = head;
	}
	
	public Atom getGoal() {
		return goal;
	}

	public Atom[] getBody() {
		return rule.getBody();
	}

	public String getComment() {
		return rule.getComment();
	}

	public Atom[] getHead() {
		return rule.getHead();
	}

	public String getName() {
		return rule.getName();
	}
	
	public Rule getRule() {
		return rule;
	}

	public String getExplanation() {
		StringBuilder toReturn = new StringBuilder();
		for (int i=0;i<3;i++) {
			if (goal.getTerm(i) instanceof GroundTerm && head.getTerm(i) instanceof GroundTerm) {
				GroundTerm g1 = (GroundTerm) goal.getTerm(i);
				GroundTerm g2 = (GroundTerm) head.getTerm(i);
				if (!g1.equals(g2)) {
					toReturn.append("Rule head \""+g2.toString()+"\"\n");
					toReturn.append("and the goal \""+g1.toString()+"\"\n");
				}
			}
		}
		return toReturn.toString();
	}
	
	
}
