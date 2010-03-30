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
package ecobertura.ui

import java.util.logging.Logger
import org.eclipse.jface.resource.ImageDescriptor
import org.eclipse.ui.plugin.AbstractUIPlugin
import org.osgi.framework.BundleContext

import ecobertura.core.log.EclipseLogger
import ecobertura.ui.core._
import ecobertura.ui.editors.EditorsAnnotator
import ecobertura.ui.preferences.PluginPreferences

/**
 * Controls the plug-in life cycle of the eCobertura UI.
 */
object UIPlugin {
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
	private var pluginPreferences: PluginPreferences = null
	
	override def start(context: BundleContext) = {
		super.start(context)
		pluginPreferences = new PluginPreferences(getPreferenceStore)
		internalInstance = this
		EclipseLogger.logFor(getLog)
		// FIXME logger shouldn't log everything multiple times (this is a multiple classloaders problem, probably)
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
	
	def preferences = pluginPreferences
}
