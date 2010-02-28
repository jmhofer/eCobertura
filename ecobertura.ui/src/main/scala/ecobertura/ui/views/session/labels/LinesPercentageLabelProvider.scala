package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider

import ecobertura.ui.views.session.CoverageSessionTreeNode

class LinesPercentageLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => computePercentage(coverageNode)
		case _ => "???"
	}
	
	private def computePercentage(node: CoverageSessionTreeNode) = {
		if (node.linesTotal == 0) {
			"-"
		} else {
			String.format("%3.2f %%", double2Double(
					node.linesCovered.toDouble / node.linesTotal * 100.0)) 
		}
	}
}
