package de.fzi.ipe.trie.evaluationlogger.views;


import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.fzi.ipe.trie.evaluationlogger.Logger;
import de.fzi.ipe.trie.evaluationlogger.LoggerImpl;
import de.fzi.ipe.trie.evaluationlogger.LoggerListener;



/**
 * Simple view have a look at the current log.
 * @author zach
 *
 */
public class LoggerDebugView extends ViewPart implements LoggerListener{
	private Text text;


	/**
	 * The constructor.
	 */
	public LoggerDebugView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		text = new Text(parent,SWT.MULTI);
		LoggerImpl.getLogger().addListener(this);
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		;
	}

	public void event(Logger logger, String... data) {
		StringBuilder builder = new StringBuilder(text.getText());
		builder.append("\n");
		for (String s: data) builder.append(s+", ");
		text.setText(builder.toString());
	}
}