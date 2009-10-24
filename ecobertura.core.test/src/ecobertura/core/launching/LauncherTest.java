package ecobertura.core.launching;

import java.io.IOException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ecobertura.core.util.JavaProject;

public class LauncherTest {

	private IWorkspace workspace;
	private JavaProject javaProject;
	
	@Before
	public void setUp() throws Exception {
		workspace = ResourcesPlugin.getWorkspace();
		javaProject = JavaProject.createIn(workspace).named("HelloWorld");
	}

	@After
	public void tearDown() throws Exception {
		javaProject.remove();
	}
	
	@Test
	public void testLaunchCoveredJavaApp() throws CoreException, IOException, OperationCanceledException, InterruptedException {
		ILaunchManager launchMgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType javaLaunchType = launchMgr.getLaunchConfigurationType(IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy configWC = javaLaunchType.newInstance(null, "myLaunchConfig");
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "Sample");
		ILaunchConfiguration config = configWC.doSave();
		config.launch("ecobertura.core.coverageLaunchMode", null);
	}
}
