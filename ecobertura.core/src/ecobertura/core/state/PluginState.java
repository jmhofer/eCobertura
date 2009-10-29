package ecobertura.core.state;

import java.io.File;

import org.eclipse.core.runtime.IPath;

public final class PluginState {
	private static final String INSTRUMENTATION_DATA_DIRECTORY = "cobertura";
	
	private final File instrumentationDataDirectory;
	
	public static PluginState initialize(IPath stateLocation) {
		return new PluginState(stateLocation);
	}

	private PluginState(IPath stateLocation) {
		instrumentationDataDirectory = new File(
				stateLocation.toFile(), INSTRUMENTATION_DATA_DIRECTORY);
		instrumentationDataDirectory.mkdirs();
	}
	
	public File instrumentationDataDirectory() {
		return instrumentationDataDirectory;
	}
	
	public void cleanUp() {
		for (File file : instrumentationDataDirectory.listFiles()) {
			file.delete();
		}
		instrumentationDataDirectory.delete();
	}
}
