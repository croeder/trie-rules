package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;




public class StyledTextView {

    private static final Cursor ARROW_CURSOR = new Cursor(Display.getCurrent(),SWT.CURSOR_ARROW);
    
    private List<TextPart> textParts = new ArrayList<TextPart>();
    private StringBuffer text = new StringBuffer();
    private int[] offsetIndex;
    private StyledText styledText;
    
    public StyledTextView(Composite parent) {
		styledText = new StyledText(parent,SWT.READ_ONLY | SWT.WRAP); 
		
		styledText.addPaintListener(new PaintListener() {
		      public void paintControl(PaintEvent e) {
		          // Do some drawing
		          Rectangle rect = ((Canvas) e.widget).getBounds();
		          e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_RED));
		          e.gc.drawFocus(5, 5, rect.width - 10, rect.height - 10);
		          e.gc.drawText("You can draw text directly on a canvas", 10, 10);
		        }
		      });
		
		formatStyledText(parent, styledText);
    }
    
    public void reset() {
    	text = new StringBuffer();
    	textParts = new ArrayList<TextPart>();
    }
    
    public void updateTextText() {
		styledText.setText(text.toString());		

		addStyleRanges(styledText);
		prepareOffsetIndex();
		addMouseListener(styledText);
		addMouseMoveListener();
//		styledText.setSize(new Point(500,100));
    }

    public Point getSize() {
		return styledText.computeSize(SWT.DEFAULT,SWT.DEFAULT);
    }
    
    public void add(TextPart part) {
        textParts.add(part);
        text.append(part.getText());
        text.append(" ");
    }
    
    public void addNewLine() {
        if (text.length() > 0) text.setCharAt(text.length()-1, '\n');
    }
    
    private void addMouseMoveListener() {        
        styledText.addMouseMoveListener(new MouseMoveListener() {

            public void mouseMove(MouseEvent e) {
                setTooltip(new Point(e.x,e.y));
            }});
    }

    private void addMouseListener(StyledText styledText) {
        styledText.addMouseListener(new MouseListener() {

            public void mouseDoubleClick(MouseEvent e) {
                select(new Point(e.x, e.y));            
            }

            public void mouseDown(MouseEvent e) {
                ;
            }

            public void mouseUp(MouseEvent e) {
                select(new Point(e.x,e.y));                
            }});
    }

    private void select(Point point) {
        TextPart currentTextPart = getTextPartAtLocation(point);
        if (currentTextPart != null) currentTextPart.notifyListeners();
    }
    
    private void setTooltip(Point point) {
        TextPart currentTextPart = getTextPartAtLocation(point);
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

    private TextPart getTextPartAtLocation(Point point) {
        int offset = -1;
        try {
			offset = styledText.getOffsetAtLocation(point);
        } catch (IllegalArgumentException ile) {
            ; //clicked outside of text area.
        }
        TextPart currentTextPart = null;
        if (offset > -1) {
            int index = offsetIndex[offset];
            if (index > -1) {
                currentTextPart = (TextPart) textParts.get(index);
            }
        }
        return currentTextPart;
    }

    private void prepareOffsetIndex() {
        offsetIndex = new int[text.length()];
		Arrays.fill(offsetIndex, -1);
        int currentOffset = 0;
		for (int i=0;i<textParts.size();i++) {
		    TextPart currentTextPart = (TextPart) textParts.get(i);
		    Arrays.fill(offsetIndex, currentOffset, currentOffset+currentTextPart.getLength(), i);
		    currentOffset += currentTextPart.getLength();
		    currentOffset++;
		}
    }

    private void addStyleRanges(StyledText styledText) {
        int currentOffset = 0;
		for (int i=0;i<textParts.size();i++) {
		    TextPart currentTextPart  = (TextPart) textParts.get(i);
		    StyleRange currentStyleRange = new StyleRange();
		    currentStyleRange.start = currentOffset;
		    currentStyleRange.length = currentTextPart.getLength();
		    currentStyleRange.foreground = currentTextPart.getForeground();
		    currentStyleRange.background = currentTextPart.getBackground();
		    styledText.setStyleRange(currentStyleRange);
		    currentOffset += currentTextPart.getLength();
		    currentOffset ++; //to account for spaces that we insert in "addTextPart"
		}
    }

    private void formatStyledText(Composite parent, StyledText styledText) {
        styledText.setFont(new Font(null,"Tahoma", 12,SWT.BOLD));		
		styledText.setBackground(parent.getBackground());
		styledText.setCursor(ARROW_CURSOR);
		Caret caret = styledText.getCaret();
		caret.setVisible(false);
		styledText.setCaret(caret);
//		styledText.setSize(new Point(500,100));
		styledText.pack();
    }
    
    
}
