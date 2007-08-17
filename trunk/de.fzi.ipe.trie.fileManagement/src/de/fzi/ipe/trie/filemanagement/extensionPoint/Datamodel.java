package de.fzi.ipe.trie.filemanagement.extensionPoint;

import java.io.IOException;

import de.fzi.ipe.trie.inference.KnowledgeBase;

public interface Datamodel {

	public KnowledgeBase getKnowledgeBase();
	public void reload() throws IOException;

}
