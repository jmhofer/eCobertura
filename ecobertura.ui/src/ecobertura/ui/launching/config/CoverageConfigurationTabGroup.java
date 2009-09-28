package ecobertura.ui.launching.config;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class CoverageConfigurationTabGroup 
		implements ILaunchConfigurationTabGroup, IExecutableExtension {

	ILaunchConfigurationTabGroup tabGroupToExtend;
	boolean configDelegationPossible = false;
	
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		tabGroupToExtend = ConfigurationTabGroupRetriever.fromConfig(config);
		configDelegationPossible = (tabGroupToExtend != null);
	}

	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		if (configDelegationPossible) {
			tabGroupToExtend.createTabs(dialog, ILaunchManager.RUN_MODE);
		}
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabGroupToExtend.getTabs();
	}

	@Override
	public void initializeFrom(ILaunchConfiguration config) {
		tabGroupToExtend.initializeFrom(config);
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configWC) {
		tabGroupToExtend.setDefaults(configWC);
	}
	
	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configWC) {
		tabGroupToExtend.performApply(configWC);
	}

	@Override
	public void dispose() {
		tabGroupToExtend.dispose();
	}

	@Override
	public void launched(ILaunch launch) {
		/* deprecated, ignore */
	}
}
