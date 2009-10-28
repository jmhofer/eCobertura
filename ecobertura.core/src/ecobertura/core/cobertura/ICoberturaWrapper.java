package ecobertura.core.cobertura;

import java.io.File;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import net.sourceforge.cobertura.coveragedata.ProjectData;

public interface ICoberturaWrapper {
	void instrumentClassFile(File classFileToInstrument) throws CoberturaException;
	ProjectData projectData() throws CoberturaException;
	IPath pathToJar() throws CoreException;
}
