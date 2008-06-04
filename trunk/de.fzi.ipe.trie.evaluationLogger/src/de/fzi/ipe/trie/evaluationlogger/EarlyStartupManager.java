package de.fzi.ipe.trie.evaluationlogger;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.IStartup;



public class EarlyStartupManager implements IStartup {

	public void earlyStartup() {
//		LoggerImpl.getLogger();
//		FileLogger.getInstance();
//		try {
//			IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
//			IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint("de.fzi.trie.evaluationLogger.Logable");
//			IExtension[] extensions = extensionPoint.getExtensions();
//			for (IExtension ext: extensions) {
//				IConfigurationElement[] configurationElements = ext.getConfigurationElements();
//				for (IConfigurationElement confi: configurationElements) {
//					Logable list = (Logable) confi.createExecutableExtension("ClassName");
//					list.setLogger(LoggerImpl.getLogger());
//				}
//			}
//		} catch (CoreException e) {
//			System.err.println("Evaluation Logger Startup Failed");
//			e.printStackTrace();
//		}
	}

}
