package de.fzi.ipe.trie.filemanagement.gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import de.fzi.ipe.trie.filemanagement.SourceFiles;
import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;
import de.fzi.ipe.trie.filemanagement.model.SourceFileListener;

/**
 * Class that creates and maintains a group that displays a click able List of the current files in the FileView
 */
public class ButtonGroup implements SourceFileListener{
	
	private Group root;
	private List<FileButton> buttons = new ArrayList<FileButton>();
	private TextViewGroup textView;

	public ButtonGroup(Composite parent) {
		root = new Group(parent,SWT.NONE);
		GridData buttonsGroupGD = new GridData(SWT.DEFAULT,SWT.FILL,false,true);
		buttonsGroupGD.widthHint= 150;
		root.setLayoutData(buttonsGroupGD);
		root.setLayout(new RowLayout(SWT.VERTICAL));
		
		createButtons();
		
		SourceFiles.getInstance().addListener(this);
	}

	public void setTextViewGroup(TextViewGroup textView) {
		this.textView = textView;
	}
	
	private void createButtons() {
		for (FileButton b: buttons) {
			b.dispose();
		}
		buttons.clear();
		SourceFiles sf = SourceFiles.getInstance();
		for (DebuggerFile f: sf.getRuleFiles()) {
			buttons.add(new FileButton(root,f));
		}
		for (DebuggerFile f: sf.getRDFFiles()) {
			buttons.add(new FileButton(root,f));
		}
		root.layout(true);
	}
	

	
	private class FileButton implements Comparable<FileButton>, SelectionListener{

		DebuggerFile file;
		Button button;
		
		public FileButton(Composite parent, DebuggerFile file) {
			button = new Button(parent, SWT.TOGGLE);
			this.file = file;
			button.setText(file.getName());
		
			RowData rd = new RowData();
			rd.width = 140;
			button.setLayoutData(rd);
			
			button.addSelectionListener(this);
		}

		public int compareTo(FileButton b2) {
			return file.getAbsolutePath().compareTo(b2.file.getAbsolutePath());
		}

		public boolean equals (FileButton b2) {
			return file.getAbsolutePath().equals(b2.file.getAbsolutePath());
		}
		
		public void dispose() {
			button.dispose();
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			;
		}

		public void widgetSelected(SelectionEvent e) {
			if (button.getSelection()) {
				//set all other buttons to 'not selected'
				for (FileButton f: buttons) {
					if (!f.equals(this)) f.button.setSelection(false);
				}
				textView.setText(file);
			}
			else {
			}
			
		}
		
		
	}



	public void filesChanged() {
		createButtons();
	}

	public void loaded() {
		;
	}
	
}
