package ecobertura.ui.util

import org.eclipse.jdt.core.search._

import ecobertura.ui.views.session.CoverageSessionClass

object JavaElementFinder {
	def fromCoverageSessionClass(covClass: CoverageSessionClass) = new JavaElementFinder(covClass)
}

class JavaElementFinder(covClass: CoverageSessionClass) {
    val searchPattern = SearchPattern.createPattern(
    		covClass.coverageData.packageName + "." + covClass.coverageData.name, 
    		IJavaSearchConstants.TYPE, IJavaSearchConstants.DECLARATIONS, 
    		SearchPattern.R_EXACT_MATCH);
	val searchScope = SearchEngine.createWorkspaceScope
	
	def find = {
		
		null // TODO start search and collect results
	}
}