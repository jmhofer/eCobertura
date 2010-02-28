package ecobertura.ui.views.session

import org.eclipse.jface.viewers.ILabelProviderListener
import org.eclipse.jface.viewers.ITableLabelProvider
import org.eclipse.ui._

class CoverageSessionLabelProvider extends ITableLabelProvider {
	override def getColumnText(node: Any, columnIndex: Int) = node match {
		case coverageNode: CoverageSessionTreeNode => columnIndex match {
			case 0 => coverageNode.name
			case 1 => coverageNode.linesCovered.toString
			case 2 => coverageNode.linesTotal.toString
			case _ => "???"
		}
		case _ => "???"
	}

	override def getColumnImage(node: Any, columnIndex: Int) = columnIndex match {
		case 0 => node match {
			case coverageNode: CoverageSessionTreeNode => coverageNode.icon
			case _ => 
				PlatformUI.getWorkbench.getSharedImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK)
			}
		case _ => null
	}
	
	override def addListener(listener: ILabelProviderListener) = { /* not supported */ }
	override def removeListener(listener: ILabelProviderListener) = { /* not supported */ }
	override def isLabelProperty(node: Any, property: String) = false
	override def dispose = {}
}
