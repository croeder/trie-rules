package de.fzi.ipe.trie.evaluationlogger;

/**
 * Implemented by plugins that extend this evaluationLoggers extension point to receive the logger object.
 * @author zach
 */
public interface Logable {

	public void setLogger(Logger logger);
	
}
