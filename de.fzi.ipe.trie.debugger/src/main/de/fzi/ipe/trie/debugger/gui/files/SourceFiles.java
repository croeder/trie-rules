package de.fzi.ipe.trie.debugger.gui.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.debugger.model.DebuggerRuleStore;


/**
 * Holds information about the rdf and rule files of the debugger.
 */
public class SourceFiles {

	
	private static SourceFiles instance = new SourceFiles();
	
	
	private Set<File> ruleFiles = new HashSet<File>();
	private Set<File> rdfFiles = new HashSet<File>();
	

	public static SourceFiles getInstance() {
		return instance;
	}

	
	private SourceFiles() {
		
	}
	
	public void reload() throws IOException {
		for (File f:rdfFiles) {
			addRDFToKB(f);
		}
		for (File f:ruleFiles) {
			addRuleToKB(f);
		}	
	}
	
	public Set<File> getRuleFiles() {
		return ruleFiles;
	}
	
	public Set<File> getRDFFiles() {
		return rdfFiles;
	}
	
	public void addRDFFile(File file) throws FileNotFoundException {
		if (!rdfFiles.contains(file)) {
			addRDFToKB(file);
		}
		rdfFiles.add(file);
	}
	
	public void addRuleFile(File file) throws IOException {
		if (!ruleFiles.contains(file)) {
			addRuleToKB(file);
		}
		ruleFiles.add(file);
	}
	
	private void addRDFToKB(File file) throws FileNotFoundException {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		Model model = maker.createDefaultModel();
		if (file.getPath().toLowerCase().endsWith(".turtle")) {
			model.read(new FileInputStream(file), null, "TURTLE");
		}
		else model.read(new FileInputStream(file), null, "RDF/XML");
		DebuggerRuleStore.getKnowledgeBase().addModel(model);
	}

	private void addRuleToKB(File file) throws IOException {
		Collection<Rule> rules = RuleParser.readRules(file);
		DebuggerRuleStore.getKnowledgeBase().addRules(rules);
	}
	
	
	public void addsomething() {
		;
	}
	

	public void removeRDFFile(File toRemove) {
		rdfFiles.remove(toRemove);
		
	}
	
	public void removeRuleFile(File toRemove) {
		ruleFiles.remove(toRemove);
	}
}
