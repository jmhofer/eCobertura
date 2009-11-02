package ecobertura.ui.core;

import java.util.logging.Logger;

import net.sourceforge.cobertura.coveragedata.ProjectData;
import ecobertura.core.results.CoverageResultsListener;
import ecobertura.core.CorePlugin;
import ecobertura.ui.UIPlugin;

public class CoverageResultsUIListener implements CoverageResultsListener {

	private static final Logger logger = Logger.getLogger(UIPlugin.PLUGIN_ID);
	
	public static CoverageResultsUIListener register() {
		return new CoverageResultsUIListener();
	}
	
	private CoverageResultsUIListener() {
		CorePlugin.instance().coverageResultsCollector().addListener(this);
		logger.fine("coverage results ui listener registered");
	}
	
	public void unregister() {
		CorePlugin.instance().coverageResultsCollector().removeListener(this);
		logger.fine("coverage results ui listener unregistered");
	}
	
	@Override
	public void coverageRunCompleted(final ProjectData projectData) {
		logger.fine("coverage run completed - we have data!");
	}
}
