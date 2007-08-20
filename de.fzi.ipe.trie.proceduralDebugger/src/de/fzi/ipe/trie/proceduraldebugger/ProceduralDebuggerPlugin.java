package de.fzi.ipe.trie.proceduraldebugger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ProceduralDebuggerPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "de.fzi.ipe.trie.proceduralDebugger";

	// The shared instance
	private static ProceduralDebuggerPlugin plugin;

	/**
	 * The constructor
	 */
	public ProceduralDebuggerPlugin() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * @return the shared instance
	 */
	public static ProceduralDebuggerPlugin getDefault() {
		return plugin;
	}

	private static Map<String, Image> images = new HashMap<String, Image>();

	private static Map<String, ImageDescriptor> imageDescriptors = new HashMap<String, ImageDescriptor>();

	public static void displayErrorMessage(Exception e) {
		Status status = new Status(IStatus.ERROR, PLUGIN_ID, 1, e.getMessage(),
				e);
		displayErrorMessage(status);
	}

	public static void displayErrorMessage(IStatus status) {
		String reason = status.getMessage();
		if (status.getException() != null) {
			reason = reason + System.getProperty("line.separator")
					+ status.getException().getMessage();
		}
		ErrorDialog.openError(Display.getCurrent().getActiveShell(),
				"Encountered Problem", reason, status);
	}

	public static URL getFile(String path_s) {
		Path path = new Path(path_s);
		URL url = plugin.find(path);
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
		ImageDescriptor toReturn = (ImageDescriptor) imageDescriptors
				.get(relativePath);
		if (toReturn == null) {
			String iconPath = "icons/";
			Path path = new Path(iconPath + relativePath);
			URL url = plugin.find(path);
			toReturn = ImageDescriptor.createFromURL(url);
			imageDescriptors.put(relativePath, toReturn);
		}
		return toReturn;
	}

}
