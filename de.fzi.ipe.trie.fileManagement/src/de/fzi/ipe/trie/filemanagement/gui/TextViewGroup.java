package de.fzi.ipe.trie.filemanagement.gui;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.hp.hpl.jena.shared.JenaException;

import de.fzi.ipe.trie.filemanagement.Activator;
import de.fzi.ipe.trie.filemanagement.SourceFiles;
import de.fzi.ipe.trie.filemanagement.model.DebuggerFile;

public class TextViewGroup implements ModifyListener, KeyListener {

	private Composite parent;
	private Text text;
	private DebuggerFile currentFile;
	private Button save,remove,add;
	private Label errorMessage;
	
	private boolean isModified =false;
	
	
	public TextViewGroup(Composite parent) {
		this.parent = parent;
		Group root = new Group(parent,SWT.NONE);
		root.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		root.setLayout(new GridLayout(1,false));

		createGroupAboveText(root);
		
		text = new Text (root, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		text.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		text.addModifyListener(this);
		text.addKeyListener(this);
	}

	private void createGroupAboveText(final Group root) {
		Composite buttonGroup = new Composite (root,SWT.NONE);
		GridData buttonGroupGD = new GridData(SWT.FILL, SWT.DEFAULT,true,false);
		buttonGroupGD.heightHint = 30;
		buttonGroup.setLayoutData(buttonGroupGD);
		buttonGroup.setLayout(new GridLayout(4,false));

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
		save.setEnabled(false);
		
		add = new Button(buttonGroup,SWT.PUSH);
		add.setText("Add");
		add.setToolTipText("Add a new file to the knowledge base");
		add.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(root.getShell());
				fileDialog.setFileName(getFileChooserPath());
				fileDialog.setFilterExtensions(new String[] {"*.rule","*.turtle","*.rdf","*.*"});
				String fullPath = fileDialog.open();
				if (fullPath != null) {
					try {
						DebuggerFile file = new DebuggerFile(new File(fullPath));
						SourceFiles.getInstance().addFile(file);
						saveFileChooserPath(fullPath);	
						setText(file);
					} catch (IOException e1) {
						showError("Could not read File!", "Could not read file "+e1.getMessage(),e1);
						e1.printStackTrace();					
					} catch (JenaException e1) {
						showError("Could not parse File!", "Could not parse file "+e1.getMessage(),e1);
						e1.printStackTrace();
					}
				}
			}});
		
		remove = new Button(buttonGroup, SWT.PUSH);
		remove.setText("Remove");
		remove.setToolTipText("Removes the current file from the knowledge base");
		remove.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				if (currentFile != null) {
					try {
						SourceFiles.getInstance().removeFile(currentFile);
						text.setText("");
						remove.setEnabled(false);
						save.setEnabled(false);
					} catch (IOException ioe) {
						//really an exception that can happen only under very strange circumstances.
						throw new RuntimeException(ioe); 
					}
				}
			}
		});
		remove.setEnabled(false);
		
		errorMessage = new Label(buttonGroup,SWT.NONE);
		GridData gd = new GridData(SWT.FILL,SWT.DEFAULT,true,false);
		errorMessage.setLayoutData(gd);
		errorMessage.setForeground(new Color(Display.getCurrent(),200,0,0));
		errorMessage.setText("");
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
		remove.setEnabled(true);
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
				errorMessage.setText(currentFile.compileTest());
				SourceFiles.getInstance().reload();
				isModified = false;
				save.setEnabled(false);
				
			} 
		} catch (IOException ioe)  {
			showError("Error Saving File", "Could not save file!", ioe);
		} catch (JenaException je) {
			showError("Error Saving File", je.getMessage(), je);			
		}
		
	}
	
	private void saveFileChooserPath(String path) {
		path = new File(path).getParentFile().getPath()+"/";
		IDialogSettings favoritesSettings = Activator.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings == null) {
			wizardSettings = favoritesSettings.addNewSection("fileChooser");
		}
		wizardSettings.put("path",path);
	}

	private String getFileChooserPath() {
		IDialogSettings favoritesSettings = Activator.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings != null) {
			return wizardSettings.get("path");
		}
		else return "";
	}

	private void showError(String title, String message, Exception exception) {
		Status status = new Status(IStatus.ERROR,"de.fzi.ipe.trie.debugger",-1,message,exception);
		ErrorDialog.openError(parent.getShell(), title, message, status);
	}
}
