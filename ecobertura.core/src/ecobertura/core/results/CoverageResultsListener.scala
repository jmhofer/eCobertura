package ecobertura.core.results

import net.sourceforge.cobertura.coveragedata.ProjectData

trait CoverageResultsListener {
	def coverageRunCompleted(projectData: ProjectData) : Unit
}
