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

import ecobertura.core.CorePlugin
import ecobertura.core.cobertura.CoberturaWrapper
import ecobertura.core.data.filters.ClassFilters

object LaunchInstrumenter {
  val COBERTURA_DATAFILE_PROPERTY = "net.sourceforge.cobertura.datafile"

  private val logger = Logger.getLogger("ecobertura.core.launching")

  def instrumentClassesFor(configuration: ILaunchConfiguration) =
      new LaunchInstrumenter(configuration)
}

class LaunchInstrumenter private (configuration: ILaunchConfiguration) {
  import LaunchInstrumenter._

  private val classFilters = ClassFilters(configuration)
  private val configWC = configuration.getWorkingCopy
  CoberturaWrapper.get.resetProjectData
  instrumentClasspath
  CoberturaWrapper.get.saveProjectDataToDefaultFile
  addCoberturaAndCoveredClassesToClasspath
  addDatafileSystemProperty

  private def instrumentClasspath = {
    CorePlugin.instance.pluginState.cleanClasses()

    val unresolvedClasspathEntries =
        JavaRuntime.computeUnresolvedRuntimeClasspath(configuration)
    val resolvedClasspathEntries =
        JavaRuntime.resolveRuntimeClasspath(unresolvedClasspathEntries, configuration)

    resolvedClasspathEntries foreach (classpathEntry => {
      def isClassFile(file: File) = file.getName.endsWith(".class")

      def containsUserClassesFromProject(entry: IRuntimeClasspathEntry) = {
        logger.fine("examining classpath entry: type %d, property %d".format(
            entry.getType, entry.getClasspathProperty))
        Classpaths.containsUserClassesFromProject(entry)
      }

      def instrumentFilesWithin(file: File, relativePath: List[String]) : Unit = {
        def instrumentClassFile(file: File) = {
          logger.fine("instrumenting %s".format(file.getPath()))
          CoberturaWrapper.get.instrumentClassFile(file)
        }

        if (file.isDirectory)
          for (subFile <- file.listFiles)
            instrumentFilesWithin(subFile, subFile.getName :: relativePath)
        else if (isClassFile(file) && classFilters.isClassIncluded(relativePath))
          instrumentClassFile(file)
      }

      if (containsUserClassesFromProject(classpathEntry)) {
        val userClasspath = classpathEntry.getLocation
        logger.fine("instrumenting classes within %s".format(userClasspath))
        CorePlugin.instance.pluginState.copyClassesFrom(new File(userClasspath))
        instrumentFilesWithin(CorePlugin.instance.pluginState.instrumentedClassesDirectory, Nil)

      } else logger.fine("skipping %s".format(classpathEntry.getLocation))
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
    val coberturaVMArgument = String.format("-D%s=\"%s\" ", COBERTURA_DATAFILE_PROPERTY,
        coberturaFile.getAbsolutePath)
    val currentVMArguments = configWC.getAttribute(
        IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "")

    if (requiresCoberturaVMArgument(currentVMArguments, coberturaVMArgument))
      configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
          String.format("%s %s ", currentVMArguments, coberturaVMArgument))
  }

  def getUpdatedLaunchConfiguration = configWC
}
