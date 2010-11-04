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
package ecobertura.core.cobertura

import java.io._
import java.lang.reflect._
import java.net.URL
import java.util.logging.Logger

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler
import net.sourceforge.cobertura.coveragedata.ProjectData
import net.sourceforge.cobertura.instrument.Main

import org.eclipse.core.runtime._

import ecobertura.core.CorePlugin
import ecobertura.core.log.LogStatus

trait ICoberturaWrapper {
  def resetProjectData
  def instrumentClassFile(classFileToInstrument: File)
  def saveProjectDataToDefaultFile
  def projectDataFromDefaultFile: ProjectData
  def projectDataFromFile(fileName: String): ProjectData
  def pathToJar: IPath
}

object CoberturaWrapper {
  val logger = Logger.getLogger("ecobertura.core.cobertura")
  	
  val DEFAULT_COBERTURA_FILENAME = "cobertura.ser" //$NON-NLS-1$
  val COBERTURA_JAR_NAME = "cobertura.jar" //$NON-NLS-1$
  
  private lazy val instance = new CoberturaWrapper
  def get = instance
	
  class CoberturaWrapper extends ICoberturaWrapper {
    private val COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS = "addInstrumentationToSingleClass"; //$NON-NLS-1$
    private val coberturaMain = new Main
    private var coberturaProjectData : ProjectData = new ProjectData
    
    initializeCoberturaProjectData
    
    private def initializeCoberturaProjectData = {
      logger.fine("initializing Cobertura project data...")
      coberturaProjectData = new ProjectData
      try {
        setPrivateProjectData
      } catch {
        case e: NoSuchFieldException => 
            wrapByCoberturaException("field %s not found in Cobertura Main", e) //$NON-NLS-1$
        case e: IllegalAccessException =>
            wrapByCoberturaException("unable to access field %s in Cobertura Main", e) //$NON-NLS-1$
      }

      def setPrivateProjectData = {
        val projectDataField = coberturaMain.getClass.getDeclaredField("projectData")
        projectDataField.setAccessible(true)
        projectDataField.set(coberturaMain, coberturaProjectData)
      }
    }

    override def projectDataFromFile(filename: String) : ProjectData = {
      val coberturaFile = new File(filename)
      CoverageDataFileHandler.loadCoverageData(coberturaFile)
    }
    
    override def resetProjectData = {
      defaultCoberturaFile.delete
      initializeCoberturaProjectData
    }
    
    override def projectDataFromDefaultFile : ProjectData =
      CoverageDataFileHandler.loadCoverageData(defaultCoberturaFile)
    
    override def saveProjectDataToDefaultFile =
      CoverageDataFileHandler.saveCoverageData(coberturaProjectData, defaultCoberturaFile)

    private def defaultCoberturaFile = 
      new File(CorePlugin.instance.pluginState.instrumentationDataDirectory, 
          CoberturaWrapper.DEFAULT_COBERTURA_FILENAME)
    
    override def instrumentClassFile(classFileToInstrument: File) = {
      try {
        invokePrivateAddInstrumentationMethodOnMain(classFileToInstrument, coberturaMain)
      } catch {
        case e: NoSuchMethodException => 
            wrapByCoberturaException("method %s() not found in Cobertura Main", e); //$NON-NLS-1$
        case e: IllegalAccessException =>
            wrapByCoberturaException("unable to access method %s() in Cobertura Main", e); //$NON-NLS-1$
        case e: InvocationTargetException =>
            wrapByCoberturaException("exception occurred within %s() in Cobertura Main", //$NON-NLS-1$
                e.getCause())
      }
      
      def invokePrivateAddInstrumentationMethodOnMain(
          classFileToInstrument: File, coberturaMain: Main) = {
        val addInstrumentationToSingleClass = coberturaMain.getClass.getDeclaredMethod(
            COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS, classOf[File])
        addInstrumentationToSingleClass.setAccessible(true)
        addInstrumentationToSingleClass.invoke(coberturaMain, classFileToInstrument)
      }
    }

    private def wrapByCoberturaException(messageTemplate: String, cause: Throwable) = {
      val message = String format (messageTemplate, COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS)
      throw new CoberturaException(message, cause)
    }
    
    override def pathToJar : IPath = {
      try {
        val url = FileLocator.toFileURL(FileLocator.find(new URL(
            String.format("platform:/plugin/ecobertura.cobertura/%s", COBERTURA_JAR_NAME))))
        new Path(url.getPath)
        
      } catch {
        case e: IOException => 
            throw new CoreException(LogStatus.fromExceptionWithSeverity(
                "unable to retrieve cobertura jar", e, LogStatus.Severity.Error))  
      }
    }
  }
}
