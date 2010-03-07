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
package ecobertura.ui.launching.config

import java.util.logging._

import org.eclipse.core.runtime._
import org.eclipse.debug.core.ILaunchManager
import org.eclipse.debug.ui._

object ConfigurationTabGroupRetriever {
	val logger = Logger.getLogger("ecobertura.ui.launching.config") //$NON-NLS-1$
	
	def fromConfig(config: IConfigurationElement): ILaunchConfigurationTabGroup = {
		new ConfigurationTabGroupRetriever(config).getConfigurationTabGroup
	}
}

class ConfigurationTabGroupRetriever(config: IConfigurationElement) {
	import ConfigurationTabGroupRetriever.logger
	
	var launchTypeId : String = null 
	
	def getConfigurationTabGroup = {
		launchTypeId = config.getAttribute("type") //$NON-NLS-1$
		val launchConfigTabGroups = Platform.getExtensionRegistry.getExtensionPoint(
				IDebugUIConstants.PLUGIN_ID, 
				IDebugUIConstants.EXTENSION_POINT_LAUNCH_CONFIGURATION_TAB_GROUPS)
		getTabGroupFromExtensionPoint(launchConfigTabGroups)
		
	}
	
	private def getTabGroupFromExtensionPoint(launchConfigTabGroups: IExtensionPoint) = {
		val launchConfigTabGroup = launchConfigTabGroups.getConfigurationElements.find(delegateConfig =>
				hasCorrectType(delegateConfig) && hasRunMode(delegateConfig))
		launchConfigTabGroup match {
			case Some(launchConfigTabGroupFound) => 
				getTabGroupFromConfigurationElement(launchConfigTabGroupFound)
			case None => {
				logger.warning(String.format(
					"No standard launch configuration tab group for launch type '%s' found.", //$NON-NLS-1$ 
					launchTypeId)) 
				null
			}
		}
	}

	private def hasCorrectType(delegateConfig: IConfigurationElement) = 
		launchTypeId.equals(delegateConfig.getAttribute("type")) //$NON-NLS-1$
		
	private def hasRunMode(delegateConfig: IConfigurationElement) = {
		delegateConfig.getChildren("launchMode").exists( //$NON-NLS-1$
			launchModeConfig => ILaunchManager.RUN_MODE == launchModeConfig.getAttribute("mode")) //$NON-NLS-1$
	}
	
	private def getTabGroupFromConfigurationElement(delegateConfig: IConfigurationElement) = {
		try {
			delegateConfig.createExecutableExtension("class").asInstanceOf[ILaunchConfigurationTabGroup] //$NON-NLS-1$
		} catch {
			case e: CoreException => {
				logger.log(Level.WARNING, "unable to create launch configuration tab group", e) //$NON-NLS-1$
				null
			}
		}
	}
}
