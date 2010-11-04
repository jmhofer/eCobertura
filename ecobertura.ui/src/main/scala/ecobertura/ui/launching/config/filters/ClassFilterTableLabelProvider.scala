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

import ecobertura.core.data.filters._

class ClassFilterTableLabelProvider extends LabelProvider with ITableLabelProvider {
  override def getColumnImage(element: Any, index: Int) = null
  override def getColumnText(element: Any, index: Int) = {
    val classFilter = element.asInstanceOf[ClassFilter]
    index match {
      case 0 => classFilter.kind.asLabel
      case 1 => classFilter.pattern
    }
  }
}
