package ecobertura.core.launching

import java.util.Arrays
import java.util.logging.Logger

import org.eclipse.core.runtime.IPath
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.jdt.launching._

import _root_.ecobertura.core.cobertura.CoberturaWrapper;

object CoverageClasspathProvider {
	val ID = "ecobertura.core.launching.coverageClasspathProvider" //$NON-NLS-1$
		
	private val logger = Logger getLogger "ecobertura.core.launching" //$NON-NLS-1$
	private var wrappedProvider = new ThreadLocal[IRuntimeClasspathProvider]
	
	def wrap(wrappedProvider: IRuntimeClasspathProvider) = {
		if (wrappedProvider != CoverageClasspathProvider.wrappedProvider) {
			logger fine "wrapping provider..."
			CoverageClasspathProvider.wrappedProvider set wrappedProvider
		}
	}
}

class CoverageClasspathProvider extends IRuntimeClasspathProvider {
	import CoverageClasspathProvider._
	
	override def computeUnresolvedClasspath(configuration: ILaunchConfiguration) = 
		wrappedProvider.get computeUnresolvedClasspath configuration
	
	override def resolveClasspath(entries: Array[IRuntimeClasspathEntry], 
			configuration: ILaunchConfiguration) = {
		
		def coberturaEntry = {
			val pathToCoberturaJar = CoberturaWrapper.get.pathToJar
			JavaRuntime newArchiveRuntimeClasspathEntry pathToCoberturaJar
		}
		
		logger fine "resolving classpath..."
		val resolvedEntries = wrappedProvider.get resolveClasspath (entries, configuration)
		val resolvedEntriesWithCobertura = Arrays copyOf (
				resolvedEntries, resolvedEntries.length + 1)
		resolvedEntriesWithCobertura(resolvedEntries.size) = coberturaEntry
		logger fine ("resolved entries: " + resolvedEntries)
		
		resolvedEntriesWithCobertura
	}
}
