package ecobertura.core.launching;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
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

	private IRuntimeClasspathEntry[] resolvedClasspathEntries() throws CoreException {
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

	@SuppressWarnings("unchecked")
	private void addCoberturaToClasspath() throws CoreException {
		final List<String> classpathEntries = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, Collections.EMPTY_LIST);
		final IPath pathToCoberturaJar = CoberturaWrapper.get().pathToJar();
		final IRuntimeClasspathEntry coberturaEntry = JavaRuntime.newArchiveRuntimeClasspathEntry(
				pathToCoberturaJar);
		classpathEntries.add(coberturaEntry.getMemento());
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpathEntries);
	}

	private void addDatafileSystemProperty() throws CoreException {
		final String currentVMArguments = configWC.getAttribute(
				IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, "");
		final File coberturaFile = new File(
				CorePlugin.instance().pluginState().instrumentationDataDirectory(), 
				ICoberturaWrapper.DEFAULT_COBERTURA_FILENAME);
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, 
				 String.format("%s -D%s=%s ", currentVMArguments, COBERTURA_DATAFILE_PROPERTY, 
						 coberturaFile.getAbsolutePath()));
	}
	
	ILaunchConfiguration getUpdatedLaunchConfiguration() throws CoreException {
		return configWC.doSave();
	}
}
