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
		
	private val logger = Logger getLogger "ecobertura.core.launching"
	
	def instrumentClassesFor(configuration: ILaunchConfiguration) = 
		new LaunchInstrumenter(configuration)
}

class LaunchInstrumenter(configuration: ILaunchConfiguration) {
	import LaunchInstrumenter._
	
	private val configWC = configuration.getWorkingCopy
	instrumentClasspath
	addCoberturaToClasspath
	addDatafileSystemProperty
	
	private def instrumentClasspath = {
		def resolvedClasspathEntries = {
			val unresolvedClasspathEntries = 
				JavaRuntime computeUnresolvedRuntimeClasspath configuration
			val resolvedClasspathEntries = 
				JavaRuntime resolveRuntimeClasspath (unresolvedClasspathEntries, configuration)
			
			resolvedClasspathEntries
		}
		
		resolvedClasspathEntries foreach (classpathEntry => {
			def containsUserClassesFromProject(entry: IRuntimeClasspathEntry) = {
				entry.getClasspathProperty == IRuntimeClasspathEntry.USER_CLASSES &&
				entry.getType == IRuntimeClasspathEntry.PROJECT
			}

			def instrumentFilesWithin(file: File) : Unit = {
				def instrumentClassFile(file: File) = {
					logger.fine(String format ("instrumenting %s", file.getPath()))
					CoberturaWrapper.get instrumentClassFile file
				}
				
				if (file.isDirectory) {
					file.listFiles foreach (instrumentFilesWithin(_))
				} else instrumentClassFile(file)
			}
			
			if (containsUserClassesFromProject(classpathEntry)) {
				val userClasspath = classpathEntry.getLocation
				logger.fine(String format ("instrumenting classes within %s", userClasspath))
				instrumentFilesWithin(new File(userClasspath))
			} else logger.fine(String format ("skipping %s", classpathEntry.getLocation))
		})
	}

	private def addCoberturaToClasspath = {
		configWC setAttribute (IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER, 
				CoverageClasspathProvider.ID)
		CoverageClasspathProvider wrap (JavaRuntime getClasspathProvider configuration)
	}
	
	private def addDatafileSystemProperty = {
		def requiresCoberturaVMArgument(currentVMArguments: String, coberturaVMArgument: String) = {
			(currentVMArguments indexOf coberturaVMArgument) == -1
		}
		
		val coberturaFile = new File(CorePlugin.instance.pluginState.instrumentationDataDirectory,
				CoberturaWrapper.DEFAULT_COBERTURA_FILENAME)
		val coberturaVMArgument = String format ("-D%s=%s ", COBERTURA_DATAFILE_PROPERTY, 
				coberturaFile.getAbsolutePath)
		val currentVMArguments = configWC getAttribute (
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "")
		
		if (requiresCoberturaVMArgument(currentVMArguments, coberturaVMArgument))
			configWC setAttribute (IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
					String format ("%s %s ", currentVMArguments, coberturaVMArgument))
	}
	
	def getUpdatedLaunchConfiguration = configWC
}
