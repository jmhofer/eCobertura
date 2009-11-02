package ecobertura.core.launching;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.ProjectData;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ecobertura.core.util.JavaApplicationLaunchConfiguration;
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
		final ILaunchConfiguration config = 
			JavaApplicationLaunchConfiguration.forProject(javaProject).createForMainType("Sample");
		final LaunchTracker tracker = LaunchTracker.prepareLaunch();
		config.launch("ecobertura.core.coverageLaunchMode", null);
		final ProjectData collectedCoverageData = tracker.waitForAndRetrieveCoverageResults(); 
		checkCoverageResults(collectedCoverageData);
	}

	private void checkCoverageResults(final ProjectData projectData) {
		for (Object classDataObj : projectData.getClasses()) {
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
