package ecobertura.core.util;

import java.util.concurrent.Semaphore;

import net.sourceforge.cobertura.coveragedata.ProjectData;
import ecobertura.core.CorePlugin;
import ecobertura.core.results.CoverageResultsListener;

public class LaunchTracker {
	
	private final Semaphore launchRunning = new Semaphore(1);
	private ProjectData projectData;
	
	public static LaunchTracker prepareLaunch() throws InterruptedException {
		return new LaunchTracker();
	}
	
	private LaunchTracker() throws InterruptedException {
		registerCoverageResultsListener();
		launchRunning.acquire();
		
	}

	private void registerCoverageResultsListener() {
		CorePlugin.instance().coverageResultsCollector().addListener(new CoverageResultsListener() {
			@Override
			public void coverageRunCompleted(ProjectData projectData) {
				LaunchTracker.this.projectData = projectData;
				launchRunning.release();
			}
		});
	}
	
	public ProjectData waitForAndRetrieveCoverageResults() throws InterruptedException {
		launchRunning.acquire();
		launchRunning.release();
		return projectData;
	}
}
