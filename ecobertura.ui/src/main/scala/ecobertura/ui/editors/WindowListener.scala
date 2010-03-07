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
