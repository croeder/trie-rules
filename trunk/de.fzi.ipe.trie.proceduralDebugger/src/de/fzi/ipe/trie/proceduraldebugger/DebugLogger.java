package de.fzi.ipe.trie.proceduraldebugger;

import de.fzi.ipe.trie.Rule;
import de.fzi.ipe.trie.evaluationlogger.Logable;
import de.fzi.ipe.trie.evaluationlogger.Logger;
import de.fzi.ipe.trie.inference.Suspender.Action;
import de.fzi.ipe.trie.inference.executionTree.ExecutionTreeGoal;
import de.fzi.ipe.trie.proceduraldebugger.model.ReasoningAccess;
import de.fzi.ipe.trie.proceduraldebugger.model.SuspendListener;

public class DebugLogger implements Logable, SuspendListener{
	
	private static Logger logger;
	
	public void setLogger(Logger logger) {
		DebugLogger.setLoggerInstance(logger);
		ReasoningAccess.getSuspender().addListener(this);
	}

	private static void setLoggerInstance(Logger logger) {
		DebugLogger.logger = logger;
	}
	
	public static void log(String... data) {
		String[] toLog = new String[data.length+1];
		toLog[0] = "ProceduralDebugger";
		for (int i=1;i<toLog.length;i++) toLog[i] = data[i-1];
		logger.log(toLog);
	}

	public void suspending(Action a, ExecutionTreeGoal goal, Rule r) {
		if (r!= null) logger.log(""+a,r.getName(),""+goal);
		else logger.log(""+a,null,""+goal);
	}

	public void waking() {
		;
	}
	
}
