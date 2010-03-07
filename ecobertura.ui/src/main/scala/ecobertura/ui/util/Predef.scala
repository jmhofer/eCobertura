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
package ecobertura.ui.util

import org.eclipse.jface.text.DocumentEvent
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.jface.viewers._
import org.eclipse.jface.action._

object Predef {
	implicit def actionFunction2Action(func: => Unit) = new Action {
		override def run = func
	}
	
	implicit def documentListener(func: => Unit) = new IDocumentListener {
		override def documentChanged(event: DocumentEvent) = func
		override def documentAboutToBeChanged(event: DocumentEvent) = { /* nothing to do */ }
	}
	
	implicit def menuListener(func: IMenuManager => Unit) = new IMenuListener {
		override def menuAboutToShow(menuManager: IMenuManager) = func(menuManager)
	}
	
	implicit def doubleClickListener(func: DoubleClickEvent => Unit) = new IDoubleClickListener {
		override def doubleClick(event: DoubleClickEvent) = func(event)
	}
	
	implicit def run(taskToRun: => Unit) = new Runnable() {
			override def run = taskToRun
	}
}