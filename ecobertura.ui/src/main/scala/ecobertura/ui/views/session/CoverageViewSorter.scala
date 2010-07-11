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
package ecobertura.ui.views.session

import org.eclipse.jface.viewers._

// TODO handle sort oder and column type changes...
class CoverageViewSorter extends ViewerSorter {
  import ColumnType._
  
  var columnType = Name
  
  override def compare(viewer: Viewer, first: Any, second: Any) = {
    (first, second) match {
      case (firstNode: CoverageSessionTreeNode, secondNode: CoverageSessionTreeNode) => {
        firstNode.name.compareTo(secondNode.name)
      }
      case _ => first.toString.compareTo(second.toString)
    }
  }
}

