package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider

import ecobertura.ui.views.session.CoverageSessionTreeNode

class LinesCoveredLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => coverageNode.linesCovered.toString
		case _ => "???"
	}
}
