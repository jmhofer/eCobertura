package ecobertura.core.launching;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import ecobertura.core.CorePlugin;
import ecobertura.core.util.JavaProject;

public class LauchnerTest {

	private Bundle core;
	private IWorkspace workspace;
	
	@Before
	public void setUp() throws Exception {
		core = Platform.getBundle(CorePlugin.PLUGIN_ID);
		core.start();
		workspace = ResourcesPlugin.getWorkspace();
	}

	@Test
	public void testCreateProject() throws CoreException, IOException, OperationCanceledException, InterruptedException {
		JavaProject.createIn(workspace).named("HelloWorld");

		// trying out launching...
		ILaunchManager launchMgr = DebugPlugin.getDefault().getLaunchManager();
		Set<String> modes = new HashSet<String>();
		modes.add("ecobertura.core.coverageLaunchMode");
		ILaunchConfigurationType javaLaunchType = launchMgr.getLaunchConfigurationType("org.eclipse.jdt.launching.localJavaApplication");
		ILaunchConfigurationWorkingCopy configWC = javaLaunchType.newInstance(null, "myLaunchConfig");
		System.out.println(configWC.getAttributes().toString());
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "Sample");
		ILaunchConfiguration config = configWC.doSave();
		config.launch("ecobertura.core.coverageLaunchMode", null);
	}

	@Test
	public void testGetProjects() {
		IProject[] projects = workspace.getRoot().getProjects();
		System.out.println("projects:");
		System.out.println(Arrays.toString(projects));
	}
	
	@Test
	public void testSetInitializationData() {
		fail("Not yet implemented");
	}

	@Test
	public void testLaunch() {
		fail("Not yet implemented");
	}

	@Test
	public void testBuildForLaunch() {
		fail("Not yet implemented");
	}

	@Test
	public void testFinalLaunchCheck() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLaunch() {
		fail("Not yet implemented");
	}

	@Test
	public void testPreLaunchCheck() {
		fail("Not yet implemented");
	}

}
