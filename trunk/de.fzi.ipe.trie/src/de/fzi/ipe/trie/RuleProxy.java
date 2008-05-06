package de.fzi.ipe.trie;


/**
 * Used to represent 'assumed' rules. Rules that are only returned because they can be
 * 'almost' unified. 
 * @author zach
 *
 */
public interface RuleProxy extends Rule{
	
	public Atom getGoal();
	public Rule getRule();
	
}
