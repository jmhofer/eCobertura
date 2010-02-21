package ecobertura.core.trace

import java.util.logging._
import org.eclipse.core.runtime.Platform

object Trace {
	def configureForPluginId(pluginId: String) = {
		val GLOBAL_PACKAGE = "ecobertura" //$NON-NLS-1$
		val GLOBAL_TRACE_LEVEL = "%s/debug/level"; //$NON-NLS-1$
		
		def globalLevel = getDebugOptionStringOrElse(
				String format (GLOBAL_TRACE_LEVEL, pluginId), Level.WARNING.toString)

		Logger.getLogger(GLOBAL_PACKAGE) setLevel (Level parse globalLevel)
		Logger.getLogger(GLOBAL_PACKAGE) addHandler configuredConsoleHandler
				
		def getDebugOptionStringOrElse(optionKey: String, defaultValue: String) : String = {
			assert(optionKey != null)
			
			val optionValue = Platform getDebugOption optionKey
			if (optionValue == null) defaultValue.toUpperCase else optionValue.toUpperCase
		}

		def configuredConsoleHandler : Handler = {
			val consoleHandler = new ConsoleHandler
			consoleHandler setLevel Level.ALL
			
			consoleHandler
		}
	}
}
