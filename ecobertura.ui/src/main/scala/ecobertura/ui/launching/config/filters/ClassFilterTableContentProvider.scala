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

import ecobertura.core.data.filters._

import org.eclipse.jface.viewers._

class ClassFilterTableContentProvider extends IStructuredContentProvider {
  override def getElements(inputElement: Any) : Array[AnyRef] = 
    inputElement.asInstanceOf[ClassFilters].toArray.asInstanceOf[Array[AnyRef]]
  
  override def inputChanged(viewer: Viewer, oldInput: Any, newInput: Any) = {}
  override def dispose = {}
}
