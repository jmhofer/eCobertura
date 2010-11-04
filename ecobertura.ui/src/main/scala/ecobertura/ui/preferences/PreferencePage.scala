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
