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
package ecobertura.ui.launching

import org.eclipse.core.runtime._
import org.eclipse.debug.ui.ILaunchShortcut
import org.eclipse.jface.viewers.ISelection
import org.eclipse.ui.IEditorPart

class JavaApplicationLaunchShortcut extends ILaunchShortcut with IExecutableExtension {
	private var shortcutToExtend : Option[ILaunchShortcut] = None
	
	override def setInitializationData(config: IConfigurationElement, propertyName: String, 
			data: Any) = {
		val retrievedShortcut = LaunchShortcutRetriever.fromId(data.asInstanceOf[String]) 
		if (retrievedShortcut != null) 
			shortcutToExtend = Some(retrievedShortcut) 
	}
	
	override def launch(selection: ISelection, mode: String) = shortcutToExtend match {
		case Some(shortcut) => shortcut.launch(selection, mode)
		case _ => // do nothing
	}
	
	override def launch(editor: IEditorPart, mode: String) = shortcutToExtend match {
		case Some(shortcut) => shortcut.launch(editor, mode)
		case _ => // do nothing
	}
}
