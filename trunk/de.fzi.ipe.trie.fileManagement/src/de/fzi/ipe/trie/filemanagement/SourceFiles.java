package de.fzi.ipe.trie.filemanagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogSettings;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.extensionPoint.FileManagementListener;
import de.fzi.ipe.trie.inference.KnowledgeBase;


/**
 * Holds information about the RDF and rule files of the debugger.
 */
public class SourceFiles implements Datamodel{
	
	private static SourceFiles instance = new SourceFiles();
	
	private KnowledgeBase knowledgeBase = new KnowledgeBase();
	private Set<FileManagementListener> listeners = new HashSet<FileManagementListener>();
	private Set<File> ruleFiles = new HashSet<File>();
	private Set<File> rdfFiles = new HashSet<File>();
	

	public static SourceFiles getInstance() {
		return instance;
	}

	private SourceFiles() {
		loadFiles();
	}
	
	/**
	 * Stores the location of all files currently active - used to reload these files the next time the program 
	 * is opened. 
	 * @see SourceFiles#loadFiles()
	 */
	protected void saveFileLocations() { 
		IDialogSettings favoritesSettings = Activator.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings == null) {
			wizardSettings = favoritesSettings.addNewSection("fileChooser");
		}
		wizardSettings.put("rdfFiles",fileSetToString(rdfFiles));
		wizardSettings.put("ruleFiles",fileSetToString(ruleFiles));
	}

	/**
	 * Loads the files that were active at the time the program was terminated the last time. 
	 */
	protected void loadFiles() {
		IDialogSettings favoritesSettings = Activator.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings != null) {
			String rdfFilesString = wizardSettings.get("rdfFiles");
			if (rdfFilesString != null) {
				Set<File> rdfFiles = stringToFileSet(rdfFilesString);
				for (File f:rdfFiles) {
					try {
						addRDFFile(f);
					} catch (FileNotFoundException e) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						e.printStackTrace();
					}
				}
			}
			String ruleFilesString = wizardSettings.get("ruleFiles");
			if (ruleFilesString != null) {
				Set<File> ruleFiles = stringToFileSet(ruleFilesString);
				for (File f:ruleFiles) {
					try {
						addRuleFile(f);						
					} catch (IOException e) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static String fileSetToString(Set<File> files) {
		StringBuilder builder = new StringBuilder();
		for (File f:files) {
			builder.append(f.getAbsolutePath());
			builder.append(";;");
		}
		return builder.toString();
	}

	private static Set<File> stringToFileSet(String string) {
		String[] paths = string.split(";;");
		Set<File> toReturn = new HashSet<File>();
		for (String p: paths) {
			toReturn.add(new File(p));
		}
		return toReturn;
	}
	
	public void addListener(FileManagementListener list) {
		listeners.add(list);
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
		}
	}
	
	public void addRuleFile(File file) throws IOException {
		if (!ruleFiles.contains(file)) {
			addRuleToKB(file);
			ruleFiles.add(file);
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

	public void removeRDFFile(File toRemove) throws IOException {
		rdfFiles.remove(toRemove);
		reload();
	}
	
	public void removeRuleFile(File toRemove) throws IOException {
		ruleFiles.remove(toRemove);
		reload();
	}
}
