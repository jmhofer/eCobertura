package ecobertura.ui.views.session

import java.util.logging.Logger
import ecobertura.core.data._

import org.eclipse.jface.viewers.Viewer
import org.eclipse.jface.viewers.ITreeContentProvider

object CoverageSessionModel {
	private val logger = Logger.getLogger("ecobertura.ui.views.session") //$NON-NLS-1$
	
	private var instance = new CoverageSessionModel

	def get = instance
}

class CoverageSessionModel extends CoverageSessionPublisher with ITreeContentProvider {
	import CoverageSessionModel.logger
	
	private var internalCoverageSession: Option[CoverageSession] = None
	
	def clear = {
		internalCoverageSession = None
		buildFromSession
		fireSessionReset
	}
	
	def setCoverageSession(coverageSession: CoverageSession) = {
		internalCoverageSession = Some(coverageSession)
		buildFromSession
		fireSessionReset
	}
	
	def coverageSession = internalCoverageSession
		
	def buildFromSession = {
		logger.fine("Building from coverage session...")
		CoverageSessionRoot.removeAllChildren
		coverageSession match {
			case Some(session) => {
				val covAllPackages = new CoverageSessionAllPackages(session)
				CoverageSessionRoot.addChild(covAllPackages)
				session.packages.foreach { covPackage =>
					covAllPackages.addChild(buildFromPackageCoverage(covPackage))
				}
			}
			case None => /* nothing to do */ 
		}
	}
	
	def buildFromPackageCoverage(covPackage: PackageCoverage) = {
		val sessionPackage = new CoverageSessionPackage(covPackage)
		logger.fine("Building package from coverage session..." + sessionPackage.name)
		covPackage.classes.foreach { covClass =>
			sessionPackage.addChild(new CoverageSessionClass(covClass))
			logger.fine("... adding class " + covClass.name)
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
