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
import org.eclipse.swt.widgets._
import org.eclipse.swt.layout._
import org.eclipse.jface.viewers._

import ecobertura.core.data.filters._
import ecobertura.ui.util.layout.FormDataBuilder
import ecobertura.ui.util.Predef._

object IncludeExcludeClassesGroupBuilder {
  def forParent(parent: Composite) = new IncludeExcludeClassesGroupBuilder(parent)
}

class IncludeExcludeClassesGroupBuilder(parent: Composite) {

  private var includeExcludeGroup: Group = null
  private var includeExcludeTable: TableViewer = null
  private var tableHolder: Composite = null
  
  def build = {
    includeExcludeGroup = initializeIncludeExcludeGroup
    buildBasicIncludeExcludeGroupLayout(includeExcludeGroup)
    tableHolder = initializeIncludeExcludeTableHolder(includeExcludeGroup)
    includeExcludeTable = initializeIncludeExcludeTableIn(tableHolder)
    initializeButtons
  }

  private def initializeIncludeExcludeGroup = {
    val includeExcludeGroup = new Group(parent, SWT.NONE)
    includeExcludeGroup.setText("Classes to Include/Exclude:")
    includeExcludeGroup.setLayout(new FormLayout)
    includeExcludeGroup
  }
  
  private def buildBasicIncludeExcludeGroupLayout(group: Composite) = {
    val gridData = new GridData
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalAlignment = SWT.FILL
    gridData.grabExcessVerticalSpace = true
    gridData.verticalAlignment = SWT.FILL
    group.setLayoutData(gridData)
  }
 
  private def initializeIncludeExcludeTableHolder(parent: Composite) = {
    val tableHolder = new Composite(parent, SWT.NONE)
    tableHolder.setLayout(new FillLayout)
    FormDataBuilder.forFormElement(tableHolder)
        .topAtPercent(0, 5).leftAtPercent(0, 5).bottomAtPercent(100, 5)
        .build
    tableHolder
  }
 
  private def initializeIncludeExcludeTableIn(parent: Composite) =
      ClassFilterTable.forParent(parent).build
  
  private def initializeButtons = {
    val addIncludeButton = initializeAddIncludeButton
    val addExcludeButton = initializeAddExcludeButton(addIncludeButton)
    val removeButton = initializeRemoveButton(addExcludeButton)
  }

  private def initializeAddIncludeButton = {
    val addIncludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addIncludeButton.setText("Add Include Filter")
    FormDataBuilder.forFormElement(addIncludeButton)
        .topAtPercent(0, 5).rightNeighborOf(tableHolder, 5).rightAtPercent(100, 5)
        .build
    addIncludeButton.addSelectionListener { 
      addAndEditClassFilterPattern(new ClassFilter(IncludeFilter, "*"))
    }
    addIncludeButton
  }

  private def initializeAddExcludeButton(addIncludeButton: Control) = {
    val addExcludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addExcludeButton.setText("Add Exclude Filter")
    FormDataBuilder.forFormElement(addExcludeButton)
        .bottomNeighborOf(addIncludeButton, 5).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
    addExcludeButton.addSelectionListener { 
      addAndEditClassFilterPattern(new ClassFilter(ExcludeFilter, "*"))
    }
    addExcludeButton
  }

  private def addAndEditClassFilterPattern(classFilter: ClassFilter) = { 
    includeExcludeTable.add(classFilter)
    includeExcludeTable.editElement(classFilter, 1)
  }

  private def initializeRemoveButton(addExcludeButton: Control) = {
    val removeButton = new Button(includeExcludeGroup, SWT.PUSH)
    removeButton.setText("Remove Filter")
    FormDataBuilder.forFormElement(removeButton)
        .bottomNeighborOf(addExcludeButton, 15).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
    removeButton.addSelectionListener {
        val swtTable = includeExcludeTable.getTable
        swtTable.remove(swtTable.getSelectionIndex)
    }
    removeButton
  }
}
