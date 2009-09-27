package ecobertura.core.log;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;

/**
 * Logs into the Eclipse Error Log. 
 */
public class EclipseLogger {
	
	private static ILog log;
	
	private EclipseLogger() {
		// protect from instantiation
	}
	
	public static void logFor(final ILog log) {
		EclipseLogger.log = log;
	}
	
	public static void info(final String message) {
		EclipseLogger.log.log(LogStatus.fromMessageWithSeverity(message, Status.INFO));
	}
	
	public static void info(final String message, final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.INFO));
	}
	
	public static void info(final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.INFO));
	}
	
	public static void warn(final String message) {
		EclipseLogger.log.log(LogStatus.fromMessageWithSeverity(message, Status.WARNING));
	}
	
	public static void warn(final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.WARNING));
	}
	
	public static void warn(final String message, final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.WARNING));
	}
	
	public static void error(final String message) {
		EclipseLogger.log.log(LogStatus.fromMessageWithSeverity(message, Status.ERROR));
	}
	
	public static void error(final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(throwable.getMessage(), throwable, Status.ERROR));
	}
	
	public static void error(final String message, final Throwable throwable) {
		EclipseLogger.log.log(LogStatus.fromExceptionWithSeverity(message, throwable, Status.ERROR));
	}
}
