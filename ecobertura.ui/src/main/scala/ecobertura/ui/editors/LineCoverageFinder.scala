package ecobertura.ui.editors

import java.util.logging.Logger

import org.eclipse.ui.texteditor.ITextEditor
import org.eclipse.jdt.core.ITypeRoot
import org.eclipse.jdt.ui._

import ecobertura.core.data._

object LineCoverageFinder {
	val logger = Logger.getLogger("ecobertura.ui.editors")
	
	def forSession(session: CoverageSession) = new LineCoverageFinder(session)
}

class LineCoverageFinder(session: CoverageSession) {
	import LineCoverageFinder.logger
	
	def findInEditor(editor: ITextEditor): List[LineCoverage] = {
		JavaUI.getEditorInputTypeRoot(editor.getEditorInput) match {
			case typeRoot: ITypeRoot => findForTypeRoot(typeRoot) 
			case _ => Nil
		}
	}
	
	private def findForTypeRoot(typeRoot: ITypeRoot) = {
		val sourceFileName = typeRoot.getElementName
		val packageName = typeRoot.getParent.getElementName
		
		def findInPackage(packageCov: PackageCoverage) = {
			packageCov.sourceFileLines.get(sourceFileName) match {
				case Some(lines) => {
					logger.fine("found lines: " + lines.mkString(", "))
					lines
				}
				case None => Nil
			}
		}
		
		session.packageMap.get(packageName) match {
			case Some(packageCov) => findInPackage(packageCov)
			case None => Nil
		}
	}
}