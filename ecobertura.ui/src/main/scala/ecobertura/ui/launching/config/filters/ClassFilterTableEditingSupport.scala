/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
 */
package ecobertura.ui.launching.config.filters

import org.eclipse.jface.viewers._
import org.eclipse.swt.SWT

import ecobertura.core.data.filters._

object ClassFilterTableEditingSupport {
  def forViewerAndColumn(viewer: TableViewer, column: Int) = 
    new ClassFilterTableEditingSupport(viewer, column)
}

class ClassFilterTableEditingSupport(viewer: TableViewer, column: Int) 
    extends EditingSupport(viewer) {

  private var listener: Option[FilterChangeListener] = None
  
  val swtTable = viewer.asInstanceOf[TableViewer].getTable
  val cellEditor = column match {
    case 0 => new ComboBoxCellEditor(swtTable, Array("include", "exclude"), SWT.READ_ONLY)
    case 1 => new TextCellEditor(swtTable)
  }
  
  def withChangeListener(listener: FilterChangeListener) = {
    this.listener = Some(listener)
    this
  }
 
  override def getValue(element: Any) : Object = {
    val classFilter = element.asInstanceOf[ClassFilter]
    column match {
      case 0 => classFilter.kind.toIndex.asInstanceOf[Object]
      case 1 => classFilter.pattern
    }
  }

  override def setValue(element: Any, value: Any) = {
    val filterToUpdate = element.asInstanceOf[ClassFilter]
    column match {
      case 0 => filterToUpdate.kind = KindOfFilter.fromIndex(value.asInstanceOf[Int])
      case 1 => filterToUpdate.pattern = value.asInstanceOf[String]
    }
    getViewer.update(element, null)
    listener match {
      case Some(listener) => listener.filtersChanged(viewer)
      case None => /* nothing to do */
    }
  }
  
  override def canEdit(element: Any) = true
  override def getCellEditor(element: Any) = cellEditor
}
