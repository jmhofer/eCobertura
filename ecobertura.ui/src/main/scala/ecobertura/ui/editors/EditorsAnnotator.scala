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
package ecobertura.ui.editors

import java.util.logging.Logger

import org.eclipse.ui._
import org.eclipse.ui.texteditor.ITextEditor

import ecobertura.ui.annotation.CoverageAnnotationModel

object EditorsAnnotator {
	private val logger = Logger.getLogger("ecobertura.ui.editors") //$NON-NLS-1$
	
	def trackEditorsOf(workbench: IWorkbench) = new EditorsAnnotator(workbench)
}

class EditorsAnnotator(workbench: IWorkbench) {
	private val windowListener = new WindowListener(this)
	
	addListenersToEditorWindows
	annotateAllEditors
	EditorsAnnotator.logger.fine("EditorsAnnotator registered.") //$NON-NLS-1$
	
	private def addListenersToEditorWindows = {
		workbench.getWorkbenchWindows foreach {
			_.getPartService.addPartListener(windowListener.partListener)
		}
		workbench.addWindowListener(windowListener)
	}
	
	private def annotateAllEditors = {
		workbench.getWorkbenchWindows foreach (annotateEditorsOfWindow(_))
		
		def annotateEditorsOfWindow(window: IWorkbenchWindow) =
			window.getPages foreach (annotateEditorsOfPage(_))
		
		def annotateEditorsOfPage(page: IWorkbenchPage) =
			page.getEditorReferences foreach (annotateEditor(_))
	}

	def annotateEditor(partref: IWorkbenchPartReference) = {
		val part = partref.getPart(false)
		part match {
			case editor: ITextEditor => CoverageAnnotationModel.attachTo(editor)
			case _ => /* nothing to do */
		}
	}
	
	def dispose = {
		workbench.removeWindowListener(windowListener)
		workbench.getWorkbenchWindows foreach {
			_.getPartService.removePartListener(windowListener.partListener)
		}
	}
}