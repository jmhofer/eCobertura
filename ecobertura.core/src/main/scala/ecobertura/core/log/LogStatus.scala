package ecobertura.core.log

import org.eclipse.core.runtime.IStatus
import org.eclipse.core.runtime.Status

import ecobertura.core.CorePlugin

object LogStatus {
	
	object Severity extends Enumeration {
		val Error = Value(IStatus.ERROR)
		val Warning = Value(IStatus.WARNING)
		val Info = Value(IStatus.INFO)
	}
		
	def something = Severity.Info
	
	def fromMessageWithSeverity(message: String, severity: Severity.Value) = { 
		assert(message != null)
		new Status(severity.id, CorePlugin.pluginId, message)
	}
	
	def fromExceptionWithSeverity(
			message: String, throwable: Throwable, severity: Severity.Value) = {
		assert(message != null)
		assert(throwable != null)
		new Status(severity.id, CorePlugin.pluginId, message, throwable)	
	}
}