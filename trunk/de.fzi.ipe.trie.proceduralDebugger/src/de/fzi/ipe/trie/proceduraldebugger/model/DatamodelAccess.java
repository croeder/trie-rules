package de.fzi.ipe.trie.proceduraldebugger.model;

import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.extensionPoint.FileManagementListener;
import de.fzi.ipe.trie.inference.KnowledgeBase;

public class DatamodelAccess implements FileManagementListener{

	private static Datamodel dm;
	
	public static KnowledgeBase getKnowledgeBase() {
		return dm.getKnowledgeBase();
	}
	
	public void knowledgeBaseChanged() {
		;
	}

	public void setDatamodel(Datamodel dm) {
		DatamodelAccess.dm = dm;
	}

	
}
