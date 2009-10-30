package ecobertura.core.launching;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;

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

import ecobertura.core.cobertura.CoberturaWrapper;
import ecobertura.core.cobertura.ICoberturaWrapper;
import ecobertura.core.util.JavaProject;
import ecobertura.core.util.LaunchTracker;

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
		final ILaunchConfiguration config = createJavaLaunchConfigurationForSample();
		final LaunchTracker launchTracker = LaunchTracker.prepareLaunch();
		config.launch("ecobertura.core.coverageLaunchMode", null);
		launchTracker.waitForLaunchTermination();
		checkCoverageResults();
	}

	private ILaunchConfiguration createJavaLaunchConfigurationForSample() throws CoreException {
		ILaunchConfigurationWorkingCopy configWC = retrieveJavaApplicationConfigurationWorkingCopy();
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, "Sample");
		updateClasspath(configWC);
		return configWC.doSave();
	}

	private ILaunchConfigurationWorkingCopy retrieveJavaApplicationConfigurationWorkingCopy()
			throws CoreException {
		ILaunchManager launchMgr = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType javaLaunchType = launchMgr.getLaunchConfigurationType(
				IJavaLaunchConfigurationConstants.ID_JAVA_APPLICATION);
		ILaunchConfigurationWorkingCopy configWC = javaLaunchType.newInstance(
				null, "myLaunchConfig");
		return configWC;
	}

	private void updateClasspath(ILaunchConfigurationWorkingCopy configWC) throws CoreException {
		List<String> classpath = new ArrayList<String>();
		classpath.add(javaProject.defaultClasspath().getMemento());
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		configWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, classpath);
	}

	private void checkCoverageResults() {
		for (Object classDataObj : CoberturaWrapper.get().projectDataFromFile(
				ICoberturaWrapper.DEFAULT_COBERTURA_FILENAME).getClasses()) {
			
			if (!(classDataObj instanceof ClassData)) {
				continue;
			}
			checkSampleClassData((ClassData) classDataObj);
		}
	}

	private void checkSampleClassData(ClassData classData) {
		assertEquals("Sample", classData.getName());
		assertEquals("expecting 8 covered lines in Sample class", 
				8, classData.getNumberOfCoveredLines());
		
		checkLines(classData);
	}

	private void checkLines(ClassData classData) {
		for (Object lineDataObj : classData.getLines()) {
			if (!(lineDataObj instanceof LineData)) {
				continue;
			}
			LineData lineData = (LineData) lineDataObj;
			assertEquals("expecting each line to have been covered exactly once", 
					1, lineData.getHits());
			// TODO why is lineData.getMethodName() null?
		}
	}
}
