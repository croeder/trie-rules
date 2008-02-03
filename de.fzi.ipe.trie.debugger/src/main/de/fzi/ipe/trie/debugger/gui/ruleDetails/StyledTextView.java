package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import de.fzi.ipe.trie.debugger.gui.events.DebuggerEvent;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBus;
import de.fzi.ipe.trie.debugger.gui.events.DebuggerEventBusListener;
import de.fzi.ipe.trie.debugger.gui.events.RedrawEvent;
import de.fzi.ipe.trie.debugger.model.DebuggerAtom;




public class StyledTextView implements MouseMoveListener, MouseListener, DebuggerEventBusListener{

    private static final Cursor ARROW_CURSOR = new Cursor(Display.getCurrent(),SWT.CURSOR_ARROW);
    
    private StyledText styledText;
    private ClauseManipulationButtons buttons = new ClauseManipulationButtons();
    private OffsetIndex textParts = new OffsetIndex();
    private DebuggerEventBus eventBus;
    
    
    public StyledTextView(Composite parent,DebuggerEventBus eventBus) {
    	this.eventBus = eventBus;
    	eventBus.addListener(this);
		styledText = new StyledText(parent,SWT.READ_ONLY | SWT.WRAP); 	
		styledText.addPaintListener(buttons);
		styledText.addMouseMoveListener(this);
		styledText.addMouseListener(this);
		
		formatStyledText(parent, styledText);
    }
    

    private void formatStyledText(Composite parent, StyledText styledText) {
        styledText.setFont(new Font(null,"Tahoma", 12,SWT.BOLD));		
		styledText.setBackground(parent.getBackground());
		styledText.setCursor(ARROW_CURSOR);
		Caret caret = styledText.getCaret();
		caret.setVisible(false);
		styledText.setCaret(caret);
		styledText.pack();
    }
    
    public void reset() {
    	buttons.clearAll();
    	
    	textParts.clear();
    }
    
    public void updateText() {
		styledText.setText(textParts.getText());		
		styledText.setStyleRanges(textParts.getStyleRanges());
    }

    public Point getSize() {
		return styledText.computeSize(SWT.DEFAULT,SWT.DEFAULT);
    }
    
    public void addClause(TextPart part,DebuggerAtom atom) {
    	textParts.add(part);
    	styledText.setText(textParts.getText());
    	int endIndex = part.getOffset() + part.getLength();
    	Point locationAtOffset = styledText.getLocationAtOffset(endIndex);
    	if (atom != null) {
    		buttons.add(new AtomFocusButton(locationAtOffset,atom,eventBus));
    		Point locationSecondButton = new Point(locationAtOffset.x+14,locationAtOffset.y);
    		buttons.add(new AtomActivateButton(locationSecondButton,atom,eventBus));
    	}
    	textParts.add(new TextPartWhitespace(6));
    }
    
    public void addNewLine() {
    	textParts.add(new TextPartNewLine());
    }
    
    public void mouseMove(MouseEvent e) {
    	TextPart currentTextPart = getTextPartAtLocation(new Point(e.x,e.y));
		if (currentTextPart != null)  {
		    String newText = currentTextPart.getToolTipText();
		    String oldText = styledText.getToolTipText();
		    if ((oldText == null) || (newText == null) ||  (!newText.equals(oldText))) {
		        styledText.setToolTipText(newText);
		    }
		}
		else { 
		    String newText = "Move mouse over colored text to get more information";
		    if (!newText.equals(styledText.getToolTipText())) {
		        styledText.setToolTipText(newText);
		    }
		}
                
    }

    public void mouseDoubleClick(MouseEvent e) {
    	;
    }

    public void mouseDown(MouseEvent e) {
        ;
    }

    public void mouseUp(MouseEvent e) {
        select(new Point(e.x,e.y));                
    }
    

    private void select(Point point) {
    	if (!buttons.click(point)) {
    		TextPart currentTextPart = getTextPartAtLocation(point);
    		if (currentTextPart != null) currentTextPart.clicked();
    	}
    }
    
    private TextPart getTextPartAtLocation(Point point) {
        int offset = -1;
        try {
			offset = styledText.getOffsetAtLocation(point);
        } catch (IllegalArgumentException ile) {
            ; //clicked outside of text area.
        }
        return textParts.getTextPartAtOffset(offset);
    }


	public void eventNotification(DebuggerEvent event) {
		if (event == RedrawEvent.RULE_DETAILS) {
			System.out.println("Updated text"); //TODO
			updateText();
		}
		
	}

    
    
}
