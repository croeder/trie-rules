package de.fzi.ipe.trie.debugger;

import de.fzi.ipe.trie.evaluationlogger.Logable;
import de.fzi.ipe.trie.evaluationlogger.Logger;

public class DebugLogger implements Logable{

	private static Logger logger;
	
	public void setLogger(Logger logger) {
		DebugLogger.logger = logger;
	}

	public static void log(String... data) {
		logger.log(data);
	}

}
