package ecobertura.core.cobertura

import java.io._
import java.lang.reflect._
import java.net.URL

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler
import net.sourceforge.cobertura.coveragedata.ProjectData
import net.sourceforge.cobertura.instrument.Main

import org.eclipse.core.runtime._

import ecobertura.core.CorePlugin
import ecobertura.core.log.LogStatus

object CoberturaWrapper {
	def DEFAULT_COBERTURA_FILENAME = "cobertura.ser"; //$NON-NLS-1$

	private lazy val instance = new CoberturaWrapper()
	def get = instance
}

trait ICoberturaWrapper {
	def instrumentClassFile(classFileToInstrument: File)
	def projectDataFromFile(fileName: String) : ProjectData
	def pathToJar : IPath
}

class CoberturaWrapper extends ICoberturaWrapper {
	private val COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS = "addInstrumentationToSingleClass"; //$NON-NLS-1$
	private val coberturaMain = new Main()
	
	initializeCoberturaProjectData
	
	private def initializeCoberturaProjectData = {
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
			projectDataField setAccessible true
			projectDataField set (coberturaMain, new ProjectData())
		}
	}
	
	override def projectDataFromFile(fileName: String) : ProjectData = {
		val coberturaFile = new File(
				CorePlugin.instance.pluginState.instrumentationDataDirectory, fileName)
		CoverageDataFileHandler loadCoverageData coberturaFile
	}
	
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
			addInstrumentationToSingleClass setAccessible true
			addInstrumentationToSingleClass invoke (coberturaMain, classFileToInstrument)
		}
	}

	private def wrapByCoberturaException(messageTemplate: String, cause: Throwable) = {
		val message = String format (messageTemplate, COBERTURA_ADD_INSTRUMENTATION_TO_SINGLE_CLASS)
		throw new CoberturaException(message, cause)
	}
	
	override def pathToJar : IPath = {
		try {
			val url = FileLocator.toFileURL(FileLocator.find(new URL(
					String format ("platform:/plugin/%s/lib/cobertura.jar", CorePlugin.pluginId))))
			new Path(url.getPath)
			
		} catch {
			case e: IOException => 
				throw new CoreException(LogStatus.fromExceptionWithSeverity(
						"unable to retrieve cobertura jar", e, LogStatus.Severity.Error))  
		}
	}
}
