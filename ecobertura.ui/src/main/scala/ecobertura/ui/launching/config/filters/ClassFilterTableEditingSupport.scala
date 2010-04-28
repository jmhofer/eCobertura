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

object ClassFilterTableEditingSupport {
  def forViewerAndColumn(viewer: ColumnViewer, column: Int) = 
    new ClassFilterTableEditingSupport(viewer, column)
}

class ClassFilterTableEditingSupport(viewer: ColumnViewer, column: Int) 
    extends EditingSupport(viewer) {

  val swtTable = viewer.asInstanceOf[TableViewer].getTable
  val cellEditor = column match {
    case 0 => new ComboBoxCellEditor(swtTable, Array("include", "exclude"))
    case 1 => new TextCellEditor(swtTable)
  }
  
  override def getValue(element: Any) : Object = column match {
    case 0 => 0.asInstanceOf[Object]
    case 1 => element.toString
  }

  override def setValue(element: Any, value: Any) = {
    // TODO
    getViewer.update(element, null)
  }
  
  override def canEdit(element: Any) = true
  override def getCellEditor(element: Any) = cellEditor
}