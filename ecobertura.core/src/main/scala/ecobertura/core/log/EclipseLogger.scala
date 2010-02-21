package ecobertura.core.log

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;

object EclipseLogger {
	private var optLog: Option[ILog] = None
	
	def logFor(ilog: ILog) = {
		assert(ilog != null)
		optLog = Some(ilog) 
	}
	
	def info(message: String) = log(LogStatus.fromMessageWithSeverity(
			message, LogStatus.Severity.Info))

	def info(message: String, throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			message, throwable, LogStatus.Severity.Info))

	def info(throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			throwable.getMessage(), throwable, LogStatus.Severity.Info))
		
	def warn(message: String) = log(LogStatus.fromMessageWithSeverity(
			message, LogStatus.Severity.Warning))

	def warn(message: String, throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			message, throwable, LogStatus.Severity.Warning))

	def warn(throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			throwable.getMessage(), throwable, LogStatus.Severity.Warning))
		
	def error(message: String) = log(LogStatus.fromMessageWithSeverity(
			message, LogStatus.Severity.Error))

	def error(message: String, throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			message, throwable, LogStatus.Severity.Error))

	def error(throwable: Throwable) = log(LogStatus.fromExceptionWithSeverity(
			throwable.getMessage(), throwable, LogStatus.Severity.Error))
		
	def log(status: IStatus): Unit = optLog match {
		case Some(ilog) => ilog.log(status)
		case None => // simply don't log
	}
}
