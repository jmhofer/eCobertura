package ecobertura.core.log;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

public class Logger {
	
	private static ILog log;
	
	private Logger() {
		// protect from instantiation
	}
	
	public static void logFor(final ILog log) {
		Logger.log = log;
		// TODO Platform.getDebugOption(option) for setting threshold and 
		// perhaps selecting packages to log for
		// TODO log to stdout instead of the error log, perhaps
	}
	
	public static void info(final String message) {
		Logger.log.log(LogStatus.fromMessageWithSeverity(message, Status.INFO));
	}
	
	public static void info(final String message, final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.INFO));
	}
	
	public static void info(final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.INFO));
	}
	
	public static void warn(final String message) {
		Logger.log.log(LogStatus.fromMessageWithSeverity(message, Status.WARNING));
	}
	
	public static void warn(final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.WARNING));
	}
	
	public static void warn(final String message, final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.WARNING));
	}
	
	public static void error(final String message) {
		Logger.log.log(LogStatus.fromMessageWithSeverity(message, Status.ERROR));
	}
	
	public static void error(final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.ERROR));
	}
	
	public static void error(final String message, final Throwable throwable) {
		Logger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.ERROR));
	}
}
