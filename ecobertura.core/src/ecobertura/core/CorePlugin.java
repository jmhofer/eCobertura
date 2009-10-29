package ecobertura.core;

import java.util.logging.Logger;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import ecobertura.core.log.EclipseLogger;
import ecobertura.core.state.PluginState;
import ecobertura.core.trace.Trace;

/**
 * Controls the plug-in life cycle of the eCobertura Core.
 */
public class CorePlugin extends Plugin {

	private static final Logger logger = Logger.getLogger(CorePlugin.PLUGIN_ID);
	public static final String PLUGIN_ID = "ecobertura.core"; //$NON-NLS-1$

	private static CorePlugin instance;
	
	private PluginState pluginState;
	
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		instance = this;
		
		Trace.configureForPluginId(PLUGIN_ID);
		EclipseLogger.logFor(getLog());

		pluginState = PluginState.initialize(getStateLocation());
		
		logger.info("plugin started"); //$NON-NLS-1$
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		pluginState.cleanUp();
		instance = null;
		super.stop(context);
		logger.info("plugin stopped"); //$NON-NLS-1$
	}
	
	public static CorePlugin instance() {
		return instance;
	}
	
	public PluginState pluginState() {
		return pluginState;
	}
}
