package ecobertura.ui.views.session

import ecobertura.core.data._

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
		buildFromSession
		fireSessionReset
	}
	
	def setCoverageSession(coverageSession: CoverageSession) = {
		this.coverageSession = Some(coverageSession)
		buildFromSession
		fireSessionReset
	}
	
	def buildFromSession = {
		CoverageSessionRoot.removeAllChildren
		coverageSession match {
			case Some(session) => {
				session.packages.foreach { covPackage =>
					CoverageSessionRoot.addChild(buildFromPackageCoverage(covPackage))
				}
			}
			case None => /* nothing to do */ 
		}
	}
	
	def buildFromPackageCoverage(covPackage: PackageCoverage) = {
		val sessionPackage = CoverageSessionPackage(covPackage.name)
		covPackage.classes.foreach { covClass =>
			sessionPackage.addChild(CoverageSessionClass(covClass.name))
		}
		sessionPackage
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
