package ecobertura.ui;

import java.util.logging.Logger;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import ecobertura.core.log.EclipseLogger;
import ecobertura.ui.core.CoverageResultsUIListener;
import ecobertura.ui.editors.EditorsAnnotator;

/**
 * Controls the plug-in life cycle of the eCobertura UI.
 */
public class UIPlugin extends AbstractUIPlugin {

	// FIXME annoying multiple loggers
	private static final Logger logger = Logger.getLogger(UIPlugin.PLUGIN_ID);
	
	public static final String PLUGIN_ID = "ecobertura.ui"; //$NON-NLS-1$

	private static UIPlugin plugin;
	
	private EditorsAnnotator editorTracker;
	private CoverageResultsUIListener resultsListener;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		UIPlugin.plugin = this;
		
		EclipseLogger.logFor(getLog());
		// FIXME: logger shouldn't log everything multiple times (this is a multiple classloaders problem, probably)
		logger.info("Cobertura plugin started."); //$NON-NLS-1$
		
		resultsListener = CoverageResultsUIListener.register();
		editorTracker = EditorsAnnotator.trackEditorsOf(getWorkbench());
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		UIPlugin.plugin = null;
		editorTracker.dispose();
		resultsListener.unregister();
		super.stop(context);
		
		logger.info("Cobertura plugin stopped."); //$NON-NLS-1$
	}

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
