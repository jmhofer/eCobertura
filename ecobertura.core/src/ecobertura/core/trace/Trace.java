package ecobertura.core.trace;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.runtime.Platform;

public class Trace {
	private static final String GLOBAL_PACKAGE = "ecobertura"; //$NON-NLS-1$
	private static final String GLOBAL_TRACE_LEVEL = "%s/debug/level"; //$NON-NLS-1$

	private Trace() {
		/* protect from instantiation */
	}
	
	public static void configureForPluginId(final String pluginId) {
		String globalLevel = getDebugOptionStringOrElse(
				String.format(GLOBAL_TRACE_LEVEL, pluginId), Level.WARNING.toString());
	    Logger.getLogger(GLOBAL_PACKAGE).addHandler(configuredConsoleHandler());
	    Logger.getLogger(GLOBAL_PACKAGE).setLevel(Level.parse(globalLevel));
	}

	private static String getDebugOptionStringOrElse(String optionKey, String defaultValue) {
		assert optionKey != null;
		
		String optionValue = Platform.getDebugOption(optionKey);
		System.out.printf("option: %s = %s%n", optionKey, optionValue);
		if (optionValue == null) {
			return defaultValue.toUpperCase();
		}
		return optionValue.toUpperCase();
	}

	private static Handler configuredConsoleHandler() {
		final Handler consoleHandler = new ConsoleHandler();
	    consoleHandler.setLevel(Level.ALL);
		return consoleHandler;
	}
}
