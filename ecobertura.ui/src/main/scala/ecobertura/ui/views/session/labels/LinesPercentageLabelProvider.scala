package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider

import ecobertura.ui.views.session.CoverageSessionTreeNode
import ecobertura.ui.util.Format

class LinesPercentageLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => 
			Format.asPercentage(coverageNode.coverageData.linesCovered, 
					coverageNode.coverageData.linesTotal)
		case _ => "???"
	}
}
