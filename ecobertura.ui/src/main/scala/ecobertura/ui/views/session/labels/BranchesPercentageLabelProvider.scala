package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider

import ecobertura.ui.views.session.CoverageSessionTreeNode
import ecobertura.ui.util.Format

class BranchesPercentageLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => 
			Format.asPercentage(coverageNode.branchesCovered, coverageNode.branchesTotal)
		case _ => "???"
	}
}
