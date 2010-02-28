package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider
import org.eclipse.ui._

import ecobertura.ui.views.session.CoverageSessionTreeNode

class NameLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => coverageNode.name
		case _ => "??? invalid node ???"
	}

	override def getImage(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => coverageNode.icon
		case _ => 
			PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK)
	}
}
