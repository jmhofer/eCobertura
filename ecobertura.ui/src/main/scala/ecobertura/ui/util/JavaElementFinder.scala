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
					logger.fine(String.format("Match found: %s", searchMatch.getElement))
					callback(javaElement)
				}
				case _ => /* nothing to do */
			}
		}
	}
}