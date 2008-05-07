package de.fzi.ipe.trie.debugger.gui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

/**
 * Class that realizes tooltips for Tables. 
 */
public class TableTooltipManager extends ShellAdapter implements MouseTrackListener, MouseMoveListener{

	private ILabelProvider tooltipProvider;
	private Table table;
	
	private Shell tip;
	private Label label;
	
	public TableTooltipManager(Table table, ILabelProvider tooltipProvider) {
		this.table = table;
		table.addMouseMoveListener(this);
		table.addMouseTrackListener(this);
		this.tooltipProvider = tooltipProvider;
		addShellListener();
	}
	
	public void mouseHover(MouseEvent e) {
		TableItem item = table.getItem(new Point(e.x,e.y));
        if (item != null) {
        	disposeTip();
        	String tooltipText = tooltipProvider.getText(item.getData());
        	if (tooltipText != null) {
        		createTooltip(tooltipText);
        		Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        		Point pt = table.toDisplay(e.x, e.y);
        		tip.setBounds(pt.x+15, pt.y+20, size.x, size.y);
        		tip.setVisible(true);
        	}
        }
	}

	private void createTooltip(String tooltipText) {
		tip = new Shell(table.getParent().getShell(), SWT.ON_TOP | SWT.TOOL);
		tip.setLayout(new FillLayout());
		label = new Label(tip, SWT.NONE);
		label.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
		label.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
		label.setText(tooltipText);
	}

	private void addShellListener() {
		Shell activeShell = Display.getCurrent().getActiveShell();
		if (activeShell != null) activeShell.addShellListener(this);
	}

	private void disposeTip() {
		if (tip != null && !tip.isDisposed()) tip.dispose();
	}
	
	public void mouseMove(MouseEvent e) {
		disposeTip();
	}

	public void shellClosed(ShellEvent e) {
		disposeTip();
	}

	public void shellDeactivated(ShellEvent e) {
		disposeTip();
	}

	public void shellIconified(ShellEvent e) {
		disposeTip();
	}

	public void mouseEnter(MouseEvent e) {
		;
	}

	public void mouseExit(MouseEvent e) {
		disposeTip();
	}

	
	
}
