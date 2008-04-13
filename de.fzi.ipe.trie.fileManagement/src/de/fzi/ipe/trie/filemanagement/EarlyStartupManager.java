package de.fzi.ipe.trie.filemanagement;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;

import de.fzi.ipe.trie.filemanagement.extensionPoint.FileManagementListener;
import de.fzi.ipe.trie.filemanagement.model.SourceFiles;

public class EarlyStartupManager implements IStartup{


	public void earlyStartup() {
		SourceFiles instance = SourceFiles.getInstance();
		
		try {
			IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("de.fzi.ipe.trie.fileManagement.fileManagementListener");
			IExtension[] extensions = extensionPoint.getExtensions();
			for (IExtension ext: extensions) {
				IConfigurationElement[] configurationElements = ext.getConfigurationElements();
				for (IConfigurationElement confi: configurationElements) {
					FileManagementListener list = (FileManagementListener) confi.createExecutableExtension("ClassName");
					list.setDatamodel(instance);
				}
			}
		} catch (CoreException e) {
			System.err.println("File Management EarlyStartupManager failed to process extensions!");
			e.printStackTrace();
		}
		
	
	}

}
