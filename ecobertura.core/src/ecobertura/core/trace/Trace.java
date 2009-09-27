package ecobertura.core.trace;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;

import ecobertura.core.CorePlugin;

public class Trace {
	private static final String GLOBAL_PACKAGE = "ecobertura"; //$NON-NLS-1$
	private static final String GLOBAL_TRACE_LEVEL = CorePlugin.PLUGIN_ID + "/debug/level"; //$NON-NLS-1$

	private Trace() {
		/* protect from instantiation */
	}
	
	public static void configure() {
		String globalLevel = getDebugOptionStringOrElse(GLOBAL_TRACE_LEVEL, "WARNING").toUpperCase(); //$NON-NLS-1$
		
	    Handler consoleHandler = new ConsoleHandler();
	    Logger.getLogger(GLOBAL_PACKAGE).addHandler(consoleHandler);
	    Logger.getLogger(GLOBAL_PACKAGE).setLevel(Level.parse(globalLevel));
	}
	
	private static String getDebugOptionStringOrElse(String optionKey, String defaultValue) {
		assert optionKey != null;
		
		String optionValue = Platform.getDebugOption(GLOBAL_TRACE_LEVEL);
		if (optionValue == null) {
			return defaultValue;
		}
		return optionValue;
	}
}
