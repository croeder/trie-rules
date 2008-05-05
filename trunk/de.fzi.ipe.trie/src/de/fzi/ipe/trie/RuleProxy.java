package de.fzi.ipe.trie;


/**
 * Used to represent 'assumed' rules. Rules that are only returned because they can be
 * 'almost' unified. 
 * @author zach
 *
 */
public class RuleProxy extends Rule{
	
	private Atom goal; //The goal that almost matched this rule
	
	public RuleProxy(String name, String comment, Atom[] head, Atom[] body, Atom goal) {
		super(name, comment, head, body);
		this.goal = goal;
	}
	
	public Atom getGoal() {
		return goal;
	}
	
}
