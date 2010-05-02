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

import ecobertura.ui.util.layout.FormDataBuilder

object IncludeExcludeClassesGroupBuilder {
  def forParent(parent: Composite) = new IncludeExcludeClassesGroupBuilder(parent)
}

class IncludeExcludeClassesGroupBuilder(parent: Composite) {

  private var includeExcludeGroup: Group = null
  private var tableHolder: Composite = null
  private var upButton: Button = null
  private var downButton: Button = null
  private var addIncludeButton: Button = null
  private var addExcludeButton: Button = null
  private var removeButton: Button = null
  
  def build = {
    initializeIncludeExcludeGroup
    buildBasicIncludeExcludeGroupLayout
    initializeIncludeExcludeTable
    initializeButtons
  }

  private def initializeIncludeExcludeGroup = {
    includeExcludeGroup = new Group(parent, SWT.NONE)
    includeExcludeGroup.setText("Classes to Include/Exclude:")
    includeExcludeGroup.setLayout(new FormLayout)
  }
  
  private def buildBasicIncludeExcludeGroupLayout = {
    val gridData = new GridData
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalAlignment = SWT.FILL
    gridData.grabExcessVerticalSpace = true
    gridData.verticalAlignment = SWT.FILL
    includeExcludeGroup.setLayoutData(gridData)
  }
 
  private def initializeIncludeExcludeTable = {
    tableHolder = new Composite(includeExcludeGroup, SWT.NONE)
    tableHolder.setLayout(new FillLayout)
    addTableTo(tableHolder)
    FormDataBuilder.forFormElement(tableHolder)
        .topAtPercent(0, 5).leftAtPercent(0, 5).bottomAtPercent(100, 5)
        .build
  }
 
  private def addTableTo(parent: Composite) = ClassFilterTable.forParent(parent).build
  
  private def initializeUpButton = {
    upButton = new Button(includeExcludeGroup, SWT.PUSH)
    upButton.setText("Up")
    FormDataBuilder.forFormElement(upButton)
        .topAtPercent(0, 5).rightNeighborOf(tableHolder, 5).rightAtPercent(100, 5)
        .build
  }

  private def initializeButtons = {
    initializeUpButton
    initializeDownButton
    initializeAddIncludeButton
    initializeAddExcludeButton
    initializeRemoveButton
  }
  
  private def initializeDownButton = {
    downButton = new Button(includeExcludeGroup, SWT.PUSH)
    downButton.setText("Down")
    FormDataBuilder.forFormElement(downButton)
        .bottomNeighborOf(upButton, 5).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
 }

  private def initializeAddIncludeButton = {
    addIncludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addIncludeButton.setText("Add Include Filter")
    FormDataBuilder.forFormElement(addIncludeButton)
        .bottomNeighborOf(downButton, 15).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
  }

  private def initializeAddExcludeButton = {
    addExcludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addExcludeButton.setText("Add Exclude Filter")
    FormDataBuilder.forFormElement(addExcludeButton)
        .bottomNeighborOf(addIncludeButton, 5).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
  }

  private def initializeRemoveButton = {
    removeButton = new Button(includeExcludeGroup, SWT.PUSH)
    removeButton.setText("Remove Filter")
    FormDataBuilder.forFormElement(removeButton)
        .bottomNeighborOf(addExcludeButton, 15).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
  }
}