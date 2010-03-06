package ecobertura.ui.views.session.commands

import java.util.logging.Logger
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.ISelection
import org.eclipse.core.commands._
import org.eclipse.ui.handlers.HandlerUtil

import ecobertura.core.data.CoverageSession

import ecobertura.ui.views.session.CoverageSessionView
import ecobertura.ui.views.session._
import ecobertura.ui.util._

class OpenCoveredClassHandler extends AbstractHandler {
	val logger = Logger.getLogger("ecobertura.ui.views.session.command")
	
	override def execute(event: ExecutionEvent) = {
		val window = HandlerUtil.getActiveWorkbenchWindow(event)
		val page = window.getActivePage
		val view = page.findView(CoverageSessionView.ID).asInstanceOf[CoverageSessionView];
				
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
			JavaElementFinder.fromCoverageSessionClass(covClass).find(
					JavaEditorOpener.openAndReveal)
			logger.fine(covClass.coverageData.lines.mkString(", "))
		}
		null
	}
}
