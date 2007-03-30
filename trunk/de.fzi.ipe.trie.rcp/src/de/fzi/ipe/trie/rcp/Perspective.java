package de.fzi.ipe.trie.rcp;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import de.fzi.ipe.trie.debugger.DebugView;
import de.fzi.ipe.trie.debugger.gui.files.FileChooserView;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);
		layout.setFixed(true);
		
		IFolderLayout folder = layout.createFolder("FOLDER", IPageLayout.LEFT, 1.0f, editorArea);
		
		folder.addView(FileChooserView.VIEW_ID);
		folder.addView(DebugView.VIEW_ID);		
	}

}
