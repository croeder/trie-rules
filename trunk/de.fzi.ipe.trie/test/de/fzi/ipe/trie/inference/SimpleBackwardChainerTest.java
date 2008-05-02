package de.fzi.ipe.trie.inference;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.inference.executionTree.simple.SimpleBackwardChainer;

public class SimpleBackwardChainerTest {

	static String result1 = 
"?X; \n"+
"http://www.fzi.de/ipe/zach#SOBOLEO; \n";
	
	static String result2 = 
		"?X; ?Y; \n"+
		"http://www.fzi.de/ipe/zach#Person; http://www.w3.org/1999/02/22-rdf-syntax-ns#type; \n"+
		"http://www.fzi.de/ipe/zach#SOBOLEO; http://www.fzi.de/ipe/zach#works_on; \n"+
		"http://www.fzi.de/ipe/zach#Wissensnetz; http://www.fzi.de/ipe/zach#member_of; \n";
	
	static String result3 = 
		"?X; ?Y; ?Z; \n"+
		"http://www.fzi.de/ipe/zach#Person; http://www.w3.org/1999/02/22-rdf-syntax-ns#type; http://www.w3.org/1999/02/22-rdf-syntax-ns#type; \n"+
		"http://www.fzi.de/ipe/zach#SOBOLEO; http://www.fzi.de/ipe/zach#works_on; http://www.fzi.de/ipe/zach#works_on; \n";
			
	static String result4 = 
		"?A; ?B; ?X; \n"+
		"http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#Valentin; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Valentin; http://www.fzi.de/ipe/zach#Simone; http://www.fzi.de/ipe/zach#SOBOLEO; \n"+
		"http://www.fzi.de/ipe/zach#Valentin; http://www.fzi.de/ipe/zach#Valentin; http://www.fzi.de/ipe/zach#SOBOLEO; \n";
	
	
	private KnowledgeBase kb = new KnowledgeBase();
	
	@Before
	public void setUp() throws Exception {
		try {
			kb.addModel("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest.turtle");
			kb.addRules("test/de/fzi/ipe/trie/inference/SimpleBackwardChainerTest.rule");
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
	
	@Test
	public void testHasProof3() {	
		SimpleBackwardChainer reasoner = new SimpleBackwardChainer(kb);
		Rule testRule = kb.getRuleBase().getRule("test3");
		Result result = reasoner.hasProof(testRule.getBody());
		assertEquals(result.toString(), result3);
	}
		
	@Test
	public void testHasProof4() {	
		SimpleBackwardChainer reasoner = new SimpleBackwardChainer(kb);
		Rule testRule = kb.getRuleBase().getRule("test4");
		Result result = reasoner.hasProof(testRule.getBody());
		assertEquals(result.toString(), result4);
		
	}

}
