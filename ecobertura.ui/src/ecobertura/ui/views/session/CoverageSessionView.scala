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
	private var viewer: TreeViewer = null

	// dummy tree structure (for now...)
	abstract class CoverageNode
	trait NamedNode {
		def name : String
	}
	case class CoverageRoot() extends CoverageNode with NamedNode {
		override def name = "root"
		val packages = CoveragePackage("ecoburtura.blubb", CoverageClass("BluBlub") :: CoverageClass("BlubBlub2") :: Nil) :: 
				CoveragePackage("ecobertura.blibb", CoverageClass("BliBilbb") :: CoverageClass("Bli2") :: Nil) :: Nil
	}
	case class CoveragePackage(name: String, classes: List[CoverageClass]) extends CoverageNode with NamedNode {
		classes.foreach(_.setParent(this))
	}
	case class CoverageClass(name: String) extends CoverageNode with NamedNode {
		private var parentPackage : CoveragePackage = null
		def parent = parentPackage
		def setParent(parent : CoveragePackage) = {
			parentPackage = parent
		}
	}
	
	class ViewContentProvider extends ITreeContentProvider {
		
		override def getElements(element: Any) : Array[Object] = getChildren(element)
		override def inputChanged(viewer: Viewer, arg0: Any, arg1: Any) = {}
		override def dispose = {}
		
		override def getChildren(parentElement: Any) : Array[Object] = parentElement match {
			case root: CoverageRoot => root.packages.toArray
			case CoveragePackage(_, classes) => classes.toArray
			case CoverageClass(_) => Array()
			case _ => Array()
		}
		
		override def getParent(element: Any) : Object = element match {
			case CoveragePackage(_, _) => CoverageRoot()
			case covClass: CoverageClass => covClass.parent
			case _ => null
		}
		
		override def hasChildren(element: Any) : Boolean = element match {
			case CoverageClass(_) => false
			case _ => true
		}
	}
	
	class ViewLabelProvider extends LabelProvider {
		override def getText(node: Any) = node match {
			case namedNode: NamedNode => namedNode.name
			case _ => "???"
		}
		override def getImage(obj: Any) =
			PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJ_ELEMENT)
	}
	
	class NameSorter extends ViewerSorter
	
	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	override def createPartControl(parent: Composite) = {
		viewer = new TreeViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL)
		viewer.setContentProvider(new ViewContentProvider)
		viewer.setLabelProvider(new ViewLabelProvider)
		viewer.setSorter(new NameSorter)
		viewer.setInput(CoverageRoot())

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
