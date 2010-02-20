package ecobertura.ui.launching

import java.util.logging._

import org.eclipse.core.runtime._
import org.eclipse.debug.ui._

object LaunchShortcutRetriever {
	private val logger = Logger getLogger "ecobertura.ui.launching" //$NON-NLS-1$
	
	def fromId(shortcutId: String): ILaunchShortcut = {
		new LaunchShortcutRetriever(shortcutId).getShortcut
	}
}
class LaunchShortcutRetriever(shortcutId: String) {
	import LaunchShortcutRetriever._
	
	def getShortcut = {
		def getShortcutFromExtensionPoint(launchShortcuts: IExtensionPoint) = {
			val shortcut = launchShortcuts.getConfigurationElements find 
					(config => shortcutId.equals(config.getAttribute("id")))
			shortcut match {
				case Some(foundShortcutConfig) => 
					getLaunchShortcutFromConfigurationElement(foundShortcutConfig)
				case None => {
					logger warning String.format(
						"No standard launch shortcut named '%s' found.", shortcutId) //$NON-NLS-1$
					null
				}
			}
		}
		
		def getLaunchShortcutFromConfigurationElement(config: IConfigurationElement) = {
			try {
				config.createExecutableExtension("class").asInstanceOf[ILaunchShortcut] //$NON-NLS-1$
			} catch {
				case e: CoreException => {
					logger.log(Level.WARNING, "unable to create launch shortcut object", e) //$NON-NLS-1$
					null
				}
			}
		}
		
		val launchShortcuts = Platform.getExtensionRegistry.getExtensionPoint(
				IDebugUIConstants.PLUGIN_ID, IDebugUIConstants.EXTENSION_POINT_LAUNCH_SHORTCUTS)
		getShortcutFromExtensionPoint(launchShortcuts)
	}
}
