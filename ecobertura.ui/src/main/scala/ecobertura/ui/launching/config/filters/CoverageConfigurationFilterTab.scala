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

import scala.collection.mutable.ListBuffer

import org.eclipse.swt.SWT
import org.eclipse.swt.layout._
import org.eclipse.swt.widgets.{Composite, Label}
import org.eclipse.jface.viewers._

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.debug.core._

import ecobertura.core.data.filters._

class CoverageConfigurationFilterTab extends AbstractLaunchConfigurationTab 
    with FilterChangeListener {
  
  override def getName = "Filters"
    
  private var classFilterTableViewer: TableViewer = _

  override def performApply(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    classFilters.addToLaunchConfiguration(workingCopyOfLaunchConfiguration)
  }
  
  private def classFilters = classFilterTableViewer.getInput.asInstanceOf[ClassFilters]
  
  override def setDefaults(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    ClassFilters(ClassFilter(IncludeFilter, "*")).addToLaunchConfiguration(
        workingCopyOfLaunchConfiguration)
  }
  
  override def initializeFrom(launchConfiguration: ILaunchConfiguration) = {
    classFilterTableViewer.setInput(ClassFilters(launchConfiguration))
  }
  
  override def createControl(parent: Composite) = {
    val panel = new Composite(parent, SWT.NONE)
    setControl(panel)

    prepareLayout(panel)
    addDescriptionLabelTo(panel)
    addIncludeExcludeClassesGroupTo(panel)

    // TODO add line ignore regexes
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
  
  private def addIncludeExcludeClassesGroupTo(panel: Composite) =
      classFilterTableViewer = IncludeExcludeClassesGroupBuilder.forParent(panel)
          .withChangeListener(this).build()
      
  override def filtersChanged(viewer: TableViewer) = {
    setDirty(true)
    setErrorMessage(null)
    updateLaunchConfigurationDialog()
  }
}
