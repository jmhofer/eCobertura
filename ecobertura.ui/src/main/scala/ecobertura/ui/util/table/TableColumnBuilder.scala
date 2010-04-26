package ecobertura.ui.util.table

import org.eclipse.swt.SWT
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._
import org.eclipse.jface.viewers._

object TableColumnBuilder {
  def forTableViewer(tableViewer: TableViewer) = {
    new TableColumnBuilder(tableViewer)
  }
}

class TableColumnBuilder(tableViewer: TableViewer) {
  private var tableLayout: Option[TableLayout] = None
  private var title: Option[String] = None
  
  private var alignment: Int = SWT.LEFT
  private var moveable: Boolean = true
  private var resizable: Boolean = true
  
  def withLayout(tableLayout: TableLayout) = {
    if (tableLayout == null) throw new NullPointerException("null layout not allowed")
    this.tableLayout = Some(tableLayout)
    this
  }
  
  def aligned(alignment: Int) = {
    this.alignment = alignment
    this
  }
  
  def titled(title: String) = {
    if (title == null) throw new NullPointerException("null title not allowed")
    this.title = Some(title)
    this
  }
  
  def notMoveable = {
    moveable = false
    this
  }
  
  def notResizable = {
    resizable = false
    this
  }
  
  def withWeightAndMinimumSize(weight: Int, minimumSize: Int) = {
    tableLayout match {
      case Some(tableLayout) => tableLayout.addColumnData(new ColumnWeightData(weight, minimumSize))
      case None => throw new IllegalStateException("please define a table layout first")
    }
    this
  }
  
  def build  = {
    val swtTable = tableViewer.getTable
    val tableViewerColumn = new TableViewerColumn(tableViewer, alignment)
    val swtColumn = tableViewerColumn.getColumn
    
    tableLayout match {
      case Some(tableLayout) => swtTable.setLayout(tableLayout)
      case None => /* nothing to do */
    }
    
    title match {
      case Some(title) => swtColumn.setText(title)
      case None => /* nothing to do */
    }
    
    swtColumn.setResizable(resizable)
    swtColumn.setMoveable(moveable)
    
    tableViewerColumn
  }
}