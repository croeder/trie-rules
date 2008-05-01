package de.fzi.ipe.trie.inference;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.inference.RuleBase;
import de.fzi.ipe.trie.inference.executionTree.simple.SimpleExecutionTreeFactory;

public class RuleBaseTest {

	@Test
	public void testGetMatchingRules() {
		Collection<Rule> rules = null;
		try {
			File file = new File("test/de/fzi/ipe/trie/inference/RuleBaseTest.rule");
			rules = RuleParser.readRules(file);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		RuleBase ruleBase = new RuleBase();
		ruleBase.addRules(rules);
		Rule testRule = ruleBase.getRule("testRule");
		for (int i=0;i<testRule.getBody().length;i++) {
			Atom goal = testRule.getBody()[i];
			List<Rule> matchingRules = ruleBase.getMatchingRules(goal, SimpleExecutionTreeFactory.getInstance());
			if (inList("fail"+i,matchingRules))  fail("fail"+i); 
			if (notInList("suc"+i,matchingRules)) fail("suc"+i);
		}
		
	}

	private boolean notInList(String name, List<Rule> rules) {
		for (Rule r:rules) {
			if (r.getName().equals(name)) return false;
		}
		return true;
	}
	
	private boolean inList(String name, List<Rule> rules) {
		for (Rule r:rules) {
			if (r.getName().equals(name)) return true;
		}
		return false;
	}
}
