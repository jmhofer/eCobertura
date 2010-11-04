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