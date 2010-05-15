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
package ecobertura.core.log

import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

import ecobertura.core.CorePlugin

object LogStatus {
  
  object Severity extends Enumeration {
    val Error = Value(IStatus.ERROR)
    val Warning = Value(IStatus.WARNING)
    val Info = Value(IStatus.INFO)
  }
      
  def something = Severity.Info
  
  def fromMessageWithSeverity(message: String, severity: Severity.Value) = { 
    require(message != null)
    new Status(severity.id, CorePlugin.pluginId, message)
  }
  
  def fromExceptionWithSeverity(message: String, throwable: Throwable, severity: Severity.Value) = {
    require(message != null)
    require(throwable != null)
    new Status(severity.id, CorePlugin.pluginId, message, throwable)    
  }
}