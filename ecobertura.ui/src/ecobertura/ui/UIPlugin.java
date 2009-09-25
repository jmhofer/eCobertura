package ecobertura.ui;


import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ecobertura.core.log.Logger;
import ecobertura.ui.editors.EditorTracker;


/**
 * The activator class controls the plug-in life cycle
 */
public class UIPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "ecobertura.ui";

	private static UIPlugin plugin;
	
	private EditorTracker editorTracker; 

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		UIPlugin.plugin = this;
		Logger.logFor(getLog());
		Logger.info("Cobertura plugin started.");
		editorTracker = EditorTracker.trackEditorsOf(getWorkbench());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		UIPlugin.plugin = null;
		editorTracker.dispose();
		Logger.info("Cobertura plugin stopped.");
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static UIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
