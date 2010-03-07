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
package ecobertura.core.util

import scala.collection.JavaConversions._
import scala.collection.mutable

import org.eclipse.core.runtime.CoreException
import org.eclipse.debug.core._
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants

object JavaApplicationLaunchConfiguration {
	def forProject(javaProject: JavaProject) = new JavaApplicationLaunchConfiguration(javaProject)
}

class JavaApplicationLaunchConfiguration(javaProject: JavaProject) {
	def createForMainType(mainTypeName: String) = {
		val configWC = retrieveJavaApplicationConfigurationWorkingCopy
		configWC setAttribute (IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainTypeName)
		updateClasspath(configWC)
		configWC.doSave
	}
	
	private def retrieveJavaApplicationConfigurationWorkingCopy = {
		val launchMgr = DebugPlugin.getDefault.getLaunchManager
		val javaLaunchType = launchMgr getLaunchConfigurationType 
				IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION
		val configWC = javaLaunchType newInstance (null, "sampleLaunchConfig")
		configWC
	}
	
	// FIXME this doesn't work for some reason...
	private def updateClasspath(configWC: ILaunchConfigurationWorkingCopy) = {
		var classpath = configWC.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, 
				mutable.Buffer[Any]()).asInstanceOf[mutable.Buffer[String]]
		classpath add javaProject.defaultClasspath.getMemento
		configWC setAttribute (IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false)
		configWC setAttribute (IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath)
	}
}
