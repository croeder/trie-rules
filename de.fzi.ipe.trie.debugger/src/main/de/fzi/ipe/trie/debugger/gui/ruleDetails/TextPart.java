
package de.fzi.ipe.trie.debugger.gui.ruleDetails;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;


public class TextPart {
    
    public static final Color COLOR_DEFAULT_FOREGROUND = new Color(Display.getCurrent(),new RGB(0,0,0));
    public static final Color COLOR_DEFAULT_BACKGROUND = new Color(Display.getCurrent(), new RGB(236, 233, 216));
    
    private String text,tooltip;
    private Color foreground;
    private Color background;
    private Set<SelectionListener> selectionListener = new HashSet<SelectionListener>();
    
    public TextPart(String text, Color foreground, Color background) {
        this.text = text;
        setForeground(foreground);
        setBackground(background);
    }
    
    public TextPart(String text) {
        this(text,null,null);
    }
    
    public String getText() { return text; }
    public Color getForeground() { return foreground; }
    public Color getBackground() { return background; }
    
    public void setForeground(Color foreground) {
        this.foreground = (foreground != null) ? foreground : COLOR_DEFAULT_FOREGROUND;        
    }
    
    public void setBackground(Color background) {
        this.background = (background != null) ? background : COLOR_DEFAULT_BACKGROUND;
    }
    
    public void addSelectionListener(SelectionListener listener) {
        selectionListener.add(listener);
    }

    public void removeSelectionListener(ISelectionListener listener) {
        selectionListener.remove(listener);
    }
    
    public void setToolTipText(String tooltip) {
        this.tooltip = tooltip;
    }
    
    public String getToolTipText() {
        return tooltip;
    }

    protected void notifyListeners() {
        Iterator listeners = selectionListener.iterator();
        while (listeners.hasNext()) {
            SelectionListener current = (SelectionListener) listeners.next();
            current.widgetSelected(null);
        }
    }

    public int getLength() {
        return text.length();
    }
    
}
