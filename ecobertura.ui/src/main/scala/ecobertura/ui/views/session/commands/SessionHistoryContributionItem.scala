/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.ui.views.session.commands

import org.eclipse.jface.action.IContributionItem
import java.util
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.menus._
import org.eclipse.ui.actions.CompoundContributionItem

import ecobertura.core.data.CoverageSession
import ecobertura.ui.views.session.CoverageSessionModel
import scala.collection.JavaConversions._

class SessionHistoryContributionItem extends CompoundContributionItem {
	override def getContributionItems = {
		val contributionsFromRecentSessions = retrieveContributionsFromRecentSessions
		
		if (contributionsFromRecentSessions.isEmpty) {
			Array(createDisabledEmptyContribution)
			
		} else {
			contributionsFromRecentSessions
		}
	}

	private def retrieveContributionsFromRecentSessions = {
		(for {
			coverageSession <- CoverageSessionModel.get.coverageSessionHistory
		} yield createCommandContributionItem(coverageSession)).toArray
	}
	
	private def createCommandContributionItem(coverageSession: CoverageSession) : IContributionItem = {
		val cciParam = basicCommandContributionItemParameter
		
		cciParam.label = coverageSession.displayName
		cciParam.parameters = createSessionParameter(coverageSession.displayName)
		
		new CommandContributionItem(cciParam)
	}
	
	private def createSessionParameter(sessionName: String) = {
		val params = new util.HashMap[String, String]()
		params.put(
				"ecobertura.ui.views.session.commands.selectRecentCoverageSession.session", 
				sessionName)
		params.put("org.eclipse.ui.commands.radioStateParameter", sessionName)
		params
	}
	
	private def createDisabledEmptyContribution : IContributionItem = {
		val cciParam = basicCommandContributionItemParameter
		
		cciParam.label = "<empty>"
		new CommandContributionItem(cciParam) {
			override def isEnabled = false
		}
	}
	
	private def basicCommandContributionItemParameter =
		new CommandContributionItemParameter(
				PlatformUI.getWorkbench().getActiveWorkbenchWindow(), null,
				"ecobertura.ui.views.session.commands.selectRecentCoverageSession", 
				CommandContributionItem.STYLE_RADIO)
}
