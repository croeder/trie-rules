package de.fzi.ipe.trie.debugger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class DebuggerPlugin extends AbstractUIPlugin implements IStartup{

	public static final String PLUGIN_ID = "de.fzi.ipe.trie.debugger.DebuggerPlugin";
	
	public static final String IMAGE_DESELECT = "clear.gif";
	public static final String IMAGE_FORWARD = "step_current.gif";
	public static final String IMAGE_FORWARD_D = "forward_nav_d.gif";
	public static final String IMAGE_BACK = "backward_nav.gif";
	public static final String IMAGE_BACK_D = "backward_nav_d.gif";
	public static final String IMAGE_RULE = "rule.gif";
	public static final String IMAGE_RULE_GREEN = "rule_green.gif";
	public static final String IMAGE_RULE_BLACK = "rule_black.gif";
	public static final String IMAGE_AXIOM_GREEN = "axiom_green.gif";
	public static final String IMAGE_AXIOM_BLACK = "axiom_black.gif";
	public static final String IMAGE_REFRESH = "refresh.gif";
	public static final String IMAGE_DYNAMIC = "dyn.gif";
	public static final String IMAGE_FACT_BLACK = "fact_black.gif";
	public static final String IMAGE_FACT_GREEN = "fact_green.gif";
	public static final String IMAGE_REMOVE_GRAY = "remove_gray.gif";
	public static final String IMAGE_TO_QUERY = "find.gif";
	public static final String IMAGE_PROOFTREE = "prooftree.gif";
    public static final String IMAGE_INFO = "info.gif";
    public static final String IMAGE_JUMP_TO_RULE = "jumpToRule.gif";
    public static final String IMAGE_SNAIL_RED = "snail_red.gif";
    public static final String IMAGE_SNAIL = "snail.gif";

    public static final String IMAGE_PLUS = "plus.png";
    public static final String IMAGE_MINUS = "minus.png";
    public static final String IMAGE_PLUS_GREEN = "plus_green.png";
    public static final String IMAGE_REMOVE_GRAY_SMALL = "remove_gray.png";
    public static final String IMAGE_FOCUS_PURPLE = "focus_purple.png";
	

	private static DebuggerPlugin singleton;
	
	private static Map<String,Image> images = new HashMap<String,Image>();
	private static Map<String,ImageDescriptor> imageDescriptors = new HashMap<String,ImageDescriptor>();

	
	   public static void displayErrorMessage(Exception e) {
	    	Status status = new Status(IStatus.ERROR,PLUGIN_ID,1,e.getMessage(),e);
	    	displayErrorMessage(status);
	    }
	    
	    public static void displayErrorMessage(IStatus status) {
	    	String reason = status.getMessage();
	    	if (status.getException() != null) { 
	    		reason = reason + System.getProperty("line.separator")+status.getException().getMessage();
	    	}
	    	ErrorDialog.openError(Display.getCurrent().getActiveShell(), "Encountered Problem", reason, status);
	    }
	
	public static DebuggerPlugin getInstance() {
	    return singleton;
	}
	
	public static URL getFile(String path_s) {
	    Path path = new Path(path_s);
	    URL url = FileLocator.find(singleton.getBundle(), path, null);
	    return url;	    
	}
	
	public static Image getImage(String imagePath) {
	    Image toReturn = (Image) images.get(imagePath);
	    if (toReturn == null) {
	        toReturn = loadImage(imagePath).createImage();
	        images.put(imagePath, toReturn);
	    }
	    return toReturn;
	}
	
    public static ImageDescriptor loadImage(String relativePath) {
        ImageDescriptor toReturn = (ImageDescriptor) imageDescriptors.get(relativePath);
        if (toReturn == null) {
	        String iconPath = "icons/";
	        Path path = new Path(iconPath + relativePath);
		    URL url = FileLocator.find(singleton.getBundle(), path, null);
	        toReturn = ImageDescriptor.createFromURL(url);
	        imageDescriptors.put(relativePath, toReturn);
        }
        return toReturn;
    }
    
    public DebuggerPlugin() {
        super();
//        KnowledgeBus.getInstance().addReceiver(new DebugReceiver());
        singleton = this;
    }	

    /* (non-Javadoc)
     * @see org.eclipse.ui.IStartup#earlyStartup()
     */
    public void earlyStartup() {
        
    }

}
