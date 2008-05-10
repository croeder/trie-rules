package de.fzi.ipe.trie.evaluationlogger.views;


import java.util.Observable;
import java.util.Observer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import de.fzi.ipe.trie.evaluationlogger.FileLogger;
import de.fzi.ipe.trie.evaluationlogger.Logger;
import de.fzi.ipe.trie.evaluationlogger.LoggerImpl;
import de.fzi.ipe.trie.evaluationlogger.LoggerListener;



/**
 * Simple view have a look at the current log.
 * @author zach
 *
 */
public class LoggerDebugView extends ViewPart implements LoggerListener{
	
	private Composite root;
	
	private Label evalFilesFolder;
	private Label evalFileName;
	
	
	private Text text;


	/**
	 * The constructor.
	 */
	public LoggerDebugView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		root = new Composite(parent, SWT.NONE);
		root.setLayout(new GridLayout(2,false));
		root.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL,GridData.VERTICAL_ALIGN_FILL,true,true));
		
		createFileLocationElements();
		createFileNameElements();

		text = new Text(root,SWT.MULTI| SWT.WRAP | SWT.H_SCROLL);
		GridData textGridData = new GridData(SWT.FILL,SWT.FILL,true,true);
		textGridData.horizontalSpan = 2;
		text.setLayoutData(textGridData);
		
		LoggerImpl.getLogger().addListener(this);
		
		root.layout(true);
	}

	private void createFileNameElements() {
		GridData gd2 = new GridData(SWT.FILL,GridData.VERTICAL_ALIGN_CENTER,true,false);
		Button fileNameButton = new Button(root,SWT.PUSH);
		fileNameButton.setText("New File");
		fileNameButton.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				FileLogger.getInstance().newFile();
				text.setText("");
			}});
		
		evalFileName = new Label(root,SWT.NONE);
		String fileName = FileLogger.getInstance().getFileName();
		if (fileName != null) evalFileName.setText(fileName);
		else evalFileName.setText("Currently no file");
		evalFileName.setLayoutData(gd2);

		FileLogger.getInstance().addObserver(new Observer() {
			public void update(Observable o, Object arg) {
				FileLogger fileLogger = (FileLogger) o;
				evalFileName.setText(fileLogger.getFileName());
			}});
		
	}

	private void createFileLocationElements() {
		Button selectFolder = new Button(root,SWT.PUSH);
		selectFolder.setText("Select Folder");
		GridData buttonGridData = new GridData(150,-1);
		selectFolder.setLayoutData(buttonGridData);
		selectFolder.setToolTipText("Select the location where the log files are stored");
		selectFolder.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
				;
			}

			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog fileDialog = new DirectoryDialog(root.getShell());
				String fullPath = fileDialog.open();
				if (fullPath != null) {
					FileLogger.getInstance().setFilePath(fullPath);
				}
			}
			
		});
		
		GridData gd = new GridData(SWT.FILL,GridData.VERTICAL_ALIGN_CENTER,true,false);
		evalFilesFolder = new Label(root,SWT.NONE);
		if (FileLogger.getInstance().getFilePath() != null) {
			evalFilesFolder.setText(FileLogger.getInstance().getFilePath());
		}
		else evalFilesFolder.setText("No Folder Selected");
		evalFilesFolder.setLayoutData(gd);

		FileLogger.getInstance().addObserver(new Observer() {
			public void update(Observable obs, Object obj) {
				FileLogger l = (FileLogger) obs;
				evalFilesFolder.setText(l.getFilePath());
			}});
		
	}


	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		;
	}

	public void event(Logger logger, String... data) {
		StringBuilder builder = new StringBuilder();
		for (String s: data) builder.append(s+", ");
		builder.append(Text.DELIMITER);
		builder.append(text.getText());
		text.setText(builder.toString());
		
		
	}
}