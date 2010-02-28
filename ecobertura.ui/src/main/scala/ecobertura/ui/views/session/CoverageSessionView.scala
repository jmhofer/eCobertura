package ecobertura.ui.views.session

import java.util.logging.Logger

import org.eclipse.swt.widgets._
import org.eclipse.swt.SWT;

import org.eclipse.jface.layout.TreeColumnLayout
import org.eclipse.jface.viewers._

import org.eclipse.ui._
import org.eclipse.ui.part._

import ecobertura.ui.util.Predef._

object CoverageSessionView {
	/**
	 * The ID of the view as specified by the extension.
	 */
	def ID = "ecobertura.ui.views.session.CoverageSessionView"
		
	private val logger = Logger.getLogger("ecobertura.ui.views.session") //$NON-NLS-1$
}

class CoverageSessionView extends ViewPart {
	import CoverageSessionView.logger
	
	private var viewer: TreeViewer = null

	class NameSorter extends ViewerSorter
	
	/**
	 * This is a callback that will allow us to create the viewer and initialize it.
	 */
	override def createPartControl(parent: Composite) = {
		val swtTreeTable = new Tree(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL)
		swtTreeTable.setHeaderVisible(true)
		swtTreeTable.setLinesVisible(true)
		
		viewer = new TreeViewer(swtTreeTable)
		val treeColumnLayout = new TreeColumnLayout
		parent.setLayout(treeColumnLayout)
		
		addTreeColumn("Name", SWT.LEFT, 8)
		addTreeColumn("Lines", SWT.RIGHT, 1)
		addTreeColumn("Total", SWT.RIGHT, 1)
		
		viewer.setContentProvider(CoverageSessionModel.get)
		viewer.setLabelProvider(new CoverageSessionLabelProvider)
		viewer.setSorter(new NameSorter)
		viewer.setInput(CoverageSessionRoot)
		viewer.expandAll
		
		CoverageSessionModel.get.addListener(new CoverageSessionListener {
			override def sessionReset = {
				logger.fine("Viewer has received sessionReset event")
				viewer.setInput(CoverageSessionRoot)
			}
		})

		// Create the help context id for the viewer's control
		// PlatformUI.getWorkbench.getHelpSystem.setHelp(viewer.getControl, "ecobertura.ui.viewer")

		def addTreeColumn(name: String, alignment: Int, weight: Int) {
			val column = new TreeColumn(swtTreeTable, alignment)
			column.setText(name)
			column.setAlignment(alignment)
			treeColumnLayout.setColumnData(column, new ColumnWeightData(weight))
		}
	}

	
	/**
	 * Passing the focus request to the viewer's control.
	 */
	override def setFocus = {
		viewer.getControl.setFocus
	}
}
