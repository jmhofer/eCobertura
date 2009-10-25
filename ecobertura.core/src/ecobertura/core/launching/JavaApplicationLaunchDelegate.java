package ecobertura.core.launching;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import ecobertura.core.cobertura.CoberturaWrapper;

public class JavaApplicationLaunchDelegate implements
		ILaunchConfigurationDelegate2, IExecutableExtension {

	private static final Logger logger = Logger.getLogger("ecobertura.core.launching");

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
		instrumentClasspath(configuration);
		// simply forward for now
		delegateToExtend.launch(configuration, ILaunchManager.RUN_MODE, launch, monitor);
	}

	// TODO clean this up! move it to its own class
	private void instrumentClasspath(final ILaunchConfiguration configuration) 
			throws CoreException {
		
	    for (IRuntimeClasspathEntry classpathEntry : resolvedClasspathEntries(configuration)) {
	    	if (classpathEntry.getClasspathProperty() != IRuntimeClasspathEntry.USER_CLASSES) {
		    	logger.fine(String.format("skipping %s", classpathEntry.getLocation()));
	    		continue;
	    	}
	    	final String userClasspath = classpathEntry.getLocation();
	    	logger.fine(String.format("instrumenting classes within %s", userClasspath));
	    	instrumentFilesWithin(new File(userClasspath));
	    }
	    
 	}

	private IRuntimeClasspathEntry[] resolvedClasspathEntries(
			final ILaunchConfiguration configuration) throws CoreException {
		
		IRuntimeClasspathEntry[] unresolvedClasspathEntries = 
	    	JavaRuntime.computeUnresolvedRuntimeClasspath(configuration);
	    IRuntimeClasspathEntry[] resolvedClasspathEntries = 
	    	JavaRuntime.resolveRuntimeClasspath(unresolvedClasspathEntries, configuration);
	    
		return resolvedClasspathEntries;
	}

	private void instrumentFilesWithin(final File file) {
		if (file.isDirectory()) {
			for (final File subFile : file.listFiles()) {
				instrumentFilesWithin(subFile);
			}
		} else {
			instrumentClassFile(file);
		}
	}

	private void instrumentClassFile(File file) {
    	logger.fine(String.format("instrumenting %s", file.getPath()));
    	CoberturaWrapper.get().instrumentClassFile(file);
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
