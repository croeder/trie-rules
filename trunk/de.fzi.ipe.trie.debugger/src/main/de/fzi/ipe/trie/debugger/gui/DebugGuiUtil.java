package de.fzi.ipe.trie.debugger.gui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;

import de.fzi.ipe.trie.debugger.DebugView;

public class DebugGuiUtil {
	
	public static final Font FONT_HEADLINE = new Font(null, "Tahoma", 14, SWT.BOLD);
	public static final Color COLOR_LABEL_SELECTED = new Color(Display.getDefault(),0 , 0, 160);
	public static final Color COLOR_LABEL_SELECTED_FOREGROUND = new Color(Display.getDefault(), 255, 255, 255);
	public static final Color COLOR_LABEL_SELECTABLE = new Color(Display.getDefault(), 255, 255, 255);
	public static final Color COLOR_BUILTIN = new Color(Display.getDefault(), 0, 128, 255);
	public static final Color COLOR_CLAUSE_NOT_SATISFIED = new Color(Display.getDefault(), 255, 0, 0);
	public static final Color COLOR_CLAUSE_NO_BINDINGS = new Color(Display.getDefault(), 185, 30, 66);
	
	public static void runWithProgressBar(final Runnable runnable) {
		try {
			ProgressMonitorDialog dialog = new ProgressMonitorDialog(DebugView.getInstance().getShell());
			if (runnable instanceof IRunnableWithProgress) {
				dialog.run(false, false, (IRunnableWithProgress) runnable);
			} else {
				IRunnableWithProgress newRunnable = new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Working ...", 1);
						runnable.run();
						monitor.worked(1);
					}
				};
				dialog.run(false, false, newRunnable);
			}
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
