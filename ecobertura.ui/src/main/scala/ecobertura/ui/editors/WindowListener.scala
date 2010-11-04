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

import org.eclipse.ui._

class WindowListener(editorTracker: EditorsAnnotator) extends IWindowListener {

	val partListener: IPartListener2 = new IPartListener2() {
		override def partOpened(partref: IWorkbenchPartReference) = 
			editorTracker.annotateEditor(partref)
		
		override def partActivated(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partBroughtToTop(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partVisible(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partInputChanged(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partClosed(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partDeactivated(partref: IWorkbenchPartReference) = { /* NOP */ } 
		override def partHidden(partref: IWorkbenchPartReference) = { /* NOP */ } 
	}
	
	override def windowOpened(window: IWorkbenchWindow) =
		window.getPartService.addPartListener(partListener)
	
	override def windowClosed(window: IWorkbenchWindow) =
		window.getPartService.removePartListener(partListener)
		
	override def windowActivated(window: IWorkbenchWindow) = { /* NOP */ }
	override def windowDeactivated(window: IWorkbenchWindow) = { /* NOP */ }
}
