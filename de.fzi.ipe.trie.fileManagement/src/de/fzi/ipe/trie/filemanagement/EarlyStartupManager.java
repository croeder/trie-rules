package de.fzi.ipe.trie.filemanagement;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;

import de.fzi.ipe.trie.filemanagement.extensionPoint.KnowledgeBaseListener;

public class EarlyStartupManager implements IStartup{


	public void earlyStartup() {
		SourceFiles instance = SourceFiles.getInstance();
		
		try {
			IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("de.fzi.ipe.trie.fileManagement.knowledgeBaseListener");
			IExtension[] extensions = extensionPoint.getExtensions();
			for (IExtension ext: extensions) {
				IConfigurationElement[] configurationElements = ext.getConfigurationElements();
				for (IConfigurationElement confi: configurationElements) {
					KnowledgeBaseListener list = (KnowledgeBaseListener) confi.createExecutableExtension("ClassName");
					list.setDatamodel(instance);
				}
			}
		} catch (CoreException e) {
			System.err.println("File Management EarlyStartupManager failed to process extensions!");
			e.printStackTrace();
		}
		
	
	}

}
