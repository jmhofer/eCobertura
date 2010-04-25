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

import org.eclipse.core.runtime._
import org.eclipse.debug.core._
import org.eclipse.debug.ui._

class CoverageConfigurationTabGroup extends ILaunchConfigurationTabGroup with IExecutableExtension {
	var tabGroupToExtend: Option[ILaunchConfigurationTabGroup] = None
	var filterTab: ILaunchConfigurationTab = null
	
	override def setInitializationData(config: IConfigurationElement, propertyName: String, data: Any) = {
		val tabGroup = ConfigurationTabGroupRetriever.fromConfig(config)
		if (tabGroup != null)
			tabGroupToExtend = Some(tabGroup)
	}
	
	override def createTabs(dialog: ILaunchConfigurationDialog, mode: String) = 
			tabGroupToExtend match {
		case Some(tabGroup) => {
		  tabGroup.createTabs(dialog, ILaunchManager.RUN_MODE)
		  filterTab = new CoverageConfigurationFilterTab
		}
		case _ => /* nothing to do */
	}
	
	override def getTabs = tabGroupToExtend match {
		case Some(tabGroup) => {
		  val standardTabs = tabGroup.getTabs.toList
		  val standardTabsAndFilterTab = standardTabs.head :: filterTab :: standardTabs.tail
		  standardTabsAndFilterTab.toArray
		}
		case _ => Array[ILaunchConfigurationTab]()
	}
	
	override def initializeFrom(config: ILaunchConfiguration) = tabGroupToExtend match {
		case Some(tabGroup) => {
		  tabGroup.initializeFrom(config)
		  filterTab.initializeFrom(config)
		}
		case _ => /* nothing to do */
	}
	
	override def setDefaults(configWC: ILaunchConfigurationWorkingCopy) = tabGroupToExtend match {
		case Some(tabGroup) => {
		  tabGroup.setDefaults(configWC)
		  filterTab.setDefaults(configWC)
		}
		case _ => /* nothing to do */
	}

	override def performApply(configWC: ILaunchConfigurationWorkingCopy) = tabGroupToExtend match {
		case Some(tabGroup) => {
		  tabGroup.performApply(configWC)
		  filterTab.performApply(configWC)
		}
		case _ => /* nothing to do */
	}
	
	override def dispose = tabGroupToExtend match {
		case Some(tabGroup) => {
		  tabGroup.dispose
		  filterTab.dispose
		}
		case _ => /* nothing to do */
	}
	
	override def launched(launch: ILaunch) = {
		// deprecated, ignore
	}
}
