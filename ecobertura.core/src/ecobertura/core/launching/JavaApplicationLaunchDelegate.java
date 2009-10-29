package ecobertura.core.launching;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate2;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
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
		ILaunchConfiguration modifiedConfiguration = addCoberturaToClasspath(configuration);
		modifiedConfiguration = addDatafileSystemProperty(modifiedConfiguration);
		
		delegateToExtend.launch(modifiedConfiguration, ILaunchManager.RUN_MODE, launch, monitor);
	}

	// TODO clean this up! move it to its own class
	private void instrumentClasspath(final ILaunchConfiguration configuration) 
			throws CoreException {
		
	    for (IRuntimeClasspathEntry classpathEntry : resolvedClasspathEntries(configuration)) {
	    	if (classpathEntry.getClasspathProperty() != IRuntimeClasspathEntry.USER_CLASSES) {
		    	logger.fine(String.format("skipping %s", classpathEntry.getLocation()));
	    		continue;
	    	}
	    	// FIXME instruments too much? - instruments jar files, too?
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

	// FIXME this doesn't work because the cobertura.jar lives in another classloader, presumably
	@SuppressWarnings("unchecked")
	private ILaunchConfiguration addCoberturaToClasspath(ILaunchConfiguration configuration) 
			throws CoreException {
		
		final ILaunchConfigurationWorkingCopy configWC = configuration.getWorkingCopy();
		final List<String> classpathEntries = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, Collections.EMPTY_LIST);
		logger.fine("old classpath == " + classpathEntries);
		final IPath pathToCoberturaJar = CoberturaWrapper.get().pathToJar();
		final IRuntimeClasspathEntry coberturaEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(
				pathToCoberturaJar);
		classpathEntries.add(coberturaEntry.getMemento());
		logger.fine("new classpath == " + classpathEntries);
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpathEntries);
		
		return configWC.doSave();
	}

	private ILaunchConfiguration addDatafileSystemProperty(
			final ILaunchConfiguration configuration) throws CoreException {
		
		final ILaunchConfigurationWorkingCopy configWC = configuration.getWorkingCopy();
		final String currentVMArguments = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
				// TODO use the proper plugin subdirectory here
				currentVMArguments + " -Dnet.sourceforge.cobertura.datafile=cobertura.ser ");
		return configWC.doSave();
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
