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

import org.eclipse.ui.PlatformUI
import org.eclipse.ui.menus._
import org.eclipse.ui.actions.CompoundContributionItem
import ecobertura.ui.views.session.CoverageSessionModel

class SessionHistoryContributionItem extends CompoundContributionItem {
	override def getContributionItems =
		(for {
			coverageSession <- CoverageSessionModel.get.coverageSessionHistory
		} yield createCommandContributionItem(coverageSession.toString)).toArray
	
	private def createCommandContributionItem(name: String) = {
		val cciParam = new CommandContributionItemParameter(
			PlatformUI.getWorkbench().getActiveWorkbenchWindow(), null,
			"ecobertura.ui.views.session.commands.openCoverageSession", 
//				"ecobertura.ui.views.session.commands.historyCommand",
// TODO add our new command...
			CommandContributionItem.STYLE_PUSH)
		
		// TODO add a useful name to coverage sessions
		cciParam.label = name
			
		new CommandContributionItem(cciParam)
	}
}

//    Map parms = new HashMap();
//    parms.put("groupBy", "Severity");
//    list[0] = new CommandContributionItem(null,
//            "org.eclipse.ui.views.problems.grouping",
//            parms, null, null, null, "Severity", null,
//            null, CommandContributionItem.STYLE_PUSH);
//}
