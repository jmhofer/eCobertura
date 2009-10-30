package ecobertura.core.results;

import net.sourceforge.cobertura.coveragedata.ProjectData;

public interface CoverageResultsListener {
	void coverageRunCompleted(ProjectData projectData);
}
