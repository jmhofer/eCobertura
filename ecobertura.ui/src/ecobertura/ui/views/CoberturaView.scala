package ecobertura.ui.views

import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part._
import org.eclipse.jface.viewers._
import org.eclipse.ui._
import org.eclipse.swt.SWT;

import ecobertura.ui.util.Predef._

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
object CoberturaView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	def ID = "ecobertura.ui.views.CoberturaView"
}
class CoberturaView extends ViewPart {
	private var viewer: TableViewer = null

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
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
