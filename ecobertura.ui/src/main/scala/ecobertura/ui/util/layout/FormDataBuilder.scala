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
package ecobertura.ui.util.layout

import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._

object FormDataBuilder {
  def forFormElement(formElement: Composite) = new FormDataBuilder(formElement)
}

class FormDataBuilder(formElement: Composite) {
  private val formData = new FormData
  
  def leftAtPercent(percent: Int, margin: Int) = { 
      formData.left = new FormAttachment(percent, margin)
      this
  }
  
  def rightAtPercent(percent: Int, margin: Int) = { 
      formData.right = new FormAttachment(percent, -margin)
      this
  }

  def topAtPercent(percent: Int, margin: Int) = {
      formData.top = new FormAttachment(percent, margin)
      this
  }

  def bottomAtPercent(percent: Int, margin: Int) = { 
      formData.bottom = new FormAttachment(percent, -margin)
      this
  }

  def leftNeighborOf(neighbor: Composite, margin: Int) = { 
      formData.right = new FormAttachment(neighbor, -margin)
      this
  }

  def rightNeighborOf(neighbor: Composite, margin: Int) = { 
      formData.left = new FormAttachment(neighbor, margin)
      this
  }

  def topNeighborOf(neighbor: Composite, margin: Int) = {
      formData.bottom = new FormAttachment(neighbor, -margin)
      this
  }

  def bottomNeighborOf(neighbor: Composite, margin: Int) = {
      formData.top = new FormAttachment(neighbor, margin)
      this
  }

  def build = {
    formElement.setLayoutData(formData)
  }
}