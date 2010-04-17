/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
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
			
	searchEngine.search(scope, new SearchHandler, everythingPattern, null)
			
	class SearchHandler extends TextSearchRequestor {
		private var matchFound = false
		
		override def acceptFile(file: IFile) = {
			logger.fine("file found: %s".format(file.toString))
			matchFound = true
			false
		}
		
		override def endReporting = {
			if (!matchFound) logger.fine("no match found")
		}
	}
}
