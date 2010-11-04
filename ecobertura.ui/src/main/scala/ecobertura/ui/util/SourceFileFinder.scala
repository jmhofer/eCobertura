/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.ui.util

import java.util.logging._
import java.util.regex.Pattern

import org.eclipse.core.resources._
import org.eclipse.search.core.text._

import ecobertura.ui.views.session.CoverageSessionClass

object SourceFileFinder {
	val logger = Logger.getLogger("ecobertura.ui.util")
	val searchEngine = TextSearchEngine.create
	val everythingPattern = Pattern.compile(".*")
	
	def fromSourceFileName(sourceFileName: String) = new SourceFileFinder(sourceFileName)
}

class SourceFileFinder(sourceFileName: String) {
	import SourceFileFinder._
	
	logger.fine("trying to find %s".format(sourceFileName))
	val workspaceRoot = ResourcesPlugin.getWorkspace.getRoot
	val fileSearchPattern = Pattern.compile(Pattern.quote(sourceFileName))
	val scope = TextSearchScope.newSearchScope(Array[IResource](workspaceRoot),
			fileSearchPattern, false)

	def find(callback: IFile => Unit) = {
		searchEngine.search(scope, new SearchHandler(callback), everythingPattern, null)
	}
	
	class SearchHandler(callback: IFile => Unit) extends TextSearchRequestor {
		private var matchFound = false
		
		override def acceptFile(file: IFile) = {
			logger.fine("file found: %s".format(file.toString))
			matchFound = true
			callback(file)
			false
		}
		
		override def endReporting = {
			if (!matchFound) logger.fine("no match found")
		}
	}
}
