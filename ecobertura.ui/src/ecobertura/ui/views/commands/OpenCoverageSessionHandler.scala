package ecobertura.ui.views.commands

import org.eclipse.core.commands._
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.FileDialog
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.core.cobertura.CoberturaWrapper

class OpenCoverageSessionHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val sessionFilename = retrieveCoverageSessionFilename(event)
		val projectData = CoberturaWrapper.get.projectDataFromFile(sessionFilename)
		
		null // handlers must return null
	}
	
	private def retrieveCoverageSessionFilename(event: ExecutionEvent) = {
		val parentShell = HandlerUtil.getActiveShell(event)
		val dialog = new FileDialog(parentShell, SWT.OPEN)
		dialog.setFilterNames(Array("Cobertura Session Files (*.ser)", "All Files (*)"))
		dialog.setFilterExtensions(Array("*.ser", "*"))
		
		dialog.open
	}
}
