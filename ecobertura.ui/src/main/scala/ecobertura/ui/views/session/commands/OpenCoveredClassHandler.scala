package ecobertura.ui.views.session.commands

import org.eclipse.core.commands._
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.jface.dialogs.MessageDialog
import ecobertura.core.data.CoverageSession
import ecobertura.ui.views.session.CoverageSessionView

class OpenCoveredClassHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val window = HandlerUtil.getActiveWorkbenchWindow(event)
		val page = window.getActivePage
		val view = page.findView(CoverageSessionView.ID).asInstanceOf[CoverageSessionView];
		val selection = view.selection
		
		// TODO jump to the respective editor instead
		MessageDialog.openInformation(window.getShell, "Doubleclick detected", 
				String.format("Clicked: %s, normally we'd jump now...", selection.toString))
				
		null // handlers must return null
	}
}
