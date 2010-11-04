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

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.{List => _, _}
import org.eclipse.jface.viewers._

import ecobertura.core.data.filters._
import ecobertura.ui.util.table.TableColumnBuilder

object ClassFilterTable {
  def forParentAndFilters(parent: Composite) = new ClassFilterTable(parent)
}

class ClassFilterTable private (parent: Composite) {
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
        .setEditingSupport(ClassFilterTableEditingSupport
            .forViewerAndColumn(classFilterTable, 0)
            .withChangeListener(listener))
     
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
  }
}
