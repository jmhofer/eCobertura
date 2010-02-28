package ecobertura.ui.views.session

import org.eclipse.swt.graphics.Image
import org.eclipse.ui.PlatformUI
import org.eclipse.jdt.ui.ISharedImages

trait CoverageSessionTreeNode {
	private var nodeChildren: List[CoverageSessionTreeNode] = Nil
	
	var parent: Option[CoverageSessionTreeNode] = None
	def name: String
	def icon: Image

	def children: List[CoverageSessionTreeNode] = nodeChildren
	def hasChildren = nodeChildren != Nil
	
	def addChild(child: CoverageSessionTreeNode) = {
		nodeChildren ::= child
		child.parent = Some(this)
	}
	
	def removeAllChildren = {
		nodeChildren = Nil
	}
}

abstract class CoverageSessionNode

case object CoverageSessionRoot 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override def name = "root"
	override def icon =  
		PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_DEFAULT)
}

case class CoverageSessionPackage(name: String) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override def icon =
		PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_PACKAGE)
}

case class CoverageSessionClass(name: String) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override def icon =  
		PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_CLASS)
}
