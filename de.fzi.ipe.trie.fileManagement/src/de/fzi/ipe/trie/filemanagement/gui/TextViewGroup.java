package de.fzi.ipe.trie.filemanagement.gui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class TextViewGroup implements ModifyListener {

	private Text text;
	private DebuggerFile currentFile;
	private Button save;
	
	
	public TextViewGroup(Composite parent) {
		Group root = new Group(parent,SWT.NONE);
		root.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		root.setLayout(new GridLayout(1,false));

		createButtonGroup(root);
		
		text = new Text (root, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		text.addModifyListener(this);
	}

	private void createButtonGroup(Group root) {
		Composite buttonGroup = new Composite (root,SWT.NONE);
		GridData buttonGroupGD = new GridData(SWT.FILL, SWT.DEFAULT,true,false);
		buttonGroupGD.heightHint = 30;
		buttonGroup.setLayoutData(buttonGroupGD);
		buttonGroup.setLayout(new RowLayout(SWT.HORIZONTAL));

		save = new Button(buttonGroup,SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				try {
					if (currentFile != null) {
						currentFile.setContent(text.getText());
						save.setEnabled(false);
					} 
				} catch (IOException ioe)  {
					//TODO exception message
				}
			}
			
		});
	}
	
	public void setText (DebuggerFile file) {
		currentFile = file;
		try {
			text.setText(file.getContent());
		} catch (IOException e1) {
			text.setText("Problem Reading File");
		}
		save.setEnabled(false);
	}

	public void modifyText(ModifyEvent e) {
		save.setEnabled(true);
	}
	
	
}
