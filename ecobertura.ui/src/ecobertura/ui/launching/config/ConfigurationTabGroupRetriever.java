package ecobertura.ui.launching.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class ConfigurationTabGroupRetriever {
	
	private static final Logger logger = Logger.getLogger("ecobertura.ui.launching.config"); //$NON-NLS-1$
	
	final IConfigurationElement config;
	String launchTypeId;
	
	static ILaunchConfigurationTabGroup fromConfig(IConfigurationElement config) {
		return new ConfigurationTabGroupRetriever(config).getConfigurationTabGroup();
	}

	private ConfigurationTabGroupRetriever(IConfigurationElement config) {
		this.config = config;
	}
	
	private ILaunchConfigurationTabGroup getConfigurationTabGroup() {
		launchTypeId = config.getAttribute("type"); //$NON-NLS-1$
		IExtensionPoint launchConfigTabGroups = Platform.getExtensionRegistry().getExtensionPoint(
				IDebugUIConstants.PLUGIN_ID, 
				IDebugUIConstants.EXTENSION_POINT_LAUNCH_CONFIGURATION_TAB_GROUPS);
		return getTabGroupFromExtensionPoint(launchConfigTabGroups);
	}

	private ILaunchConfigurationTabGroup getTabGroupFromExtensionPoint(
			IExtensionPoint launchConfigTabGroups) {
		for (IConfigurationElement delegateConfig : launchConfigTabGroups.getConfigurationElements()) {
			if (hasCorrectType(delegateConfig) && hasRunMode(delegateConfig)) {
				return getTabGroupFromConfigurationElement(delegateConfig);
			}
		}
		logger.warning(String.format(
				"No standard launch configuration tab group for launch type '%s' found.", //$NON-NLS-1$ 
				launchTypeId)); 
		return null;
	}

	private boolean hasCorrectType(IConfigurationElement delegateConfig) {
		return launchTypeId.equals(delegateConfig.getAttribute("type")); //$NON-NLS-1$
	}

	private boolean hasRunMode(IConfigurationElement delegateConfig) {
		for (IConfigurationElement launchModeConfig : delegateConfig.getChildren("launchMode")) { //$NON-NLS-1$
			if (ILaunchManager.RUN_MODE.equals(launchModeConfig.getAttribute("mode"))) { //$NON-NLS-1$
				return true;
			}
		}
		return false;
	}

	private ILaunchConfigurationTabGroup getTabGroupFromConfigurationElement(
			IConfigurationElement delegateConfig) {
		try {
			return (ILaunchConfigurationTabGroup) delegateConfig.createExecutableExtension("class"); //$NON-NLS-1$
			
		} catch (CoreException e) {
			logger.log(Level.WARNING, "unable to create launch configuration tab group", e); //$NON-NLS-1$
			return null;
		}
	}
}
