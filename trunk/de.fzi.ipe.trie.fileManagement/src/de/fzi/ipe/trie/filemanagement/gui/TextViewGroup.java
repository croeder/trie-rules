package de.fzi.ipe.trie.filemanagement.gui;

import java.io.IOException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class TextViewGroup implements ModifyListener, KeyListener {

	private Text text;
	private DebuggerFile currentFile;
	private Button save;
	private Label errorMessage;
	
	private boolean isModified =false;
	
	
	public TextViewGroup(Composite parent) {
		Group root = new Group(parent,SWT.NONE);
		root.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		root.setLayout(new GridLayout(1,false));

		createButtonGroup(root);
		
		text = new Text (root, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		text.addModifyListener(this);
		text.addKeyListener(this);
	}

	private void createButtonGroup(Group root) {
		Composite buttonGroup = new Composite (root,SWT.NONE);
		GridData buttonGroupGD = new GridData(SWT.FILL, SWT.DEFAULT,true,false);
		buttonGroupGD.heightHint = 30;
		buttonGroup.setLayoutData(buttonGroupGD);
		buttonGroup.setLayout(new GridLayout(2,false));

		save = new Button(buttonGroup,SWT.PUSH);
		save.setText("Save");
		save.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				saveText();
			}
			
		});
		
		errorMessage = new Label(buttonGroup,SWT.NONE);
		GridData gd = new GridData(SWT.FILL,SWT.DEFAULT,true,false);
		errorMessage.setLayoutData(gd);
		errorMessage.setForeground(new Color(Display.getCurrent(),200,0,0));
		errorMessage.setText("Ich bin neu hier");
	}
	
	public void setText (DebuggerFile file) {
		currentFile = file;
		try {
			text.setText(file.getContent());
		} catch (IOException e1) {
			text.setText("Problem Reading File");
		}
		isModified = false;
		save.setEnabled(false);
		errorMessage.setText("");
	}

	public void modifyText(ModifyEvent e) {
		isModified = true;
		save.setEnabled(true);
	}

	public void keyPressed(KeyEvent e) {
		;
	}

	public void keyReleased(KeyEvent e) {
		//save on ctrl-s
		if (isModified && e.keyCode == 115 && ((e.stateMask & SWT.CTRL) != 0)) saveText();
	}

	private void saveText() {
		try {
			if (currentFile != null) {
				currentFile.setContent(text.getText());
				isModified = false;
				save.setEnabled(false);
				
				errorMessage.setText(currentFile.compileTest());
			} 
		} catch (IOException ioe)  {
			//TODO exception message
		}
	}
	
	
}
