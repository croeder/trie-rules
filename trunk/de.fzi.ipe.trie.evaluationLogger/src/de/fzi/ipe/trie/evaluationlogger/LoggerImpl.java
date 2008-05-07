package de.fzi.ipe.trie.evaluationlogger;

import java.util.HashSet;
import java.util.Set;

public class LoggerImpl implements Logger {

	private static Logger logger = new LoggerImpl();
	private long startingTime = System.currentTimeMillis();
	
	private Set<LoggerListener> listeners = new HashSet<LoggerListener>();
	
	public static Logger getLogger() {
		return logger;
	}
	
	public void log(String... data) {
		for (LoggerListener l: listeners) l.event(this, data);
		
	
		System.out.print((System.currentTimeMillis()-startingTime)/1000);
		System.out.print(",");
		for (String s:data) System.out.print(s+",");
		System.out.println();
	}

	public void addListener(LoggerListener lis) {
		listeners.add(lis);
		
	}

	public void removeListener(LoggerListener lis) {
		listeners.remove(lis);
	}

}
