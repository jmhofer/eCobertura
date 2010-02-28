package ecobertura.ui.views.session

import org.eclipse.swt.graphics.Image
import org.eclipse.ui.PlatformUI
import org.eclipse.jdt.ui._

trait CoverageSessionTreeNode {
	private var nodeChildren: List[CoverageSessionTreeNode] = Nil
	
	var parent: Option[CoverageSessionTreeNode] = None
	def name: String
	def icon: Image

	def linesCovered: Int
	def linesTotal: Int
	
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
	override val name = "root"
	override val icon =  
		PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_DEFAULT)
		
	override def linesCovered = 0
	override def linesTotal = 0
}

case class CoverageSessionPackage(name: String, linesCovered: Int, linesTotal: Int) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override val icon =
		JavaUI.getSharedImages.getImage(ISharedImages.IMG_OBJS_PACKAGE)
}

case class CoverageSessionClass(name: String, linesCovered: Int, linesTotal: Int) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override val icon =  
		JavaUI.getSharedImages.getImage(ISharedImages.IMG_OBJS_CLASS)
}
