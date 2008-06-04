package de.fzi.ipe.trie.rcp;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.filemanagement.FileView;
import de.fzi.ipe.trie.proceduraldebugger.ProceduralDebugView;
import de.fzi.trie.visualization.DependencyView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		IFolderLayout folder = layout.createFolder("FOLDER", IPageLayout.LEFT, 1.0f, editorArea);
		
		folder.addView(ProceduralDebugView.VIEW_ID);
		folder.addView(FileView.VIEW_ID);
		folder.addView(DebugView.VIEW_ID);
		folder.addView(DependencyView.VIEW_ID);
//		folder.addView("de.fzi.ipe.trie.evaluationlogger.views.LoggerDebugView");
	}

}
