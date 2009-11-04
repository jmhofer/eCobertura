package ecobertura.core.launching;

import java.io.File;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import ecobertura.core.CorePlugin;
import ecobertura.core.cobertura.CoberturaWrapper;
import ecobertura.core.cobertura.ICoberturaWrapper;

public final class LaunchInstrumenter {
	public static final String COBERTURA_DATAFILE_PROPERTY = "net.sourceforge.cobertura.datafile"; 
	private static final Logger logger = Logger.getLogger("ecobertura.core.launching");

	private final ILaunchConfiguration configuration;
	private final ILaunchConfigurationWorkingCopy configWC;
	
	static LaunchInstrumenter instrumentClassesFor(final ILaunchConfiguration configuration) 
			throws CoreException {
		
		return new LaunchInstrumenter(configuration);
	}

	private LaunchInstrumenter(final ILaunchConfiguration configuration) throws CoreException {
		this.configuration = configuration;
		configWC = configuration.getWorkingCopy();
		
		instrumentClasspath();
		addCoberturaToClasspath();
		addDatafileSystemProperty();
	}
	
	private void instrumentClasspath() 
			throws CoreException {
		
	    for (IRuntimeClasspathEntry classpathEntry : resolvedClasspathEntries()) {
	    	if (containsUserClassesFromProject(classpathEntry)) {
		    	final String userClasspath = classpathEntry.getLocation();
		    	logger.fine(String.format("instrumenting classes within %s", userClasspath));
		    	instrumentFilesWithin(new File(userClasspath));
	    		
	    	} else {
		    	logger.fine(String.format("skipping %s", classpathEntry.getLocation()));
	    	}
	    }
	    
 	}

	private IRuntimeClasspathEntry[] resolvedClasspathEntries() throws CoreException {
		IRuntimeClasspathEntry[] unresolvedClasspathEntries = 
	    	JavaRuntime.computeUnresolvedRuntimeClasspath(configuration);
	    IRuntimeClasspathEntry[] resolvedClasspathEntries = 
	    	JavaRuntime.resolveRuntimeClasspath(unresolvedClasspathEntries, configuration);
	    
		return resolvedClasspathEntries;
	}

	private boolean containsUserClassesFromProject(final IRuntimeClasspathEntry entry) {
		return entry.getClasspathProperty() == IRuntimeClasspathEntry.USER_CLASSES
				&& entry.getType() == IRuntimeClasspathEntry.PROJECT;
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

	private void addCoberturaToClasspath() throws CoreException {
	    configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
	            InstrumentedClasspathProvider.ID);
	    InstrumentedClasspathProvider.wrap(JavaRuntime.getClasspathProvider(configuration));
	}

	private void addDatafileSystemProperty() throws CoreException {
		final File coberturaFile = new File(
				CorePlugin.instance().pluginState().instrumentationDataDirectory(), 
				ICoberturaWrapper.DEFAULT_COBERTURA_FILENAME);
		final String coberturaVMArgument = String.format("-D%s=%s ", COBERTURA_DATAFILE_PROPERTY, 
				 coberturaFile.getAbsolutePath());
		final String currentVMArguments = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
		if (requiresCoberturaVMArgument(currentVMArguments, coberturaVMArgument)) {
			configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
					 String.format("%s %s ", currentVMArguments, coberturaVMArgument));
		}
	}

	private boolean requiresCoberturaVMArgument(
			final String currentVMArguments, final String coberturaVMArgument) {
		return currentVMArguments.indexOf(coberturaVMArgument) == -1;
	}
	
	ILaunchConfiguration getUpdatedLaunchConfiguration() throws CoreException {
		return configWC;
	}
}
