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
package ecobertura.ui.preferences

import org.eclipse.jface.preference._
import org.eclipse.ui.IWorkbenchPreferencePage
import org.eclipse.ui.IWorkbench
import ecobertura.ui._

class PreferencePage extends FieldEditorPreferencePage(FieldEditorPreferencePage.GRID) 
		with IWorkbenchPreferencePage {
	setPreferenceStore(UIPlugin.instance.getPreferenceStore)
	setDescription("Preferences for eCobertura")

	override def createFieldEditors = {
		addField(new IntegerFieldEditor(
				PluginPreferences.sessionHistorySize,
				"Coverage Session &History Size",
				getFieldEditorParent))
	}
	
	override def init(workbench: IWorkbench) = {}
}
