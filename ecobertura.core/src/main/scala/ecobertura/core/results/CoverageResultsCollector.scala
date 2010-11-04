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
package ecobertura.core.results

import java.util.logging.Logger

import net.sourceforge.cobertura.coveragedata.ProjectData

import org.eclipse.debug.core._
import org.eclipse.debug.core.model.IProcess

import ecobertura.core.cobertura.CoberturaWrapper
import ecobertura.core.data.CoverageSession

object CoverageResultsCollector {
  def collect = new CoverageResultsCollector
}

class CoverageResultsCollector private () extends IDebugEventSetListener {
  val logger = Logger.getLogger("ecobertura.core.results")
  
  private var listeners = List[CoverageResultsListener]() 
  private var currentLaunch: Option[ILaunch] = None
  
  DebugPlugin.getDefault.addDebugEventListener(this)
  
  override def handleDebugEvents(events: Array[DebugEvent]) = {
    def isCoverageLaunchTerminationEvent(event: DebugEvent) : Boolean = {
      if (!isLaunchTerminationEvent(event)) false
      else {
        val launch = event.getSource.asInstanceOf[IProcess].getLaunch
        Some(launch) == currentLaunch
      }
    }
    
    def isLaunchTerminationEvent(event: DebugEvent) : Boolean = 
        event.getSource.isInstanceOf[IProcess] && event.getKind == DebugEvent.TERMINATE
        
    def notifyListeners(coverageSession: CoverageSession) = 
        listeners.foreach(_.coverageRunCompleted(coverageSession))

    def retrieveCoverageData = 
        CoverageSession.fromCoberturaProjectData(CoberturaWrapper.get.projectDataFromDefaultFile)

    for (event <- events if isCoverageLaunchTerminationEvent(event)) {
      logger.fine("detected termination of covered launch: " + event.toString)
      notifyListeners(retrieveCoverageData)
    }
  }
  
  def coveredLaunchStarted(launch: ILaunch) = {
    assert(launch != null)
    logger.fine("covered launch started...")
    currentLaunch = Some(launch)
  }
  
  def stopCollecting = {
    DebugPlugin.getDefault.removeDebugEventListener(this)
  }
  
  def addListener(listener: CoverageResultsListener) = {
    logger.fine("adding listener " + listener.toString)
    listeners ::= listener
  }
  def removeListener(listener: CoverageResultsListener) = listeners.filterNot(_.equals(listener)) 
}
