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

import ecobertura.core.data.filters.ClassFilter

import org.eclipse.jface.viewers._

class ClassFilterTableContentProvider extends IStructuredContentProvider {
  override def getElements(inputElement: Any) : Array[Object] = 
    inputElement.asInstanceOf[Array[ClassFilter]].toArray
  
  override def inputChanged(viewer: Viewer, oldInput: Any, newInput: Any) = {}
  override def dispose = {}
}
