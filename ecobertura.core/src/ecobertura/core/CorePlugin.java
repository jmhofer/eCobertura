package ecobertura.core;


import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

import ecobertura.core.log.Logger;

/**
 * The activator class controls the plug-in life cycle
 */
public class CorePlugin extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ecobertura.core";

	// The shared instance
	private static CorePlugin plugin;
	
	/**
	 * The constructor
	 */
	public CorePlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Logger.logFor(getLog());
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CorePlugin getDefault() {
		return plugin;
	}

}
