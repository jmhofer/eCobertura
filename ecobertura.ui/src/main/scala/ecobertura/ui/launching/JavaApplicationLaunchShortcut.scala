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
