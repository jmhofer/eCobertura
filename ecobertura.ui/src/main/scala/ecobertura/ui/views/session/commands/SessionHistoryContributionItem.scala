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
package ecobertura.ui.views.session.commands

import java.util
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.menus._
import org.eclipse.ui.actions.CompoundContributionItem

import ecobertura.core.data.CoverageSession
import ecobertura.ui.views.session.CoverageSessionModel
import scala.collection.JavaConversions._

class SessionHistoryContributionItem extends CompoundContributionItem {
	override def getContributionItems =
		(for {
			coverageSession <- CoverageSessionModel.get.coverageSessionHistory
		} yield createCommandContributionItem(coverageSession)).toArray
	
	private def createCommandContributionItem(coverageSession: CoverageSession) = {
		val cciParam = new CommandContributionItemParameter(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow(), null,
			"ecobertura.ui.views.session.commands.selectRecentCoverageSession", 
			CommandContributionItem.STYLE_PUSH)
		
		cciParam.label = coverageSession.displayName
		cciParam.parameters = createSessionParameter(coverageSession.displayName)
		
		new CommandContributionItem(cciParam)
	}
	
	private def createSessionParameter(sessionName: String) = {
		val params = new util.HashMap[String, String]()
		params.put(
				"ecobertura.ui.views.session.commands.selectRecentCoverageSession.session", 
				sessionName)
		params
	}
}
