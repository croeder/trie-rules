package de.fzi.trie.visualization;

import de.fzi.ipe.trie.evaluationlogger.Logable;
import de.fzi.ipe.trie.evaluationlogger.Logger;

public class DebugLogger implements Logable{

	private static Logger logger;
	
	public void setLogger(Logger logger) {
		DebugLogger.setLoggerInstance(logger);
	}

	private static void setLoggerInstance(Logger logger) {
		DebugLogger.logger = logger;
	}
	
	public static void log(String... data) {
		String[] toLog = new String[data.length+1];
		toLog[0] = "Visualization";
		for (int i=1;i<toLog.length;i++) toLog[i] = data[i-1];
		logger.log(toLog);
	}
	
}
