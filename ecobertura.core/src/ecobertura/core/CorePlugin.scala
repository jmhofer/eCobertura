package ecobertura.core

import java.util.logging.Logger

import org.eclipse.core.runtime.Plugin
import org.osgi.framework.BundleContext

import ecobertura.core.log.EclipseLogger
import ecobertura.core.results.CoverageResultsCollector
import ecobertura.core.state.PluginState
import ecobertura.core.trace.Trace

object CorePlugin {
	private val internalPluginId = "ecobertura.core" //$NON-NLS-1$
	private val logger = Logger getLogger internalPluginId
	
	private var internalInstance: CorePlugin = null
	
	def instance = internalInstance
	def pluginId = internalPluginId
}

class CorePlugin extends Plugin {
	import CorePlugin._
	
	private var internalPluginState: PluginState = null
	private var internalResultsCollector: CoverageResultsCollector = null
	
	def pluginState = internalPluginState
	def coverageResultsCollector = internalResultsCollector

	override def start(context: BundleContext): Unit = {
		super.start(context)
		internalInstance = this
		
		Trace configureForPluginId pluginId;
		EclipseLogger logFor getLog

		internalPluginState = PluginState initialize getStateLocation
		internalResultsCollector = CoverageResultsCollector collect
		
		logger info "plugin started" //$NON-NLS-1$
	}
	
	override def stop(context: BundleContext): Unit = {
		internalPluginState.cleanUp
		internalInstance = null
		super.stop(context)
		
		logger info "plugin stopped" //$NON-NLS-1$
	}
}
