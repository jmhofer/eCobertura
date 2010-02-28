package ecobertura.ui

import java.util.logging.Logger
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

import ecobertura.core.log.EclipseLogger
import ecobertura.ui.core._
import ecobertura.ui.editors.EditorsAnnotator

/**
 * Controls the plug-in life cycle of the eCobertura UI.
 */
object UIPlugin {
	// FIXME annoying multiple loggers
	private val internalPluginId = "ecobertura.ui" //$NON-NLS-1$
	private val logger = Logger.getLogger(internalPluginId)

	private var internalInstance: UIPlugin = null
	
	def instance = internalInstance
	def pluginId = internalPluginId
	
	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	def getImageDescriptor(path: String) = {
		AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, path)
	}
}

class UIPlugin extends AbstractUIPlugin {
	import UIPlugin._
	
	private var editorTracker: EditorsAnnotator = null
	private var resultsListener: CoverageResultsUIListener = null
	
	override def start(context: BundleContext) = {
		super.start(context)
		internalInstance = this
		EclipseLogger.logFor(getLog)
		// FIXME: logger shouldn't log everything multiple times (this is a multiple classloaders problem, probably)
		logger.info("Cobertura plugin started.") //$NON-NLS-1$
		
		resultsListener = CoverageResultsUIListener.register
		editorTracker = EditorsAnnotator.trackEditorsOf(getWorkbench)
	}
	
	override def stop(context: BundleContext) = {
		internalInstance = null
		editorTracker.dispose
		resultsListener.unregister
		super.stop(context)
		logger.info("Cobertura plugin stopped.") //$NON-NLS-1$
	}
}
