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
package ecobertura.core.launching

import java.io.File

import org.eclipse.jdt.launching.IRuntimeClasspathEntry

/**
 * A small helper object for deciding what's what within runtime classpaths.
 */
object Classpaths {
  def containsUserClassesFromProject(entry: IRuntimeClasspathEntry) =
    entry.getClasspathProperty == IRuntimeClasspathEntry.USER_CLASSES && isDirectoryOfClasses(entry)
  
  private def isDirectoryOfClasses(entry: IRuntimeClasspathEntry) = {
    entry.getType == IRuntimeClasspathEntry.PROJECT ||
      (entry.getType == IRuntimeClasspathEntry.ARCHIVE && new File(entry.getLocation).isDirectory)
  }
}