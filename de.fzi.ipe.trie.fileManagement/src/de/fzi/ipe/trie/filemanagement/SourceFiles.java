package de.fzi.ipe.trie.filemanagement;

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
import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.extensionPoint.KnowledgeBaseListener;
import de.fzi.ipe.trie.inference.KnowledgeBase;


/**
 * Holds information about the rdf and rule files of the debugger.
 */
public class SourceFiles implements Datamodel{
	
	private static SourceFiles instance = new SourceFiles();
	
	private KnowledgeBase knowledgeBase = new KnowledgeBase();
	private Set<KnowledgeBaseListener> listeners = new HashSet<KnowledgeBaseListener>();
	private Set<File> ruleFiles = new HashSet<File>();
	private Set<File> rdfFiles = new HashSet<File>();
	

	public static SourceFiles getInstance() {
		return instance;
	}

	
	private SourceFiles() {
	}
	
	public void addListener(KnowledgeBaseListener list) {
		listeners.add(list);
	}
	
	private void notifyListeners() {
		for (KnowledgeBaseListener lis: listeners) lis.knowledgeBaseChanged();
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
	
	public void reload() throws IOException {
		knowledgeBase.clear();
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
			rdfFiles.add(file);
			notifyListeners();
		}
	}
	
	public void addRuleFile(File file) throws IOException {
		if (!ruleFiles.contains(file)) {
			addRuleToKB(file);
			ruleFiles.add(file);
			notifyListeners();
		}
	}
	
	private void addRDFToKB(File file) throws FileNotFoundException {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		Model model = maker.createDefaultModel();
		if (file.getPath().toLowerCase().endsWith(".turtle")) {
			model.read(new FileInputStream(file), null, "TURTLE");
		}
		else model.read(new FileInputStream(file), null, "RDF/XML");
		knowledgeBase.addModel(model); 
	}

	private void addRuleToKB(File file) throws IOException {
		Collection<Rule> rules = RuleParser.readRules(file); 
		knowledgeBase.addRules(rules); 
	}

	public void removeRDFFile(File toRemove) {
		rdfFiles.remove(toRemove);
		//TODO not removed from kb.
	}
	
	public void removeRuleFile(File toRemove) {
		ruleFiles.remove(toRemove);
		//TODO not removed from kb. 
	}
}
