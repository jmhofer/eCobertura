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
package ecobertura.ui.util

import java.util.logging._
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.search._

import ecobertura.ui.views.session.CoverageSessionClass

object JavaElementFinder {
	val logger = Logger.getLogger("ecobertura.ui.util")
	val searchEngine = new SearchEngine 
	val searchScope = SearchEngine.createWorkspaceScope
	
	def fromCoverageSessionClass(covClass: CoverageSessionClass) = new JavaElementFinder(covClass)
}

class JavaElementFinder(covClass: CoverageSessionClass) {
	import JavaElementFinder._

	val qualifiedClassNameToFind = 
		if (covClass.coverageData.packageName.isEmpty) covClass.coverageData.name
		else "%s.%s".format(covClass.coverageData.packageName, covClass.coverageData.name)
	logger.fine("looking for %s".format(qualifiedClassNameToFind))
	
    val searchPattern = SearchPattern.createPattern(
    		qualifiedClassNameToFind, 
    		IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS, 
    		SearchPattern.R_EXACT_MATCH)
	
	def find(callback: IJavaElement => Unit) = {
		try {
			searchEngine.search(searchPattern, 
					Array[SearchParticipant](SearchEngine.getDefaultSearchParticipant), 
					searchScope, new SearchHandler(callback), null)
			logger.fine("searching...")
		} catch {
			case e: ClassCastException => findBySourceFile(callback)
			case otherException => logger.log(Level.FINE, "some other exception occured", otherException)
		}
	}
	
	class SearchHandler(callback: IJavaElement => Unit) extends SearchRequestor {
		private var matchFound = false
		
		override def acceptSearchMatch(searchMatch: SearchMatch) = {
			searchMatch.getElement match {
				case javaElement: IJavaElement => {
					logger.fine("Match found: %s".format(javaElement))
					matchFound = true
					callback(javaElement)
				}
				case other => logger.fine("something else found: %s".format(other.toString)) /* nothing to do */
			}
		}
		
		override def endReporting = {
			if (!matchFound) {
				logger.fine("no match found")
				findBySourceFile(callback)
			}
		}
	}
	
	private def findBySourceFile(callback: IJavaElement => Unit) = {
		SourceFileFinder.fromSourceFileName(covClass.coverageData.sourceFileName)	
	}
}