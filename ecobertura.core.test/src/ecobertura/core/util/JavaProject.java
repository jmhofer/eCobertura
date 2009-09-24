package ecobertura.core.util;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

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

	public JavaProject named(String name) throws CoreException, JavaModelException {
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

	private void createAndConfigureJavaProject() throws JavaModelException {
		javaProject = JavaCore.create(project);
		initializeOutputPath();
		initializeSourcePath();
		initializeJRE();
		createClassPathFrom();
	}

	private void initializeOutputPath() throws JavaModelException {
		outputLocation = project.getFullPath().append(OUTPUT_PATH);
	}

	private void initializeSourcePath() {
		IPath srcPath = project.getFullPath().append(SOURCE_PATH);
		srcEntry = JavaCore.newSourceEntry(srcPath);
	}

	private void initializeJRE() {
		jreEntry = JavaCore.newContainerEntry(
				new Path(JavaRuntime.JRE_CONTAINER));
	}
	
	private void createClassPathFrom() 
			throws JavaModelException {
		
		javaProject.setRawClasspath(
				new IClasspathEntry[] { jreEntry, srcEntry }, 
				outputLocation, JavaProject.NO_MONITOR);
	}
}