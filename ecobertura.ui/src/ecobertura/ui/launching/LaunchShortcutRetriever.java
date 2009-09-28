package ecobertura.ui.launching;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut;

public class LaunchShortcutRetriever {
	private static final Logger logger = Logger.getLogger("ecobertura.ui.launching"); //$NON-NLS-1$
	
	private String shortcutId;
	
	static ILaunchShortcut fromId(String shortcutId) {
		return new LaunchShortcutRetriever(shortcutId).getShortcut();
	}
	
	private LaunchShortcutRetriever(String shortcutId) {
		this.shortcutId = shortcutId;
	}

	private ILaunchShortcut getShortcut() {
		IExtensionPoint launchShortcuts = Platform.getExtensionRegistry().getExtensionPoint(
				IDebugUIConstants.PLUGIN_ID, IDebugUIConstants.EXTENSION_POINT_LAUNCH_SHORTCUTS);
		return getShortcutFromExtensionPoint(launchShortcuts);
	}

	private ILaunchShortcut getShortcutFromExtensionPoint(IExtensionPoint launchShortcuts) {
		for (IConfigurationElement config : launchShortcuts.getConfigurationElements()) {
			if (shortcutId.equals(config.getAttribute("id"))) { //$NON-NLS-1$
				return getLaunchShortcutFromConfigurationElement(config);
			}
		}
		logger.warning(String.format("No standard launch shortcut named '%s' found.", shortcutId)); //$NON-NLS-1$
		return null;
	}

	private ILaunchShortcut getLaunchShortcutFromConfigurationElement(
			IConfigurationElement config) {
		try {
			return (ILaunchShortcut) config.createExecutableExtension("class"); //$NON-NLS-1$
			
		} catch (CoreException e) {
			logger.log(Level.WARNING, "unable to create launch shortcut object", e); //$NON-NLS-1$
			return null;
		}
	}
}
