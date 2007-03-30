package de.fzi.ipe.trie.debugger.gui;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;

import de.fzi.ipe.trie.debugger.DebugView;

public class DebugGuiUtil {
	

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
