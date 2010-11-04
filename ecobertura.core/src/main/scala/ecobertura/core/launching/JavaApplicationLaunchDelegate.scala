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

import org.eclipse.core.runtime._
import org.eclipse.debug.core._
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2

import _root_.ecobertura.core.CorePlugin

class JavaApplicationLaunchDelegate extends ILaunchConfigurationDelegate2 
    with IExecutableExtension {

  private var delegateToExtend: ILaunchConfigurationDelegate2 = _

  override def setInitializationData(config: IConfigurationElement, propertyName: String, data: Any) =
      delegateToExtend = LaunchDelegateRetriever.fromExtensionConfig(config).getDelegate

  override def launch(configuration: ILaunchConfiguration, mode: String, launch: ILaunch, 
      monitor: IProgressMonitor) = {

    // TODO monitor

    val launchInstrumenter = LaunchInstrumenter.instrumentClassesFor(configuration)
    val updatedConfiguration = launchInstrumenter.getUpdatedLaunchConfiguration
  
    CorePlugin.instance.coverageResultsCollector.coveredLaunchStarted(launch)
    delegateToExtend.launch(updatedConfiguration, ILaunchManager.RUN_MODE, launch, monitor)
  }
	
  override def buildForLaunch(configuration: ILaunchConfiguration, mode: String,
      monitor: IProgressMonitor) = {
    // simply forward for now
    delegateToExtend.buildForLaunch(configuration, mode, monitor)
  }
  
  override def finalLaunchCheck(configuration: ILaunchConfiguration, mode: String,
      monitor: IProgressMonitor) = {
    // simply forward for now
    delegateToExtend.finalLaunchCheck(configuration, mode, monitor)
  }
  
  override def getLaunch(configuration: ILaunchConfiguration, mode: String) = {
    // simply forward for now
    delegateToExtend.getLaunch(configuration, mode)
  }
  
  override def preLaunchCheck(configuration: ILaunchConfiguration, mode: String,
      monitor: IProgressMonitor) = {
    // simply forward for now
    delegateToExtend.preLaunchCheck(configuration, mode, monitor)
  }
}
