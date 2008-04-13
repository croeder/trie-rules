package de.fzi.ipe.trie.filemanagement;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.fzi.ipe.trie.filemanagement.gui.ButtonGroup;
import de.fzi.ipe.trie.filemanagement.gui.TextViewGroup;

public class FileView extends ViewPart {

	public static final String VIEW_ID = "de.fzi.ipe.trie.debugger.FileView";
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite root = new Composite(parent,SWT.NONE);
		root.setLayout(new GridLayout(2,false));
		
		ButtonGroup buttonGroup = new ButtonGroup(root);
		TextViewGroup textView = new TextViewGroup(root);
		buttonGroup.setTextViewGroup(textView);
		
		parent.layout(true);
	}


	@Override
	public void setFocus() {
		;
	}

}
