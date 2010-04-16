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
package ecobertura.core.launching

import java.io.File
import java.util.logging.Logger

import org.eclipse.core.runtime.CoreException
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy
import org.eclipse.jdt.launching._

import _root_.ecobertura.core.CorePlugin;
import _root_.ecobertura.core.cobertura.CoberturaWrapper;

object LaunchInstrumenter {
	val COBERTURA_DATAFILE_PROPERTY = "net.sourceforge.cobertura.datafile"
		
	private val logger = Logger.getLogger("ecobertura.core.launching")
	
	def instrumentClassesFor(configuration: ILaunchConfiguration) = 
		new LaunchInstrumenter(configuration)
}

class LaunchInstrumenter(configuration: ILaunchConfiguration) {
	import LaunchInstrumenter._
	
	private val configWC = configuration.getWorkingCopy
	CoberturaWrapper.get.resetProjectData
	instrumentClasspath
	CoberturaWrapper.get.saveProjectDataToDefaultFile
	addCoberturaAndCoveredClassesToClasspath
	addDatafileSystemProperty
	
	private def instrumentClasspath = {
		def resolvedClasspathEntries = {
			val unresolvedClasspathEntries = 
				JavaRuntime.computeUnresolvedRuntimeClasspath(configuration)
			val resolvedClasspathEntries = 
				JavaRuntime.resolveRuntimeClasspath(unresolvedClasspathEntries, configuration)
			
			resolvedClasspathEntries
		}
		
		resolvedClasspathEntries foreach (classpathEntry => {
			def containsUserClassesFromProject(entry: IRuntimeClasspathEntry) = {
				entry.getClasspathProperty == IRuntimeClasspathEntry.USER_CLASSES &&
				entry.getType == IRuntimeClasspathEntry.PROJECT
			}

			def instrumentFilesWithin(file: File) : Unit = {
				def instrumentClassFile(file: File) = {
					logger.fine(String.format("instrumenting %s", file.getPath()))
					CoberturaWrapper.get.instrumentClassFile(file)
				}
				
				if (file.isDirectory) {
					file.listFiles foreach (instrumentFilesWithin(_))
				} else instrumentClassFile(file)
			}
			
			if (containsUserClassesFromProject(classpathEntry)) {
				val userClasspath = classpathEntry.getLocation
				logger.fine(String format ("instrumenting classes within %s", userClasspath))
				CorePlugin.instance.pluginState.copyClassesFrom(new File(userClasspath))
				instrumentFilesWithin(CorePlugin.instance.pluginState.instrumentedClassesDirectory)
				
			} else logger.fine(String.format("skipping %s", classpathEntry.getLocation))
		})
	}

	private def addCoberturaAndCoveredClassesToClasspath = {
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, 
				CoverageClasspathProvider.ID)
		CoverageClasspathProvider.wrap(JavaRuntime.getClasspathProvider(configuration))
	}
	
	private def addDatafileSystemProperty = {
		def requiresCoberturaVMArgument(currentVMArguments: String, coberturaVMArgument: String) =
			(currentVMArguments.indexOf(coberturaVMArgument)) == -1
		
		val coberturaFile = new File(CorePlugin.instance.pluginState.instrumentationDataDirectory,
				CoberturaWrapper.DEFAULT_COBERTURA_FILENAME)
		val coberturaVMArgument = String.format("-D%s=%s ", COBERTURA_DATAFILE_PROPERTY, 
				coberturaFile.getAbsolutePath)
		val currentVMArguments = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "")
		
		if (requiresCoberturaVMArgument(currentVMArguments, coberturaVMArgument))
			configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
					String.format("%s %s ", currentVMArguments, coberturaVMArgument))
	}
	
	def getUpdatedLaunchConfiguration = configWC
}
