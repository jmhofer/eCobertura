package ecobertura.core.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;

public class JavaApplicationLaunchConfiguration {

	private final JavaProject javaProject;
	
	public static JavaApplicationLaunchConfiguration forProject(final JavaProject javaProject) 
			throws CoreException {
		
		return new JavaApplicationLaunchConfiguration(javaProject);
	}

	private JavaApplicationLaunchConfiguration(final JavaProject javaProject) throws CoreException {
		this.javaProject = javaProject;
	}

	public ILaunchConfiguration createForMainType(final String mainTypeName) throws CoreException {
		ILaunchConfigurationWorkingCopy configWC = retrieveJavaApplicationConfigurationWorkingCopy();
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainTypeName);
		updateClasspath(configWC);
		return configWC.doSave();
	}

	private ILaunchConfigurationWorkingCopy retrieveJavaApplicationConfigurationWorkingCopy()
			throws CoreException {
		ILaunchManager launchMgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType javaLaunchType = launchMgr.getLaunchConfigurationType(
				IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy configWC = javaLaunchType.newInstance(
				null, "sampleLaunchConfig");
		return configWC;
	}

	// FIXME this doesn't work for some reason...
	@SuppressWarnings("unchecked")
	private void updateClasspath(ILaunchConfigurationWorkingCopy configWC) throws CoreException {
		final List<String> classpath = configWC.getAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, new ArrayList<String>());
		classpath.add(javaProject.defaultClasspath().getMemento());
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
	}

	
}
