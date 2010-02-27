package ecobertura.ui.views.session

import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part._
import org.eclipse.jface.viewers._
import org.eclipse.ui._
import org.eclipse.swt.SWT;

import ecobertura.ui.util.Predef._

object CoverageSessionView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	def ID = "ecobertura.ui.views.session.CoverageSessionView"
}
class CoverageSessionView extends ViewPart {
	private var viewer: TableViewer = null

	class ViewContentProvider extends IStructuredContentProvider {
		override def inputChanged(v: Viewer, oldInput: Any, newInput: Any) = {}
		override def dispose = {}
		override def getElements(parent: Any) = Array[Object]("One", "Two", "Three")
	}
	
	class ViewLabelProvider extends LabelProvider with ITableLabelProvider {
		override def getColumnText(obj: Object, index: Int) = getText(obj)
		override def getColumnImage(obj: Object, index: Int) = getImage(obj)
		override def getImage(obj: Object) = 
			PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJ_ELEMENT)
	}
	
	class NameSorter extends ViewerSorter
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	override def createPartControl(parent: Composite) = {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL)
		viewer.setContentProvider(new ViewContentProvider)
		viewer.setLabelProvider(new ViewLabelProvider)
		viewer.setSorter(new NameSorter)
		viewer.setInput(getViewSite)

		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench.getHelpSystem.setHelp(viewer.getControl, "ecobertura.ui.viewer")
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	override def setFocus = {
		viewer.getControl.setFocus
	}
}
