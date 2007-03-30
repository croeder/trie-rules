package de.fzi.ipe.trie.inference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import de.fzi.ipe.trie.Atom;
import de.fzi.ipe.trie.LiteralTerm;
import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.URITerm;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeFacts;

/**
 * Set of facts and rules 
 */
public class KnowledgeBase {

	private Model model;
	private RuleBase ruleBase = new RuleBase();

	
	
	public KnowledgeBase() {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		model = maker.createDefaultModel();		
	}
	
	public void clear() {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		model = maker.createDefaultModel();		
		ruleBase = new RuleBase();
	}
	
	public KnowledgeBase(Model model, Collection<Rule> rules) {
		this();
		this.model.add(model);
		this.ruleBase.addRules(rules);
	}
	
	public void addModel(Model model) {
		this.model.add(model);
	}
	
	public void addModel(String path) throws IOException {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		Model model = maker.createDefaultModel();
		model.read(new FileInputStream(path), null, "TURTLE");
		this.model.add(model);
	}
	
	public void addRules(String path) throws IOException {
		File file = new File(path);
		Collection<Rule> rules = RuleParser.readRules(file);
		this.ruleBase.addRules(rules);
	}
	
	public void addRules(Collection<Rule> rules) {
		ruleBase.addRules(rules);
	}
	
	public RuleBase getRuleBase() {
		return ruleBase;
	}
	
	public StmtIterator getStatements(ExecutionTreeFacts facts) {
		Resource subject = null;
		Property property = null;
		RDFNode object =null;
		
		Atom goal = facts.getGoal();
		if (goal.getSubject() instanceof URITerm) subject = model.getResource(((URITerm) goal.getSubject()).getUri());
		if (goal.getPredicate() instanceof URITerm) property = model.getProperty(((URITerm) goal.getPredicate()).getUri());
		if (goal.getObject() instanceof URITerm) object = model.getResource(((URITerm) goal.getObject()).getUri());
		else if (goal.getObject() instanceof LiteralTerm) {
			String literalValue = ((LiteralTerm)goal.getObject()).getLiteral(); 
			object = model.createLiteral(literalValue);
		}
		
		StmtIterator iterator = model.listStatements(subject, property, object);
		return iterator;
	}
	
}
