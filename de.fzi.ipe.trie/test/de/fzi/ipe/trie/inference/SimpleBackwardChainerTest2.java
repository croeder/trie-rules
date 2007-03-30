package de.fzi.ipe.trie.inference;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import de.fzi.ipe.trie.Rule;

public class SimpleBackwardChainerTest2 {
	
	static String result1 =
		"?person; ?werkzeug; \n"+
		"http://www.fzi.de/ipe/zach#Valentin; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Andreas; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#SOBOLEO; \n";
	
	@Test
	public void testHasProof() {
		KnowledgeBase kb = new KnowledgeBase();
		try {
			kb.addModel("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest2.turtle");
			kb.addRules("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest2.rule");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail(ioe.getMessage());
		}
		
		SimpleBackwardChainer reasoner = new SimpleBackwardChainer(kb);
		Rule testRule = kb.getRuleBase().getRule("test1");
		Result result = reasoner.hasProof(testRule.getBody());
		assertEquals(result.toString(), result1);
	}

}
