package ecobertura.ui.core

import java.util.logging.Logger

import ecobertura.core.CorePlugin
import ecobertura.core.data.CoverageSession
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
	
	override def coverageRunCompleted(coverageSession: CoverageSession) = {
		logger fine "coverage run completed - we have data!"
		for (packageData <- coverageSession.packages) {
			logger.fine(packageData.name)
			for (classData <- packageData.classes) {
				logger.fine(classData.name)
			}
		}
	}
}
