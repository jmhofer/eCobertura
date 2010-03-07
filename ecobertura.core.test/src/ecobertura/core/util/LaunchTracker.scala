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
package ecobertura.core.util
import java.util.concurrent.Semaphore

import ecobertura.core.CorePlugin
import ecobertura.core.data.CoverageSession
import ecobertura.core.results.CoverageResultsListener

object LaunchTracker {
	def prepareLaunch = new LaunchTracker
}

class LaunchTracker {
	private var session: Option[CoverageSession] = None

	private val launchRunning = new Semaphore(1)
	
	registerCoverageResultsListener
	launchRunning.acquire
	
	private def registerCoverageResultsListener = {
		CorePlugin.instance.coverageResultsCollector addListener new CoverageResultsListener {
			override def coverageRunCompleted(session: CoverageSession) = {
				assert(session != null)
				LaunchTracker.this.session = Some(session)
				launchRunning.release
			}
		}
	}
	
	def waitForAndRetrieveCoverageResults = {
		launchRunning.acquire
		launchRunning.release

		assert(session != None)
		session.get
	}
}