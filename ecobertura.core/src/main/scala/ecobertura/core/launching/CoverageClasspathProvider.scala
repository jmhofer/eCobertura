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

import java.util.Arrays
import java.util.logging.Logger

import org.eclipse.core.runtime.IPath
import org.eclipse.core.runtime.Path
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.jdt.launching._

import _root_.ecobertura.core.CorePlugin
import _root_.ecobertura.core.cobertura.CoberturaWrapper

object CoverageClasspathProvider {
  val ID = "ecobertura.core.launching.coverageClasspathProvider" //$NON-NLS-1$

  private val logger = Logger.getLogger("ecobertura.core.launching") //$NON-NLS-1$
  private var wrappedProvider = new ThreadLocal[IRuntimeClasspathProvider]

  def wrap(wrappedProvider: IRuntimeClasspathProvider) = {
    if (wrappedProvider != CoverageClasspathProvider.wrappedProvider) {
      logger.fine("wrapping provider...")
      CoverageClasspathProvider.wrappedProvider.set(wrappedProvider)
    }
  }
}

class CoverageClasspathProvider extends IRuntimeClasspathProvider {
  import CoverageClasspathProvider._

  override def computeUnresolvedClasspath(configuration: ILaunchConfiguration) = 
    wrappedProvider.get.computeUnresolvedClasspath(configuration)

  override def resolveClasspath(entries: Array[IRuntimeClasspathEntry], 
      configuration: ILaunchConfiguration) = {

    def coberturaEntry = {
      val pathToCoberturaJar = CoberturaWrapper.get.pathToJar
      JavaRuntime.newArchiveRuntimeClasspathEntry(pathToCoberturaJar)
    }

    logger.fine("resolving classpath...")
    val resolvedEntries = wrappedProvider.get.resolveClasspath(entries, configuration)
    logger.fine("resolved entries: " + resolvedEntries.mkString)

    val resolvedEntriesWithCoveredClasses = substituteProjectClassesByCoveredClasses(resolvedEntries)
    val resolvedEntriesWithCobertura = Arrays.copyOf(
        resolvedEntriesWithCoveredClasses, resolvedEntriesWithCoveredClasses.size + 1)
    resolvedEntriesWithCobertura(resolvedEntries.size) = coberturaEntry

    logger.fine("resolved entries with cobertura: " + resolvedEntriesWithCobertura.mkString)

    resolvedEntriesWithCobertura
  }

  private def substituteProjectClassesByCoveredClasses(resolvedEntries: Array[IRuntimeClasspathEntry]) = {
    for {
      i <- 0 until resolvedEntries.size
      entry = resolvedEntries(i)
      if entry.getClasspathProperty == IRuntimeClasspathEntry.USER_CLASSES &&
      entry.getType == IRuntimeClasspathEntry.PROJECT
    } resolvedEntries(i) = adaptedProjectClassesEntry(entry)

    resolvedEntries
  }

  private def adaptedProjectClassesEntry(projectClasses: IRuntimeClasspathEntry) = {
    logger.fine("adapting %s".format(projectClasses.getLocation))
    val newPath = new Path(CorePlugin.instance.pluginState.instrumentedClassesDirectory.getAbsolutePath)
    logger.fine("new path: %s".format(newPath.toString))
    JavaRuntime.newArchiveRuntimeClasspathEntry(newPath)
  }
}
