package de.fzi.ipe.trie.filemanagement.extensionPoint;


public interface KnowledgeBaseListener {

	public void setDatamodel(Datamodel dm);
	public void knowledgeBaseChanged();

	
}
