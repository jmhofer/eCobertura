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

import java.util.logging.Logger
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
	
    val searchPattern = SearchPattern.createPattern(
    		covClass.coverageData.packageName + "." + covClass.coverageData.name, 
    		IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS, 
    		SearchPattern.R_EXACT_MATCH);
	
	def find(callback: IJavaElement => Unit) = {
		searchEngine.search(searchPattern, 
				Array[SearchParticipant](SearchEngine.getDefaultSearchParticipant()), 
				searchScope, new SearchHandler(callback), null);
	}
	
	class SearchHandler(callback: IJavaElement => Unit) extends SearchRequestor {
		override def acceptSearchMatch(searchMatch: SearchMatch) = {
			searchMatch.getElement match {
				case javaElement: IJavaElement => {
					logger.fine(String.format("Match found: %s", javaElement))
					callback(javaElement)
				}
				case _ => /* nothing to do */
			}
		}
	}
}