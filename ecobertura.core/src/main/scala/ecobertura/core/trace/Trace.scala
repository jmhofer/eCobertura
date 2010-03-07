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
