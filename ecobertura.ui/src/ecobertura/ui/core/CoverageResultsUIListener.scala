package ecobertura.ui.core

import java.util.logging.Logger

import net.sourceforge.cobertura.coveragedata._
import ecobertura.core.CorePlugin
import ecobertura.core.results.CoverageResultsListener
import ecobertura.ui.UIPlugin

object CoverageResultsUIListener {
	def register = new CoverageResultsUIListener
}

class CoverageResultsUIListener extends CoverageResultsListener {
	val logger = Logger.getLogger(UIPlugin.pluginId)

	CorePlugin.instance.coverageResultsCollector addListener this
	logger fine "coverage results ui listener registered"
	
	def unregister = {
		CorePlugin.instance.coverageResultsCollector removeListener this
		logger fine "coverage results ui listener unregistered"
	}
	
	override def coverageRunCompleted(projectData: ProjectData) = {
		logger fine "coverage run completed - we have data!"
		for (classDataObj <- List(projectData.getClasses)) {
			val classData = classDataObj.asInstanceOf[ClassData]
			logger fine classData.getName
			for (lineDataObj <- List(classData.getLines)) {
				val lineData = lineDataObj.asInstanceOf[LineData]
				logger fine (lineData.getMethodName + ", " + lineData.getLineNumber + ", " + 
						lineData.getHits)
			}
		}
	}
}
