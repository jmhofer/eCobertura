package ecobertura.core.util

import java.net.URL
import java.util.Collections

import org.eclipse.core.resources._
import org.eclipse.core.runtime._
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core._
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import ecobertura.core.CorePlugin;

/**
 * Builder utility for creating Java projects in an Eclipse workspace.
 * To be used in the PDE tests.
 */
object JavaProject {
	def createIn(workspace: IWorkspace) = new JavaProject(workspace)
}

class JavaProject(workspace: IWorkspace) {
	private val NO_MONITOR = null
	private val SOURCE_PATH = "src" //$NON-NLS-1$
	private val OUTPUT_PATH = "bin" //$NON-NLS-1$
		
	private var project: Option[IProject] = None
	private var javaProject: Option[IJavaProject] = None
	private var outputLocation: Option[IPath] = None
	private var srcFolder: Option[IFolder] = None
	private var srcEntry: Option[IClasspathEntry] = None
	private var jreEntry: Option[IClasspathEntry] = None
	
	private var unnamed: Boolean = true
	
	def remove = project.get delete (IResource.FORCE, NO_MONITOR)
	
	def defaultClasspath = JavaRuntime newDefaultProjectClasspathEntry javaProject.get
	
	def named(name: String) = {
		if (unnamed) {
			createWorkspaceProject(name)
			addJavaNatureToWorkspaceProject
			createAndConfigureJavaProject
			unnamed = false
		}
		this
	}
	
	private def createWorkspaceProject(name: String) = {
		project = Some(workspace.getRoot.getProject(name))
		assert(project != Some(null))
		project.get create NO_MONITOR
		project.get open NO_MONITOR 
	}
	
	private def addJavaNatureToWorkspaceProject = {
		val desc = project.get.getDescription
		desc setNatureIds Array(JavaCore.NATURE_ID)
		project.get setDescription (desc, NO_MONITOR)
	}
	
	private def createAndConfigureJavaProject = {
		javaProject = Some(JavaCore.create(project.get))
		assert(javaProject != Some(null))
		initializeOutputPath
		initializeSourcePath
		initializeJRE
		createClassPath
		addSampleSourceFile
		waitUntilAutomaticBuildComplete
	}
	
	private def initializeOutputPath = {
		val folder = project.get getFolder OUTPUT_PATH
		folder create (IResource.FORCE, true, NO_MONITOR)
		outputLocation = Some(folder.getFullPath)
		assert(outputLocation != Some(null))
	}

	private def initializeSourcePath = {
		srcFolder = Some(project.get getFolder SOURCE_PATH)
		assert(srcFolder != Some(null))
		srcFolder.get create (IResource.FORCE, true, NO_MONITOR)
		srcEntry = Some(JavaCore newSourceEntry srcFolder.get.getFullPath)
		assert(srcEntry != Some(null))
	}
	
	private def initializeJRE = {
		jreEntry = Some(JavaCore newContainerEntry new Path(JavaRuntime.JRE_CONTAINER))
		assert(jreEntry != Some(null))
	}
	
	private def createClassPath =
		javaProject.get setRawClasspath (Array[IClasspathEntry](jreEntry.get, srcEntry.get), 
				outputLocation.get, NO_MONITOR)
	
	private def addSampleSourceFile = {
		val srcFilePath = new Path(srcFolder.get.getFullPath + "/Sample.java")
		val srcFile = workspace.getRoot getFile srcFilePath
		val url = FileLocator find (Platform getBundle CorePlugin.pluginId, 
				new Path("resources/Sample.java"), Collections.EMPTY_MAP)
		srcFile create (url.openStream, IResource.FORCE, NO_MONITOR)
		JavaCore create srcFile
	}
	
	private def waitUntilAutomaticBuildComplete =
		Job.getJobManager join (ResourcesPlugin.FAMILY_AUTO_BUILD, NO_MONITOR)
}
