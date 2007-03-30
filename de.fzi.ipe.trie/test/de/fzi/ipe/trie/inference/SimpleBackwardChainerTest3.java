package de.fzi.ipe.trie.inference;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.fzi.ipe.trie.Rule;

public class SimpleBackwardChainerTest3 {
	
	static String result1 =
		"?name; ?werkzeug; \n"+
		"\"valentin\"; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"\"simone\"; http://www.fzi.de/ipe/zach#SOBOLEO; \n";

	static String result2 = 
		"?werkzeug; \n"+
		"http://www.fzi.de/ipe/zach#SOBOLEO; \n";
	
	KnowledgeBase kb = new KnowledgeBase();

	
	
	@Before
	public void setUp() throws Exception {
		try {
			kb.addModel("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest3.turtle");
			kb.addRules("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest3.rule");
		} catch (IOException ioe) {
			ioe.printStackTrace();
			fail(ioe.getMessage());
		}		
	}
	
	@Test
	public void testHasProof() {	
		SimpleBackwardChainer reasoner = new SimpleBackwardChainer(kb);
		Rule testRule = kb.getRuleBase().getRule("test1");
		Result result = reasoner.hasProof(testRule.getBody());
		assertEquals(result.toString(), result1);
	}

	@Test
	public void testHasProof2() {	
		SimpleBackwardChainer reasoner = new SimpleBackwardChainer(kb);
		Rule testRule = kb.getRuleBase().getRule("test2");
		Result result = reasoner.hasProof(testRule.getBody());
		assertEquals(result.toString(), result2);
	}

	
}
