package ecobertura.ui.views.session

import ecobertura.core.data.CoverageSession
import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.ITreeContentProvider

object CoverageSessionModel {
	private var instance = new CoverageSessionModel

	def get = instance
}

class CoverageSessionModel extends CoverageSessionPublisher with ITreeContentProvider {
	private var coverageSession: Option[CoverageSession] = None
	
	def clear = {
		coverageSession = None
		fireSessionReset
	}
	
	def setCoverageSession(coverageSession: CoverageSession) = {
		this.coverageSession = Some(coverageSession)
		fireSessionReset
	}
	
	override def getElements(element: Any) : Array[Object] = getChildren(element)
	override def inputChanged(viewer: Viewer, arg0: Any, arg1: Any) = { /* no changes allowed yet */ }
	override def dispose = { /* nothing to dispose of right now */ }
	
	override def getChildren(parentElement: Any) : Array[Object] = parentElement match {
		case node: CoverageSessionTreeNode => node.children.toArray
		case _ => Array()
	}
	
	override def getParent(element: Any) : Object = element match {
		case node: CoverageSessionTreeNode => node.parent.getOrElse(null)
		case _ => null
	}
	
	override def hasChildren(element: Any) : Boolean = element match {
		case node: CoverageSessionTreeNode => node.hasChildren
		case _ => false
	}
}
