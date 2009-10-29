package ecobertura.core.util;

import java.util.concurrent.Semaphore;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.model.IProcess;

public class LaunchTracker {
	
	private final Semaphore launchRunning = new Semaphore(1);
	
	public static LaunchTracker prepareLaunch() throws InterruptedException {
		return new LaunchTracker();
	}
	
	private LaunchTracker() throws InterruptedException {
		registerLaunchTerminationListener();
		launchRunning.acquire();
		
	}

	private void registerLaunchTerminationListener() {
		DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener() {
			@Override 
			public void handleDebugEvents(DebugEvent[] events) {
				for (DebugEvent event : events) {
					if (isLaunchTerminationEvent(event)) {
						launchRunning.release();
					}
				}
			}

			private boolean isLaunchTerminationEvent(DebugEvent event) {
				return event.getSource() instanceof IProcess 
						&& event.getKind() == DebugEvent.TERMINATE;
			}
		});
	}
	
	public void waitForLaunchTermination() throws InterruptedException {
		launchRunning.acquire();
		launchRunning.release();
	}
}
