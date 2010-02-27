package ecobertura.ui.views.session

import net.sourceforge.cobertura.coveragedata.ProjectData

object CoverageSessionModel {
	private var instance = new CoverageSessionModel

	def get = instance
}

class CoverageSessionModel extends CoverageSessionPublisher {
	private var projectData: Option[ProjectData] = None
	
	def clear = {
		projectData = None
		fireSessionReset
	}
	
	def setProjectData(projectData: ProjectData) = {
		this.projectData = Some(projectData)
		fireSessionReset
	}
}
