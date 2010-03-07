/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
 */
package ecobertura.core.launching

import java.io.IOException

import net.sourceforge.cobertura.coveragedata._

import org.eclipse.core.resources.IWorkspace
import org.eclipse.core.resources.ResourcesPlugin
import org.eclipse.core.runtime.CoreException
import org.eclipse.core.runtime.OperationCanceledException
import org.eclipse.debug.core.ILaunchConfiguration

import org.junit._
import org.junit.Assert._

import ecobertura.core.util._

class LauncherTest {
	private var workspace: IWorkspace = null
	private var javaProject: JavaProject = null
	
	@Before
	def setUp = {
		workspace = ResourcesPlugin.getWorkspace
		javaProject = (JavaProject createIn workspace) named "HelloWorld"
	}
	
	@After
	def tearDown = javaProject.remove
	
/* FIXME test not working, and using old api...
	@Test
	def testLaunchCoveredJavaApp = {
		val config = JavaApplicationLaunchConfiguration forProject javaProject createForMainType "Sample"
		val tracker = LaunchTracker.prepareLaunch
		config launch ("ecobertura.core.coverageLaunchMode", null)
		val collectedCoverageData = tracker.waitForAndRetrieveCoverageResults
		checkCoverageResults(collectedCoverageData)
	}
	
	private def checkCoverageResults(projectData: ProjectData) = {
		checkClassesInProject(projectData)
		checkSourceFilesInProject(projectData)
	}
	
	private def checkClassesInProject(projectData: ProjectData) = {
		for (classDataObj <- List(projectData.getClasses) 
				if classDataObj.isInstanceOf[ClassData]) {
			checkSampleClassData(classDataObj.asInstanceOf[ClassData])
		}
	}
	
	private def checkSampleClassData(classData: ClassData) = {
		assertEquals("Sample", classData.getName)
		assertEquals("Sample.java", classData.getSourceFileName)
		assertEquals("expecting 8 covered lines in Sample class",
				8, classData.getNumberOfCoveredLines)
		
		checkLines(classData);
	}
	
	private def checkSourceFilesInProject(projectData: ProjectData) = {
		for (sourceFileObj <- List(projectData.getSourceFiles) 
				if sourceFileObj.isInstanceOf[SourceFileData]) {
			checkSampleSourceFileData(sourceFileObj.asInstanceOf[SourceFileData])
		}
	}

	private def checkSampleSourceFileData(sourceFileData: SourceFileData) = {
		assertEquals("Sample.java", sourceFileData.getName)
		val classObjs = sourceFileData.getClasses
		assertEquals(1, classObjs.size)
		val classObj = classObjs.first
		assertTrue(classObj.isInstanceOf[ClassData])
		assertEquals("Sample", (classObj.asInstanceOf[ClassData]).getName);
	}
	
	private def checkLines(classData: ClassData) = {
		for (lineDataObj <- List(classData.getLines) if lineDataObj.isInstanceOf[LineData]) {
			val lineData = lineDataObj.asInstanceOf[LineData]
			assertEquals("expecting each line to have been covered exactly once", 
					1, lineData.getHits)
			
			// TODO why is lineData.getMethodName() null?
		}
	}
	*/
}