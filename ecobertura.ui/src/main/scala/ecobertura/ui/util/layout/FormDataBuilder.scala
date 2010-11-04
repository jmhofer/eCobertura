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
package ecobertura.ui.util.layout

import org.eclipse.swt.layout._
import org.eclipse.swt.widgets._

object FormDataBuilder {
  def forFormElement(formElement: Control) = new FormDataBuilder(formElement)
}

class FormDataBuilder(formElement: Control) {
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

  def leftNeighborOf(neighbor: Control, margin: Int) = { 
      formData.right = new FormAttachment(neighbor, -margin)
      this
  }

  def rightNeighborOf(neighbor: Control, margin: Int) = { 
      formData.left = new FormAttachment(neighbor, margin)
      this
  }

  def topNeighborOf(neighbor: Control, margin: Int) = {
      formData.bottom = new FormAttachment(neighbor, -margin)
      this
  }

  def bottomNeighborOf(neighbor: Control, margin: Int) = {
      formData.top = new FormAttachment(neighbor, margin)
      this
  }

  def build = {
    formElement.setLayoutData(formData)
  }
}