package de.fzi.ipe.trie.filemanagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogSettings;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;
import com.hp.hpl.jena.shared.JenaException;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.RuleParser;
import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;
import de.fzi.ipe.trie.filemanagement.model.SourceFileListener;
import de.fzi.ipe.trie.inference.KnowledgeBase;


/**
 * Holds information about the RDF and rule files of the debugger.
 */
public class SourceFiles implements Datamodel{
	
	private static SourceFiles instance = new SourceFiles();
	
	private KnowledgeBase knowledgeBase = new KnowledgeBase();
	private Set<DebuggerFile> ruleFiles = new HashSet<DebuggerFile>();
	private Set<DebuggerFile> rdfFiles = new HashSet<DebuggerFile>();
	
	private Set<SourceFileListener> listeners = new HashSet<SourceFileListener>();

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
	public void saveFileLocations() { 
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
	public void loadFiles() {
		IDialogSettings favoritesSettings = Activator.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings != null) {
			String rdfFilesString = wizardSettings.get("rdfFiles");
			if (rdfFilesString != null) {
				Set<File> rdfFiles = stringToFileSet(rdfFilesString);
				for (File f:rdfFiles) {
					try {
						addRDFFile(new DebuggerFile(f));
					} catch (FileNotFoundException e) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						e.printStackTrace();
					} catch (JenaException je) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						je.printStackTrace();						
					}
				}
			}
			String ruleFilesString = wizardSettings.get("ruleFiles");
			if (ruleFilesString != null) {
				Set<File> ruleFiles = stringToFileSet(ruleFilesString);
				for (File f:ruleFiles) {
					try {
						addRuleFile(new DebuggerFile(f));						
					} catch (IOException e) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						e.printStackTrace();
					} catch (JenaException e) {
						System.err.println("Could not reload file "+f.toString()+" - no big deal. ");
						e.printStackTrace();						
					}
				}
			}
		}
		notifyListenersLoaded();
	}
	
	private static String fileSetToString(Set<DebuggerFile> files) {
		StringBuilder builder = new StringBuilder();
		for (DebuggerFile f:files) {
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
	
	public void addListener(SourceFileListener list) {
		listeners.add(list);
	}
	
	public void removeListener(SourceFileListener list) {
		listeners.remove(list);
	}
	
	private void notifyListenerFilesChanged() {
		for (SourceFileListener l: listeners) l.filesChanged();
	}
	
	private void notifyListenersLoaded() {
		for (SourceFileListener l:listeners) l.loaded();
	}
	
	public KnowledgeBase getKnowledgeBase() {
		return knowledgeBase;
	}
	
	public void reload() throws IOException {
		knowledgeBase.clear();
		for (DebuggerFile f:rdfFiles) {
			addRDFToKB(f);
		}
		for (DebuggerFile f:ruleFiles) {
			addRuleToKB(f);
		}
		notifyListenersLoaded();
	}
	
	public Set<DebuggerFile> getRuleFiles() {
		return Collections.unmodifiableSet(ruleFiles);
	}
	
	public Set<DebuggerFile> getRDFFiles() {
		return Collections.unmodifiableSet(rdfFiles);
	}
	
	public void addRDFFile(DebuggerFile file) throws FileNotFoundException {
		if (!rdfFiles.contains(file)) {
			addRDFToKB(file);
			rdfFiles.add(file);
			notifyListenerFilesChanged();
		}
	}
	
	public void addRuleFile(DebuggerFile file) throws IOException {
		if (!ruleFiles.contains(file)) {
			addRuleToKB(file);
			ruleFiles.add(file);
			notifyListenerFilesChanged();
		}
	}
	
	private void addRDFToKB(DebuggerFile file) throws FileNotFoundException {
		ModelMaker maker = ModelFactory.createMemModelMaker();
		Model model = maker.createDefaultModel();
		if (file.isTurtleFile()) model.read(file.openInputStream(), null, "TURTLE");
		else model.read(file.openInputStream(), null, "RDF/XML");
		knowledgeBase.addModel(model); 
	}

	private void addRuleToKB(DebuggerFile debuggerFile) throws IOException {
		Collection<Rule> rules = RuleParser.readRules(debuggerFile.getFile()); 
		knowledgeBase.addRules(rules); 
	}

	public void removeRDFFile(DebuggerFile toRemove) throws IOException {
		rdfFiles.remove(toRemove);
		reload();
		notifyListenerFilesChanged();
	}
	
	public void removeRuleFile(DebuggerFile toRemove) throws IOException {
		ruleFiles.remove(toRemove);
		reload();
		notifyListenerFilesChanged();
	}
	

	
}
