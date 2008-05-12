package de.fzi.ipe.trie.evaluationlogger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class LoggerImpl implements Logger {

	private static Logger logger = new LoggerImpl();
	private DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private Set<LoggerListener> listeners = new HashSet<LoggerListener>();
	private String[] previousLoggedMessage = new String[0];
	
	public static Logger getLogger() {
		return logger;
	}
	
	private String clean (String text) {
		if (text != null) {
			String toReturn = text.replaceAll(",", ";");
			toReturn = toReturn.replaceAll("\n", " ");
			return toReturn;
		}
		else return null;
	}
	
	public void log(String... data) {
		String[] toLog = new String[data.length+1];
		toLog[0] = dateFormat.format(new Date());
		for (int i=1;i<toLog.length;i++) toLog[i] = clean(data[i-1]);
		if (!equals(previousLoggedMessage,toLog)) { //preventing the same message to be send multiple times. 
			previousLoggedMessage = toLog.clone();
			for (LoggerListener l: listeners) l.event(this, toLog);
		}
	}
	
	private static boolean equals (String[] one, String[] two) {
		if (one.length != two.length) return false;
		else {
			for (int i=0;i<one.length;i++) {
				if (!one[i].equals(two[i])) return false;
			}
			return true;
		}
	}

	public void addListener(LoggerListener lis) {
		listeners.add(lis);
		
	}

	public void removeListener(LoggerListener lis) {
		listeners.remove(lis);
	}

}
