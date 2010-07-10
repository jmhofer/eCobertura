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
package ecobertura.ui.views.session

import java.util.logging.Logger

import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.ITreeContentProvider

import ecobertura.core.data._
import ecobertura.ui.UIPlugin

object CoverageSessionModel {
	private val logger = Logger.getLogger("ecobertura.ui.views.session") //$NON-NLS-1$
	
	private var instance = new CoverageSessionModel

	def get = instance
}

class CoverageSessionModel extends CoverageSessionResetPublisher with ITreeContentProvider {
	import CoverageSessionModel.logger
	
	private var internalCoverageSessionHistory = List[CoverageSession]()
	private var selectedCoverageSession: Option[CoverageSession] = None
	
	def clearHistory = {
		internalCoverageSessionHistory = List()
		buildFromSession
		fireSessionReset(None)
	}
	
	def addCoverageSession(coverageSession: CoverageSession) = {
		internalCoverageSessionHistory = (coverageSession :: internalCoverageSessionHistory) take 
				UIPlugin.instance.preferences.coverageSessionHistorySize
		selectedCoverageSession = Some(coverageSession)
		logger.fine("history: " + internalCoverageSessionHistory.mkString(", "))
		buildFromSession
		
		fireSessionReset(selectedCoverageSession)
	}
	
	def selectCoverageSession(displayNameOfSelectedSession: String) = {
		internalCoverageSessionHistory.find(_.displayName == displayNameOfSelectedSession) match {
			case Some(coverageSession) => {
				selectedCoverageSession = Some(coverageSession)
				buildFromSession
				fireSessionReset(selectedCoverageSession)
			}
			case None => /* nothing to do */
		}
	}
	
	def currentCoverageSession = selectedCoverageSession
	def coverageSessionHistory = internalCoverageSessionHistory
	
	def buildFromSession = {
		logger.fine("Building from coverage session...")
		CoverageSessionRoot.removeAllChildren
		currentCoverageSession match {
			case Some(session) => {
				val covAllPackages = new CoverageSessionAllPackages(session)
				CoverageSessionRoot.addChild(covAllPackages)
				session.packages.foreach { covPackage =>
					covAllPackages.addChild(buildFromPackageCoverage(covPackage))
				}
			}
			case None => /* nothing to do */ 
		}
	}
	
	def buildFromPackageCoverage(covPackage: PackageCoverage) = {
		val sessionPackage = new CoverageSessionPackage(covPackage)
		logger.fine("Building package from coverage session..." + sessionPackage.name)
		covPackage.classes.foreach { covClass =>
			sessionPackage.addChild(new CoverageSessionClass(covClass))
			logger.fine("... adding class " + covClass.name)
		}
		sessionPackage
	}
	
	override def getElements(element: Any) : Array[AnyRef] = getChildren(element)
	override def inputChanged(viewer: Viewer, arg0: Any, arg1: Any) = { /* no changes allowed yet */ }
	override def dispose = { /* nothing to dispose of right now */ }
	
	override def getChildren(parentElement: Any) : Array[AnyRef] = parentElement match {
		case node: CoverageSessionTreeNode => node.children.toArray
		case _ => Array()
	}
	
	override def getParent(element: Any) : AnyRef = element match {
		case node: CoverageSessionTreeNode => node.parent.getOrElse(null)
		case _ => null
	}
	
	override def hasChildren(element: Any) : Boolean = element match {
		case node: CoverageSessionTreeNode => node.hasChildren
		case _ => false
	}
}
