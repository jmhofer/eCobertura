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
package ecobertura.ui.core

import java.util.logging.Logger

import org.eclipse.swt.widgets.Display

import ecobertura.core.CorePlugin
import ecobertura.core.data.CoverageSession
import ecobertura.core.results.CoverageResultsListener
import ecobertura.ui.util.Predef._
import ecobertura.ui.UIPlugin
import ecobertura.ui.views.session.CoverageSessionModel

object CoverageResultsUIListener {
	def register = new CoverageResultsUIListener
}

class CoverageResultsUIListener extends CoverageResultsListener {
	val logger = Logger.getLogger(UIPlugin.pluginId)

	CorePlugin.instance.coverageResultsCollector.addListener(this)
	logger.fine("coverage results ui listener registered")
	
	def unregister = {
		CorePlugin.instance.coverageResultsCollector.removeListener(this)
		logger.fine("coverage results ui listener unregistered")
	}
	
	override def coverageRunCompleted(coverageSession: CoverageSession) = {
		logger.fine("coverage run completed - we have data!")
		logger.fine(coverageSession.toString)
		for (packageData <- coverageSession.packages) {
			logger.fine(packageData.toString)
			for (classData <- packageData.classes) {
				logger.fine(classData.toString)
			}
		}
		Display.getCurrent.syncExec {
			CoverageSessionModel.get.setCoverageSession(coverageSession)
		}
	}
}
