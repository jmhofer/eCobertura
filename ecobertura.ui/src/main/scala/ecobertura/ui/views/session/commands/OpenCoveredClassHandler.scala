package ecobertura.ui.views.session.commands

import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ISelection
import org.eclipse.core.commands._
import org.eclipse.ui.handlers.HandlerUtil
import org.eclipse.jface.dialogs.MessageDialog

import ecobertura.core.data.CoverageSession

import ecobertura.ui.views.session.CoverageSessionView
import ecobertura.ui.views.session._
import ecobertura.ui.util.JavaElementFinder

class OpenCoveredClassHandler extends AbstractHandler {
	override def execute(event: ExecutionEvent) = {
		val window = HandlerUtil.getActiveWorkbenchWindow(event)
		val page = window.getActivePage
		val view = page.findView(CoverageSessionView.ID).asInstanceOf[CoverageSessionView];
		MessageDialog.openInformation(window.getShell, "Doubleclick detected", 
				String.format("Clicked: %s, normally we'd jump now...", view.selection.toString))
				
		view.selection match {
			case structuredSelection: IStructuredSelection =>
				handleStructuredSelection(structuredSelection.getFirstElement)
			case _ => /* nothing to do */
		}
		
		def handleStructuredSelection(selectedObject: Any) = selectedObject match {
			case covClass: CoverageSessionClass => handleClassSelection(covClass)
			case _ => /* nothing to do */
		}

		def handleClassSelection(covClass: CoverageSessionClass) = {
			MessageDialog.openInformation(window.getShell, "Doubleclick detected", 
					String.format("Clicked: %s, normally we'd jump now...", covClass.coverageData.toString))
			val selectedJavaElement = JavaElementFinder.fromCoverageSessionClass(covClass).find

			// TODO jump to the respective editor instead
			
		}
		
		null
	}
}
