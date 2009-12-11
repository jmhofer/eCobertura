package ecobertura.core.util

import java.util.concurrent.Semaphore

import net.sourceforge.cobertura.coveragedata.ProjectData

import ecobertura.core.CorePlugin
import ecobertura.core.results.CoverageResultsListener

object LaunchTracker {
	def prepareLaunch = new LaunchTracker
}

class LaunchTracker {
	private var projectData: Option[ProjectData] = None

	private val launchRunning = new Semaphore(1)
	
	registerCoverageResultsListener
	launchRunning.acquire
	
	private def registerCoverageResultsListener = {
		CorePlugin.instance.coverageResultsCollector addListener new CoverageResultsListener {
			override def coverageRunCompleted(projectData: ProjectData) = {
				assert(projectData != null)
				LaunchTracker.this.projectData = Some(projectData)
				launchRunning.release
			}
		}
	}
	
	def waitForAndRetrieveCoverageResults = {
		launchRunning.acquire
		launchRunning.release

		assert(projectData != None)
		projectData.get
	}
}