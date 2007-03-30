package de.fzi.ipe.trie.inference;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;

import org.junit.Test;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;

public class KnowledgeBaseTest {

	@Test
	public void testGetFacts() {
		Collection<Rule> rules = null;
		try {
			File file = new File("test/de/fzi/ipe/trie/inference/KnowledgeBaseTest.rule");
			rules = RuleParser.readRules(file);
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		Model model = null;
		try {
			ModelMaker maker = ModelFactory.createMemModelMaker();
			model = maker.createDefaultModel();
			model.read(new FileInputStream("test/de/fzi/ipe/trie/inference/KnowledgeBaseTest.turtle"), null, "TURTLE");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
		
		KnowledgeBase knowledgeBase = new KnowledgeBase(model,rules);
		Rule testRule = knowledgeBase.getRuleBase().getRule("testFail");
		for (int i=0;i<testRule.getBody().length;i++) {
			Atom goal = testRule.getBody()[i];
			Unification.processAtom(goal);
			ExecutionTreeFacts facts = new ExecutionTreeFacts(goal,knowledgeBase);
			if (facts.next(new VariableBindings())) fail("testfail "+i);
		}

		testRule = knowledgeBase.getRuleBase().getRule("testSuc");
		for (int i=0;i<testRule.getBody().length;i++) {
			Atom goal = testRule.getBody()[i];
			Unification.processAtom(goal);
			ExecutionTreeFacts facts = new ExecutionTreeFacts(goal,knowledgeBase);
			if (!facts.next(new VariableBindings())) fail("testsuc "+i);
		}

		
	}

}
