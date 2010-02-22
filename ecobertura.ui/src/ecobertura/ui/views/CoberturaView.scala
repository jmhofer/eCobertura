package ecobertura.ui.views

import org.eclipse.swt.widgets.Composite
import org.eclipse.ui.part._
import org.eclipse.jface.viewers._
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action._
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui._
import org.eclipse.swt.widgets.Menu;
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
	private var action1: Action = null
	private var action2: Action = null
	private var doubleClickAction: Action = null

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
		
		makeActions
		hookContextMenu
		hookDoubleClickAction
		contributeToActionBars

		def makeActions = {
			action1 = showMessage("Action 1 executed")
			action1.setText("Action 1")
			action1.setToolTipText("Action 1 tooltip")
			action1.setImageDescriptor(PlatformUI.getWorkbench.getSharedImages
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK))
				
			action2 = showMessage("Action 2 executed")
			action2.setText("Action 2")
			action2.setToolTipText("Action 2 tooltip")
			action2.setImageDescriptor(PlatformUI.getWorkbench.getSharedImages
				.getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK))
				
			doubleClickAction = {
				val selection = viewer.getSelection
				val obj = selection.asInstanceOf[IStructuredSelection].getFirstElement
				showMessage("Double-click detected on " + obj.toString)
			}
			
			def showMessage(message: String) = 
					MessageDialog.openInformation(viewer.getControl.getShell, 
							"Cobertura View", message)
		}
		
		def hookContextMenu = {
			val menuMgr = new MenuManager("#PopupMenu")
			menuMgr.setRemoveAllWhenShown(true)
			menuMgr.addMenuListener { manager: IMenuManager => fillContextMenu(manager) }
			val menu = menuMgr.createContextMenu(viewer.getControl)
			viewer.getControl.setMenu(menu)
			getSite.registerContextMenu(menuMgr, viewer)
			
		}

		def fillContextMenu(manager: IMenuManager) = {
			manager.add(action1)
			manager.add(action2)
			// Other plug-ins can contribute there actions here
			manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS))
		}
		
		def hookDoubleClickAction = {
			viewer.addDoubleClickListener { event: DoubleClickEvent => doubleClickAction.run }
		}
		
		def contributeToActionBars = {
			val bars = getViewSite.getActionBars
			fillLocalPullDown(bars.getMenuManager)
			fillLocalToolBar(bars.getToolBarManager)
			
			def fillLocalPullDown(manager: IMenuManager) = {
				manager.add(action1)
				manager.add(new Separator)
				manager.add(action2)
			}
			
			def fillLocalToolBar(manager: IToolBarManager) = {
				manager.add(action1)
				manager.add(action2)
			}
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	override def setFocus = {
		viewer.getControl.setFocus
	}
}
