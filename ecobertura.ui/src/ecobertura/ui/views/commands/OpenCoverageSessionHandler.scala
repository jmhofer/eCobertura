package ecobertura.ui.views.commands

import org.eclipse.core.commands._
import org.eclipse.jface.dialogs.MessageDialog
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.ui._

class OpenCoverageSessionHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val parentShell = HandlerUtil.getActiveShell(event)
		MessageDialog.openInformation(parentShell, "Cobertura View", "hello world")		
		null
	}
}
