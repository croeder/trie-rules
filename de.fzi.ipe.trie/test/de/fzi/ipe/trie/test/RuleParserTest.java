package de.fzi.ipe.trie.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;

public class RuleParserTest {

	private static String compareString = 
		"[projectUsesTool:\n	(?A,http://www.fzi.de/ipe/zach#uses,?C)\n<-\n	(?B,http://www.fzi.de/ipe/zach#works_on,?C)\n	(?B,http://www.fzi.de/ipe/zach#member_of,?A)\n	(?A,http://www.w3.org/1999/02/22-rdf-syntax-ns#type,http://www.fzi.de/ipe/zach#Project)\n]";
	
	@Test
	public void testReadRulesFile() {
		File file = new File("test/de/fzi/ipe/trie/test/RuleParserTest.rule");
		try {
			Collection<Rule> rules = RuleParser.readRules(file);
			for (Rule r:rules) {
				assertEquals(compareString, r.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

}
