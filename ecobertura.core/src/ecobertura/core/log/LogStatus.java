package ecobertura.core.log;

import org.eclipse.core.runtime.Status;

import ecobertura.core.CorePlugin;

public class LogStatus extends Status {
	public static LogStatus fromMessageWithSeverity(final String message, final int severity) {
		assert message != null;
		assert severity == Status.ERROR || severity == Status.WARNING || severity == Status.INFO;
		
		return new LogStatus(severity, message);
	}
	
	public static LogStatus fromExceptionWithSeverity(final String message, final Throwable throwable, 
			final int severity) {
		assert message != null;
		assert severity == Status.ERROR || severity == Status.WARNING || severity == Status.INFO;
		assert throwable != null;
		
		return new LogStatus(severity, message, throwable);
	}
	
	private LogStatus(final int severity, final String message) {
		super(severity, CorePlugin.PLUGIN_ID, message);
	}
	
	private LogStatus(final int severity, final String message, final Throwable throwable) {
		super(severity, CorePlugin.PLUGIN_ID, message, throwable);
	}
	
}
