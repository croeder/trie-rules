package de.fzi.ipe.trie.evaluationlogger;

public interface Logger {

	public void log(String... data);
	public void addListener(LoggerListener lis);
	public void removeListener(LoggerListener lis);

	
}
