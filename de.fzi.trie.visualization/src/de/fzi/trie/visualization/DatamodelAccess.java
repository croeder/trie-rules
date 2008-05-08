package de.fzi.trie.visualization;

import de.fzi.ipe.trie.filemanagement.extensionPoint.Datamodel;
import de.fzi.ipe.trie.filemanagement.extensionPoint.FileManagementListener;

public class DatamodelAccess implements FileManagementListener{

	private static Datamodel dm;
	
	protected static Datamodel getDatamodel() {
		return dm;
	}
	
	private static void setDatamodelSingleton(Datamodel datamodel) {
		dm = datamodel;
	}
	
	public void setDatamodel(Datamodel dm) {
		DatamodelAccess.setDatamodelSingleton(dm);
	}


	
}
