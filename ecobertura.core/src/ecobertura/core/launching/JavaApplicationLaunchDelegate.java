package ecobertura.core.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;

import ecobertura.core.CorePlugin;

public class JavaApplicationLaunchDelegate implements
		ILaunchConfigurationDelegate2, IExecutableExtension {

	private ILaunchConfigurationDelegate2 delegateToExtend; 
	
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {
		delegateToExtend = LaunchDelegateRetriever.fromExtensionConfig(config).getDelegate();
	}
	
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
	    // TODO monitor
		
		final LaunchInstrumenter launchInstrumenter = 
			LaunchInstrumenter.instrumentClassesFor(configuration);
		final ILaunchConfiguration updatedConfiguration = 
			launchInstrumenter.getUpdatedLaunchConfiguration();
		CorePlugin.instance().coverageResultsCollector().coveredLaunchStarted(updatedConfiguration);
		delegateToExtend.launch(updatedConfiguration, ILaunchManager.RUN_MODE, launch, monitor);
	}

	@Override
	public boolean buildForLaunch(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {

		// simply forward for now
		return delegateToExtend.buildForLaunch(configuration, mode, monitor);
	}

	@Override
	public boolean finalLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		
		// simply forward for now
		return delegateToExtend.finalLaunchCheck(configuration, mode, monitor);
	}

	@Override
	public ILaunch getLaunch(ILaunchConfiguration configuration, String mode)
			throws CoreException {
		
		// simply forward for now
		return delegateToExtend.getLaunch(configuration, mode);
	}

	@Override
	public boolean preLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		
		// simply forward for now
		return delegateToExtend.preLaunchCheck(configuration, mode, monitor);
	}
}
