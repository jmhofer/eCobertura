package ecobertura.core.launching;

import static org.junit.Assert.*;

import java.util.Arrays;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
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
	public void testCreateProject() throws CoreException {
		JavaProject.createIn(workspace).named("HelloWorld");
	}

	@Test
	public void testGetProjects() {
		IProject[] projects = workspace.getRoot().getProjects();
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
