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
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.debug.core._

import ecobertura.ui.util.layout.FormDataBuilder
 
class CoverageConfigurationFilterTab extends AbstractLaunchConfigurationTab {
  override def getName = "Filters"
  
  override def performApply(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    // TODO
  }
  
  override def setDefaults(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    // TODO
  }
  
  override def initializeFrom(launchConfiguration: ILaunchConfiguration) = {
    // TODO
  }
  
  override def createControl(parent: Composite) = {
    val panel = new Composite(parent, SWT.NONE)
    setControl(panel)

    prepareLayout(panel)
    addDescriptionLabelTo(panel)
    addIncludeExcludeClassesGroupTo(panel)
    // TODO
  }
  
  private def prepareLayout(panel: Composite) = {
    val layout = new GridLayout
    layout.verticalSpacing = 15;
    panel.setLayout(layout)
  }
  
  private def addDescriptionLabelTo(panel: Composite) = {
    val label = new Label(panel, SWT.WRAP)
    label.setText("Specify include/exclude filters here concerning which classes should be " +
            "instrumented.")
  }
  
  private def addIncludeExcludeClassesGroupTo(panel: Composite) = {
    val includeExcludeGroup = new Group(panel, SWT.NONE)
    includeExcludeGroup.setText("Classes to Include/Exclude:")
    
    val gridData = new GridData
    gridData.grabExcessHorizontalSpace = true
    gridData.horizontalAlignment = SWT.FILL
    gridData.grabExcessVerticalSpace = true
    gridData.verticalAlignment = SWT.FILL
    includeExcludeGroup.setLayoutData(gridData)

    includeExcludeGroup.setLayout(new FormLayout)

    val tableHolder = new Composite(includeExcludeGroup, SWT.NONE)
    tableHolder.setLayout(new FillLayout)
    addTableTo(tableHolder)

    FormDataBuilder.forFormElement(tableHolder)
        .topAtPercent(0, 5).leftAtPercent(0, 5).bottomAtPercent(100, 5)
        .build

    val upButton = new Button(includeExcludeGroup, SWT.PUSH)
    upButton.setText("Up")
    
    FormDataBuilder.forFormElement(upButton)
        .topAtPercent(0, 5).rightNeighborOf(tableHolder, 5).rightAtPercent(100, 5)
        .build

    val downButton = new Button(includeExcludeGroup, SWT.PUSH)
    downButton.setText("Down")
    
    FormDataBuilder.forFormElement(downButton)
        .bottomNeighborOf(upButton, 5).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build

    val addIncludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addIncludeButton.setText("Add Include Filter")
    
    FormDataBuilder.forFormElement(addIncludeButton)
        .bottomNeighborOf(downButton, 15).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build

    val addExcludeButton = new Button(includeExcludeGroup, SWT.PUSH)
    addExcludeButton.setText("Add Exclude Filter")

    FormDataBuilder.forFormElement(addExcludeButton)
        .bottomNeighborOf(addIncludeButton, 5).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
    
    val removeButton = new Button(includeExcludeGroup, SWT.PUSH)
    removeButton.setText("Remove Filter")

    FormDataBuilder.forFormElement(removeButton)
        .bottomNeighborOf(addExcludeButton, 15).rightNeighborOf(tableHolder, 5)
        .rightAtPercent(100, 5).build
  }

  private def addTableTo(parent: Composite) = ClassFilterTable.forParent(parent).build
}