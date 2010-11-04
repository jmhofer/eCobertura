/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.core.launching

import java.util.logging.Logger

import scala.collection.JavaConversions._
import scala.collection.mutable.HashSet

import org.eclipse.core.runtime._
import org.eclipse.debug.core._
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2

import ecobertura.core.CorePlugin

object LaunchDelegateRetriever {
  def fromExtensionConfig(config: IConfigurationElement) = new LaunchDelegateRetriever(config)
}

class LaunchDelegateRetriever private (config: IConfigurationElement) {
  val logger = Logger.getLogger("ecobertura.core.launching") //$NON-NLS-1$

  def getDelegate = {
    val launchTypeName = config.getAttribute("type") //$NON-NLS-1$
    val launchType = getLaunchConfigurationType(launchTypeName)

    getLaunchDelegateFor(launchType)
  }

  private def getLaunchConfigurationType(launchTypeName: String) = {
    val launchType = DebugPlugin.getDefault.getLaunchManager.getLaunchConfigurationType(
        launchTypeName)
    if (launchType == null) {
      throw new CoreException(new Status(IStatus.ERROR, CorePlugin.pluginId, 
          "unknown launch configuration type %s".format(launchTypeName))) //$NON-NLS-1$
    }
    launchType
  }

  private def getLaunchDelegateFor(launchType: ILaunchConfigurationType) = {
    val delegatesForType = launchType.getDelegates(HashSet(ILaunchManager.RUN_MODE))
    if (delegatesForType.isEmpty) {
      throw new CoreException(new Status(IStatus.ERROR, CorePlugin.pluginId,
          "no delegate for %s found".format(launchType.getName)))
    }
    convertToType2Delegate(delegatesForType.head.getDelegate)
  }

  private def convertToType2Delegate(delegate: ILaunchConfigurationDelegate) = {
    if (delegate.isInstanceOf[ILaunchConfigurationDelegate2]) 
      delegate.asInstanceOf[ILaunchConfigurationDelegate2]
    else 
      adaptToType2Delegate(delegate)
  }

  private def adaptToType2Delegate(delegate: ILaunchConfigurationDelegate) = {
    new ILaunchConfigurationDelegate2() {
      override def buildForLaunch(configuration: ILaunchConfiguration, mode: String,
          monitor: IProgressMonitor) = true

      override def finalLaunchCheck(configuration: ILaunchConfiguration, mode: String,
          monitor: IProgressMonitor) = true

      override def getLaunch(configuration: ILaunchConfiguration, mode: String) = null

      override def preLaunchCheck(configuration: ILaunchConfiguration, mode: String,
          monitor: IProgressMonitor) = true

      override def launch(configuration: ILaunchConfiguration, mode: String, launch: ILaunch,
          monitor: IProgressMonitor) = delegate.launch(configuration, mode, launch, monitor)
    }
  }
}