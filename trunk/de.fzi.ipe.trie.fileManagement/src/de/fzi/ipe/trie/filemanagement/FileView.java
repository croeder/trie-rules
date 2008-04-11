package de.fzi.ipe.trie.filemanagement;


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class FileView extends ViewPart {

	public static final String VIEW_ID = "de.fzi.ipe.trie.debugger.FileView";
	
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite root = new Composite(parent,SWT.NONE);
		root.setLayout(new GridLayout(2,false));
		
		createButtonsGroup(root);
		createTextGroup(root);
		
		parent.layout(true);
	}

	private void createTextGroup(Composite root) {
		Group textGroup = new Group(root,SWT.NONE);
		textGroup.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		textGroup.setLayout(new FillLayout());
		
		Text text = new Text (textGroup, SWT.NONE);
		text.setText("Hallo!");
	}

	private void createButtonsGroup(Composite root) {
		Group buttonsGroup = new Group(root,SWT.NONE);
		GridData buttonsGroupGD = new GridData(SWT.DEFAULT,SWT.FILL,false,true);
		buttonsGroupGD.widthHint= 150;
		buttonsGroup.setLayoutData(buttonsGroupGD);
		buttonsGroup.setLayout(new RowLayout());
		
		Button button1 = new Button (buttonsGroup, SWT.PUSH);
		button1.setText ("button1");
		Button button2 = new Button (buttonsGroup, SWT.PUSH);
		button2.setText ("button2");
		Button button3 = new Button (buttonsGroup, SWT.PUSH);
		button3.setText ("button3");
	}

	@Override
	public void setFocus() {
		;
	}

}
