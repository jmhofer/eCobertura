package ecobertura.ui.views.session

import org.eclipse.jface.viewers.LabelProvider
import org.eclipse.ui._

class CoverageSessionLabelProvider extends LabelProvider {
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
