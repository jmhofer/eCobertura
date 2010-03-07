/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
 */
package ecobertura.core

import java.util.logging.Logger

import org.eclipse.core.runtime.Plugin
import org.osgi.framework.BundleContext

import log.EclipseLogger
import results.CoverageResultsCollector
import state.PluginState
import trace.Trace

object CorePlugin {
	private val internalPluginId = "ecobertura.core" //$NON-NLS-1$
	private val logger = Logger.getLogger(internalPluginId)
	
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
		if (internalInstance == null) {
			super.start(context)
			internalInstance = this
			
			Trace.configureForPluginId(pluginId)
			EclipseLogger.logFor(getLog)
	
			internalPluginState = PluginState.initialize(getStateLocation)
			internalResultsCollector = CoverageResultsCollector.collect
			
			logger.info("plugin started") //$NON-NLS-1$
		}
	}
	
	override def stop(context: BundleContext): Unit = {
		if (internalInstance != null) {
			internalPluginState.cleanUp
			internalInstance = null
			super.stop(context)
			
			logger.info("plugin stopped") //$NON-NLS-1$
		}
	}
}
