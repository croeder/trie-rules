package de.fzi.ipe.trie.filemanagement;

import java.util.HashSet;
import java.util.Set;

import de.fzi.ipe.trie.evaluationlogger.Logable;
import de.fzi.ipe.trie.evaluationlogger.Logger;
import de.fzi.ipe.trie.filemanagement.extensionPoint.SourceFileListener;
import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class DebugLogger implements Logable, SourceFileListener{

	private static Logger logger;
	private static SourceFiles sourceFiles;
	
	public void setLogger(Logger logger) {
		DebugLogger.setLoggerInstance(logger);
	}

	private static void setLoggerInstance(Logger logger) {
		DebugLogger.logger = logger;
		sourceFiles = SourceFiles.getInstance();
		sourceFiles.addListener(new DebugLogger());
	}
	
	public static void log(String... data) {
		String[] toLog = new String[data.length+1];
		toLog[0] = "FileManagement";
		for (int i=1;i<toLog.length;i++) toLog[i] = data[i-1];
		logger.log(toLog);
	}

	public void filesChanged() {
		Set<DebuggerFile> files = new HashSet<DebuggerFile>();
		files.addAll(sourceFiles.getRDFFiles());
		files.addAll(sourceFiles.getRuleFiles());
		StringBuilder builder = new StringBuilder();
		for (DebuggerFile file: files) builder.append(file.getName()+"; ");
		log("FilesChanged",builder.toString());
	}

	public void loaded() {
		log("FilesLoaded");
	}
	
	
	
}
