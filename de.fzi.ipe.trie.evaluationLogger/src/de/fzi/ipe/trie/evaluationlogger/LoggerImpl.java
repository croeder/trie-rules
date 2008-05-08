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
	
	private String clean (String text) {
		String toReturn = text.replaceAll(",", ";");
		toReturn = toReturn.replaceAll("\n", " ");
		return toReturn;
	}
	
	public void log(String... data) {
		String[] toLog = new String[data.length+1];
		toLog[0] = ""+(System.currentTimeMillis()-startingTime)/1000;
		for (int i=1;i<toLog.length;i++) toLog[i] = clean(data[i-1]);
		for (LoggerListener l: listeners) l.event(this, toLog);

		for (String s:toLog) System.out.print(s+",");
		System.out.println();
	}

	public void addListener(LoggerListener lis) {
		listeners.add(lis);
		
	}

	public void removeListener(LoggerListener lis) {
		listeners.remove(lis);
	}

}
