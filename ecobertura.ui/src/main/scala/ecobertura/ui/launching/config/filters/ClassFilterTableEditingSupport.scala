/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
