package ecobertura.core.cobertura;

import java.io.File;

import net.sourceforge.cobertura.coveragedata.ProjectData;

public interface ICoberturaWrapper {
	void instrumentClassFile(File classFileToInstrument) throws CoberturaException;
	ProjectData projectData() throws CoberturaException;
}
