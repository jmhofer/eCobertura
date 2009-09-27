package ecobertura.core.launching;

import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;


import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchDelegate;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;

import ecobertura.core.CorePlugin;

class LaunchDelegateRetriever {
	private static final Logger logger = Logger.getLogger("ecobertura.core.launching"); //$NON-NLS-1$
	
	private IConfigurationElement config;
	
	static LaunchDelegateRetriever fromExtensionConfig(IConfigurationElement config) {
		return new LaunchDelegateRetriever(config);
	}
	
	private LaunchDelegateRetriever(IConfigurationElement config) {
		this.config = config;
	}
	
	ILaunchConfigurationDelegate2 getDelegate() throws CoreException {
		String launchTypeName = config.getAttribute("type"); //$NON-NLS-1$
		ILaunchConfigurationType type = getLaunchConfigurationType(launchTypeName);
		return getLaunchDelegateFor(type);
	}

	private ILaunchConfigurationType getLaunchConfigurationType(String launchTypeName) throws CoreException {
		ILaunchConfigurationType type = 
			DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(launchTypeName);
		if (type == null) {
			throw new CoreException(new Status(Status.ERROR, CorePlugin.PLUGIN_ID, 
					String.format("unknown launch configuration type %s", launchTypeName))); //$NON-NLS-1$
		}
		return type;
	}

	private ILaunchConfigurationDelegate2 getLaunchDelegateFor(ILaunchConfigurationType type) throws CoreException {
		ILaunchDelegate[] delegatesForType = type.getDelegates(new HashSet<String>(
				Arrays.asList(ILaunchManager.RUN_MODE)));
		if (delegatesForType.length == 0) {
			throw new CoreException(new Status(Status.ERROR, CorePlugin.PLUGIN_ID, 
					String.format("no delegate for %s found", type.getName()))); //$NON-NLS-1$
		}
		if (delegatesForType.length > 1) {
			logger.warning(String.format("more than one launch delegate found for %s",  //$NON-NLS-1$
					type.getName()));
		}
		return convertToType2Delegate(delegatesForType[0].getDelegate());
	}

	private ILaunchConfigurationDelegate2 convertToType2Delegate(
			ILaunchConfigurationDelegate delegate) {
		
		if (delegate instanceof ILaunchConfigurationDelegate2) {
			return (ILaunchConfigurationDelegate2) delegate;
		}
		return adaptToType2Delegate(delegate);
	}

	private ILaunchConfigurationDelegate2 adaptToType2Delegate(
			final ILaunchConfigurationDelegate delegate) {
		
		return new ILaunchConfigurationDelegate2() {
			@Override
			public boolean buildForLaunch(ILaunchConfiguration configuration,
					String mode, IProgressMonitor monitor) throws CoreException {
				return true;
			}

			@Override
			public boolean finalLaunchCheck(ILaunchConfiguration configuration,
					String mode, IProgressMonitor monitor) throws CoreException {
				return true;
			}

			@Override
			public ILaunch getLaunch(ILaunchConfiguration configuration,
					String mode) throws CoreException {
				return null;
			}

			@Override
			public boolean preLaunchCheck(ILaunchConfiguration configuration,
					String mode, IProgressMonitor monitor) throws CoreException {
				return true;
			}

			@Override
			public void launch(ILaunchConfiguration configuration, String mode,
					ILaunch launch, IProgressMonitor monitor)
					throws CoreException {
				delegate.launch(configuration, mode, launch, monitor);
			}
		};
	}


}
