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

import org.eclipse.core.commands._
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.core.cobertura.CoberturaWrapper
import ecobertura.core.data.CoverageSession
import ecobertura.ui.views.session.CoverageSessionModel

class OpenCoverageSessionHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val sessionFilename = retrieveCoverageSessionFilename(event)
		val projectData = CoberturaWrapper.get.projectDataFromFile(sessionFilename)
		CoverageSessionModel.get.addCoverageSession(
				CoverageSession.fromCoberturaProjectData(projectData))
		
		null // handlers must return null
	}
	
	private def retrieveCoverageSessionFilename(event: ExecutionEvent) = {
		val parentShell = HandlerUtil.getActiveShell(event)
		val dialog = new FileDialog(parentShell, SWT.OPEN)
		dialog.setFilterNames(Array("Cobertura Session Files (*.ser)", "All Files (*)"))
		dialog.setFilterExtensions(Array("*.ser", "*"))
		
		dialog.open
	}
}
