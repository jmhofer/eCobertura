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
