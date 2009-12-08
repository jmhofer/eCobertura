package ecobertura.core.util;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;

import ecobertura.core.CorePlugin$;

/**
 * Builder utility class for creating Java projects in an Eclipse workspace.
 * To be used in the PDE tests.
 */
public class JavaProject {
	
	private final static IProgressMonitor NO_MONITOR = null;
	
	private final static String SOURCE_PATH = "src"; //$NON-NLS-1$
	private final static String OUTPUT_PATH = "bin"; //$NON-NLS-1$
	
	private IWorkspace workspace;
	private IProject project;
	private IFolder srcFolder;
	private IJavaProject javaProject;
	private IPath outputLocation;
	private IClasspathEntry srcEntry;
	private IClasspathEntry jreEntry;
		
	private boolean unnamed = true;
	
	public static JavaProject createIn(IWorkspace workspace) {
		return new JavaProject(workspace);
	}
	
	private JavaProject(IWorkspace workspace) {
		this.workspace = workspace;
	}

	public void remove() throws CoreException {
		project.delete(IProject.FORCE, NO_MONITOR);
	}
	
	public IRuntimeClasspathEntry defaultClasspath() throws CoreException {
		return JavaRuntime.newDefaultProjectClasspathEntry(javaProject);
	}
	
	public JavaProject named(String name) throws CoreException, JavaModelException, IOException, OperationCanceledException, InterruptedException {
		if (unnamed) {
			createWorkspaceProject(name);
			addJavaNatureToWorkspaceProject();
			createAndConfigureJavaProject();
			unnamed = false;
		}
		
		return this;
	}

	private void createWorkspaceProject(String name) throws CoreException {
		project = workspace.getRoot().getProject(name);
		project.create(JavaProject.NO_MONITOR);
		project.open(JavaProject.NO_MONITOR);
	}

	private void addJavaNatureToWorkspaceProject() throws CoreException {
		IProjectDescription desc = project.getDescription();
		desc.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(desc, JavaProject.NO_MONITOR);
	}

	private void createAndConfigureJavaProject() throws CoreException, IOException, OperationCanceledException, InterruptedException {
		javaProject = JavaCore.create(project);
		initializeOutputPath();
		initializeSourcePath();
		initializeJRE();
		createClassPath();
		addSampleSourceFile();
		waitUntilAutomaticBuildComplete();
	}

	private void initializeOutputPath() throws CoreException {
	    IFolder folder = project.getFolder(OUTPUT_PATH);
	    folder.create(IResource.FORCE, true, NO_MONITOR);
		outputLocation = folder.getFullPath();
	}

	private void initializeSourcePath() throws CoreException {
	    srcFolder = project.getFolder(SOURCE_PATH);
	    srcFolder.create(IResource.FORCE, true, NO_MONITOR);
		srcEntry = JavaCore.newSourceEntry(srcFolder.getFullPath());
	}

	private void initializeJRE() {
		jreEntry = JavaCore.newContainerEntry(
				new Path(JavaRuntime.JRE_CONTAINER));
	}
	
	private void createClassPath() 
			throws JavaModelException {
		
		javaProject.setRawClasspath(
				new IClasspathEntry[] { jreEntry, srcEntry }, 
				outputLocation, JavaProject.NO_MONITOR);
	}
	
	private void addSampleSourceFile() throws CoreException, IOException {
		IPath srcFilePath = new Path(srcFolder.getFullPath() + "/Sample.java");
		IFile srcFile = workspace.getRoot().getFile(srcFilePath);
		URL url = FileLocator.find(Platform.getBundle(CorePlugin$.MODULE$.pluginId()), new Path("resources/Sample.java"), Collections.EMPTY_MAP);
		srcFile.create(url.openStream(), IFile.FORCE, NO_MONITOR);
		JavaCore.create(srcFile);
	}
	
	private void waitUntilAutomaticBuildComplete() throws OperationCanceledException, InterruptedException {
		Job.getJobManager().join(ResourcesPlugin.FAMILY_AUTO_BUILD, NO_MONITOR);
	}
}