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
package ecobertura.core.trace

import java.util.logging._
import org.eclipse.core.runtime.Platform

object Trace {
  def configureForPluginId(pluginId: String) = {
    val GLOBAL_PACKAGE = "ecobertura" //$NON-NLS-1$
    val GLOBAL_TRACE_LEVEL = "%s/debug/level" //$NON-NLS-1$
    
    def globalLevel = getDebugOptionStringOrElse(
        String format (GLOBAL_TRACE_LEVEL, pluginId), Level.WARNING.toString)

    Logger.getLogger(GLOBAL_PACKAGE) setLevel (Level parse globalLevel)
    Logger.getLogger(GLOBAL_PACKAGE) addHandler configuredConsoleHandler
            
    def getDebugOptionStringOrElse(optionKey: String, defaultValue: String) : String = {
      require(optionKey != null)
      
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
