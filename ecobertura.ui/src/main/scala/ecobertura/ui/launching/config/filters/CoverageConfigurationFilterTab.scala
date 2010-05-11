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
import org.eclipse.swt.widgets.{Composite, Label}
import org.eclipse.jface.viewers.TableViewer

import org.eclipse.debug.ui.AbstractLaunchConfigurationTab
import org.eclipse.debug.core._

import ecobertura.core.data.filters._

// TODO call setDirty and updateConfigurationDialog when changes are being made...

class CoverageConfigurationFilterTab extends AbstractLaunchConfigurationTab 
    with FilterChangeListener {
  
  override def getName = "Filters"

  override def performApply(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    // TODO
  }
  
  override def setDefaults(workingCopyOfLaunchConfiguration: ILaunchConfigurationWorkingCopy) = {
    val initialFilters = List[ClassFilter](ClassFilter(IncludeFilter, "*"))
    LaunchConfigurationFilters.addFiltersToLaunchConfiguration(
        initialFilters, workingCopyOfLaunchConfiguration)
  }
  
  override def initializeFrom(launchConfiguration: ILaunchConfiguration) = {
    import scala.collection.JavaConversions._
    val classFilterList = launchConfiguration.getAttribute("classFilters", 
        new java.util.ArrayList[String])
        
    classFilterList foreach { value: Any => {
        println(ClassFilter(value.toString))
        // TODO
      }
    }
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
      IncludeExcludeClassesGroupBuilder.forParent(panel).withChangeListener(this).build()
      
  override def filtersChanged(viewer: TableViewer) = {
    println("filters changed")
    setDirty(true)
    updateLaunchConfigurationDialog()
  }
}