
package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;


public interface TextPart {
    
    public static final Color COLOR_DEFAULT_FOREGROUND = new Color(Display.getCurrent(),new RGB(0,0,0));
    public static final Color COLOR_DEFAULT_BACKGROUND = new Color(Display.getCurrent(), new RGB(236, 233, 216));

    public String getText();
    
    public void setOffset(int begin);
    
    public StyleRange getStyleRange();
    
    public String getToolTipText();
    
    public int getLength();
    
    public int getOffset();
    
    public void clicked();
    
    public void deregister();
    
}
