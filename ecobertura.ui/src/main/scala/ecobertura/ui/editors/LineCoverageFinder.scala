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
package ecobertura.ui.editors

import java.util.logging.Logger

import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.jdt.core.ITypeRoot
import org.eclipse.jdt.ui._

import ecobertura.core.data._

object LineCoverageFinder {
	val logger = Logger.getLogger("ecobertura.ui.editors")
	
	def forSession(session: CoverageSession) = new LineCoverageFinder(session)
}

class LineCoverageFinder(session: CoverageSession) {
	import LineCoverageFinder.logger
	
	def findInEditor(editor: ITextEditor): List[LineCoverage] = {
		JavaUI.getEditorInputTypeRoot(editor.getEditorInput) match {
			case typeRoot: ITypeRoot => findForTypeRoot(typeRoot) 
			case _ => Nil
		}
	}
	
	private def findForTypeRoot(typeRoot: ITypeRoot) = {
		val sourceFileName = typeRoot.getElementName
		val packageName = typeRoot.getParent.getElementName
		
		def findInPackage(packageCov: PackageCoverage) = {
			packageCov.sourceFileLines.get(sourceFileName) match {
				case Some(lines) => {
					logger.fine("found lines: " + lines.mkString(", "))
					lines
				}
				case None => Nil
			}
		}
		
		session.packageMap.get(packageName) match {
			case Some(packageCov) => findInPackage(packageCov)
			case None => Nil
		}
	}
}