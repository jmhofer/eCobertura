package ecobertura.ui.core;

import java.util.logging.Logger;

import net.sourceforge.cobertura.coveragedata.ClassData;
import net.sourceforge.cobertura.coveragedata.LineData;
import net.sourceforge.cobertura.coveragedata.ProjectData;
import ecobertura.core.CorePlugin$;
import ecobertura.core.results.CoverageResultsListener;
import ecobertura.ui.UIPlugin;

public class CoverageResultsUIListener implements CoverageResultsListener {

	private static final Logger logger = Logger.getLogger(UIPlugin.PLUGIN_ID);
	
	public static CoverageResultsUIListener register() {
		return new CoverageResultsUIListener();
	}
	
	private CoverageResultsUIListener() {
		CorePlugin$.MODULE$.instance().coverageResultsCollector().addListener(this);
		logger.fine("coverage results ui listener registered");
	}
	
	public void unregister() {
		CorePlugin$.MODULE$.instance().coverageResultsCollector().removeListener(this);
		logger.fine("coverage results ui listener unregistered");
	}
	
	@Override
	public void coverageRunCompleted(final ProjectData projectData) {
		logger.fine("coverage run completed - we have data!");
		for (Object classDataObj : projectData.getClasses()) {
			final ClassData classData = (ClassData) classDataObj;
			logger.fine(classData.getName());
			for (Object lineDataObj : classData.getLines()) {
				final LineData lineData = (LineData) lineDataObj;
				logger.fine(lineData.getMethodName() + ", " + lineData.getLineNumber() + ", " + 
						lineData.getHits());
			}
		}
	}
}
