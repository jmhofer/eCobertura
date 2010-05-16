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

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.{List => _, _}
import org.eclipse.jface.viewers._

import ecobertura.core.data.filters._
import ecobertura.ui.util.table.TableColumnBuilder

object ClassFilterTable {
  def forParentAndFilters(parent: Composite, classFilters: ClassFilters) = 
      new ClassFilterTable(parent, classFilters)
}

class ClassFilterTable(parent: Composite, classFilters: ClassFilters) {
  private var listener: FilterChangeListener = _
  
  def withChangeListener(listener: FilterChangeListener) = {
    this.listener = listener
    this
  }
  
  def build() = {
    val classFilterTable = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION)

    configureTable(classFilterTable)
    addColumns(classFilterTable)
    configureTableModel(classFilterTable)
    
    classFilterTable
  }
  
  private def configureTable(classFilterTable: TableViewer) = {
    val swtTable = classFilterTable.getTable
    classFilterTable.getTable.setHeaderVisible(true)
    classFilterTable.getTable.setLinesVisible(true)
  }
  
  private def addColumns(classFilterTable: TableViewer) = {
    val tableLayout = new TableLayout
    
    TableColumnBuilder.forTableViewer(classFilterTable).aligned(SWT.LEFT)
        .titled("Kind").notMoveable
        .withLayout(tableLayout).withWeightAndMinimumSize(0, 100)
        .build
        .setEditingSupport(ClassFilterTableEditingSupport.forViewerAndColumn(classFilterTable, 0))
     
    TableColumnBuilder.forTableViewer(classFilterTable).aligned(SWT.LEFT)
        .titled("Type Pattern").notMoveable
        .withLayout(tableLayout).withWeightAndMinimumSize(100, 200)
        .build
        .setEditingSupport(ClassFilterTableEditingSupport
            .forViewerAndColumn(classFilterTable, 1)
            .withChangeListener(listener))
        
    classFilterTable.getTable.setLayout(tableLayout)
  }
  
  private def configureTableModel(classFilterTable: TableViewer) = {
    classFilterTable.setLabelProvider(new ClassFilterTableLabelProvider)
    classFilterTable.setContentProvider(new ClassFilterTableContentProvider)

    classFilterTable.setInput(classFilters)
  }
}
