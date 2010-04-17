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
import org.eclipse.ui.part.FileEditorInput

import java.util.logging.Logger
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ISelection
import org.eclipse.core.commands._
import org.eclipse.ui.PlatformUI
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.core.data.CoverageSession

import ecobertura.ui.views.session.CoverageSessionView
import ecobertura.ui.views.session._
import ecobertura.ui.util._

class OpenCoveredClassHandler extends AbstractHandler {
	val logger = Logger.getLogger("ecobertura.ui.views.session.command")
	
	override def execute(event: ExecutionEvent) = {
		val window = HandlerUtil.getActiveWorkbenchWindow(event)
		val page = window.getActivePage
		val view = page.findView(CoverageSessionView.ID).asInstanceOf[CoverageSessionView];
				
		view.selection match {
			case structuredSelection: IStructuredSelection =>
				handleStructuredSelection(structuredSelection.getFirstElement)
			case _ => logger.fine("not a structured selection") /* nothing to do */
		}
		
		def handleStructuredSelection(selectedObject: Any) = selectedObject match {
			case covClass: CoverageSessionClass => handleClassSelection(covClass)
			case _ => logger.fine("not a CoverageSessionClass") /* nothing to do */
		}

		def handleClassSelection(covClass: CoverageSessionClass) = {
			SourceFileFinder.fromSourceFileName(covClass.coverageData.sourceFileName).find(
					file => {
						val defaultEditor = PlatformUI.getWorkbench.getEditorRegistry.getDefaultEditor(file.getName);
						page.openEditor(new FileEditorInput(file), defaultEditor.getId());
					}
			)
		}
		null
	}
}
