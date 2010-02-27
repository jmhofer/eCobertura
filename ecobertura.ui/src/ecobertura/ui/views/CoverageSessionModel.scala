package ecobertura.ui.views

import net.sourceforge.cobertura.coveragedata.ProjectData

object CoverageSessionModel {
	private var instance = new CoverageSessionModel(None)

	def get = instance
	
	def clear = {
		instance = new CoverageSessionModel(None)
	}
	
	def setProjectData(projectData : ProjectData) = {
		instance = new CoverageSessionModel(Some(projectData))
	}
}

class CoverageSessionModel(projectData: Option[ProjectData]) {
	
}
