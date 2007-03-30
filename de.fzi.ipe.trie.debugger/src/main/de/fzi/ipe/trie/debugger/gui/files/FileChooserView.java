package de.fzi.ipe.trie.debugger.gui.files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import com.hp.hpl.jena.shared.JenaException;

import de.fzi.ipe.trie.debugger.DebuggerPlugin;

public class FileChooserView extends ViewPart {

	public static final String VIEW_ID = "de.fzi.ipe.trie.debugger.FileChooserView";
	private Composite parent;
	
	
	public FileChooserView() {
	}

	@Override
	public void createPartControl(final Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));

		//RDF File
		createRDFFileFields(parent);
		createRuleFileFields(parent);
		
	}

	private void createRuleFileFields(final Composite parent) {
		this.parent = parent;
		Group rdfFilesGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		rdfFilesGroup.setLayout(layout);

		Label rdfFilesLabel = new Label(rdfFilesGroup, SWT.NONE);
		rdfFilesLabel.setText("Rule Files");
		
		
		Composite listHolder = new Composite(rdfFilesGroup,SWT.NONE);
		listHolder.setLayout(new FillLayout());
		GridData listHolderGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		listHolderGD.heightHint = 200;
		listHolder.setLayoutData(listHolderGD);
		final ListViewer ruleFileList = new ListViewer(listHolder,SWT.NONE);
		ruleFileList.setContentProvider(new FileSetContentProvider());
		ruleFileList.setLabelProvider(new FileLabelProvider());

		Composite buttonHolder = new Composite(rdfFilesGroup, SWT.NONE);
		buttonHolder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		buttonHolder.setLayout(new RowLayout());
		
		Button addButton = new Button(buttonHolder, SWT.PUSH);
		addButton.setText("Add");
		addButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(parent.getShell());
				fileDialog.setFileName(getFileChooserPath());
				fileDialog.setFilterExtensions(new String[] {"*.rule","*.*"});
				String fullPath = fileDialog.open();
				if (fullPath != null) {
					try {
						File file = new File(fullPath);
						SourceFiles.getInstance().addRuleFile(file);
						ruleFileList.setInput(SourceFiles.getInstance().getRuleFiles());
						saveFileChooserPath(fullPath);						
					} catch (IOException e1) {
						showError("Could not read File!", "Could not read file "+e1.getMessage(),e1);
						e1.printStackTrace();					
					} catch (JenaException e1) {
						showError("Could not parse File!", "Could not parse file "+e1.getMessage(),e1);
						e1.printStackTrace();
					}
				}
				
			}});
		
		Button removeButton = new Button(buttonHolder,SWT.PUSH);
		removeButton.setText("Remove");		
		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) ruleFileList.getSelection();
				if (!selection.isEmpty()) {
					File toRemove = (File) selection.getFirstElement();
					SourceFiles.getInstance().removeRuleFile(toRemove);
					ruleFileList.setInput(SourceFiles.getInstance().getRuleFiles());
				}
				
			}});
	}

	
	private void saveFileChooserPath(String path) {
		path = new File(path).getParentFile().getPath()+"/";
		IDialogSettings favoritesSettings = DebuggerPlugin.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings == null) {
			wizardSettings = favoritesSettings.addNewSection("fileChooser");
		}
		wizardSettings.put("path",path);
	}

	private String getFileChooserPath() {
		IDialogSettings favoritesSettings = DebuggerPlugin.getInstance().getDialogSettings();
		IDialogSettings wizardSettings = favoritesSettings.getSection("fileChooser");
		if (wizardSettings != null) {
			return wizardSettings.get("path");
		}
		else return "";
	}
	
	private void createRDFFileFields(final Composite parent) {
		Group rdfFilesGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		rdfFilesGroup.setLayout(layout);

		Label rdfFilesLabel = new Label(rdfFilesGroup, SWT.NONE);
		rdfFilesLabel.setText("RDF Files");
		
		Composite listHolder = new Composite(rdfFilesGroup,SWT.NONE);
		listHolder.setLayout(new FillLayout());
		GridData listHolderGD = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
		listHolderGD.heightHint = 200;
		listHolder.setLayoutData(listHolderGD);
		final ListViewer rdfFileList = new ListViewer(listHolder,SWT.NONE);
		rdfFileList.setContentProvider(new FileSetContentProvider());
		rdfFileList.setLabelProvider(new FileLabelProvider());

		Composite buttonHolder = new Composite(rdfFilesGroup, SWT.NONE);
		buttonHolder.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
		buttonHolder.setLayout(new RowLayout());
		
		Button addButton = new Button(buttonHolder, SWT.PUSH);
		addButton.setText("Add");
		addButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(parent.getShell());
				fileDialog.setFileName(getFileChooserPath());
				fileDialog.setFilterExtensions(new String[] {"*.rdf","*.turtle","*.*"});
				String fullPath = fileDialog.open();
				if (fullPath != null) {
					File file = new File(fullPath);
					try {
						SourceFiles.getInstance().addRDFFile(file);
						rdfFileList.setInput(SourceFiles.getInstance().getRDFFiles());
						saveFileChooserPath(fullPath);
					} catch (FileNotFoundException e1) {
						showError("Could not find File!", "Could not find file "+e1.getMessage(),e1);
						e1.printStackTrace();					
					} catch (JenaException e1) {
						showError("Could not parse File!", "Could not parse file "+e1.getMessage(),e1);
						e1.printStackTrace();
					}
				}
				
			}});
		
		Button removeButton = new Button(buttonHolder,SWT.PUSH);
		removeButton.setText("Remove");		
		removeButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) rdfFileList.getSelection();
				if (!selection.isEmpty()) {
					File toRemove = (File) selection.getFirstElement();
					SourceFiles.getInstance().removeRDFFile(toRemove);
					rdfFileList.setInput(SourceFiles.getInstance().getRDFFiles());
				}
				
			}});
	}

	@Override
	public void setFocus() {
		;
	}

	private void showError(String title, String message, Exception exception) {
		Status status = new Status(IStatus.ERROR,"de.fzi.ipe.trie.debugger",-1,message,exception);
		ErrorDialog.openError(parent.getShell(), title, message, status);

	}
	
	
	private class FileLabelProvider extends LabelProvider {

		public String getText(Object element) {
			if (element instanceof File) return ((File)element).getName();
			else return super.getText(element);
		}
				
	}
	
	private class FileSetContentProvider implements IStructuredContentProvider{

		private File[] files;
		
		public void dispose() {}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			if (newInput instanceof Set) {
				files = ((Set<File>)newInput).toArray(new File[0]);
			}
			else files = new File[0];
		}

		public Object[] getElements(Object inputElement) {
			return files;
		}
		
	}
	
	

}
