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

class CoverageViewSorter extends ViewerSorter {
  import ColumnType._
  
  object SortDirection extends Enumeration {
    type SortDirection = Value
    val Ascending, Descending = Value
  }
  import SortDirection._

  private var columnType = Name
  private var sortDirection = Descending
  
  def setColumnType(columnType: ColumnType) = {
    if (this.columnType == columnType) sortDirection = reversedSortDirection
    else this.columnType = columnType
  }
  
  private def reversedSortDirection = sortDirection match {
      case Ascending => Descending
      case Descending => Ascending
  }

  override def compare(viewer: Viewer, first: Any, second: Any) = {
    (first, second) match {
      case (firstNode: CoverageSessionTreeNode, secondNode: CoverageSessionTreeNode) => {
        compareTreeNodes(firstNode, secondNode)
      }
      case _ => first.toString.compareTo(second.toString)
    }
  }
  
  private def compareTreeNodes(
      firstNode: CoverageSessionTreeNode, secondNode: CoverageSessionTreeNode) = {
    val firstCoverage = firstNode.coverageData
    val secondCoverage = secondNode.coverageData
    
    val comparisonValue = columnType match {
      case Name => firstNode.name.compareTo(secondNode.name)
      case CoveredLines => intWrapper(firstCoverage.linesCovered).compareTo(secondCoverage.linesCovered) 
      case TotalLines => intWrapper(firstCoverage.linesTotal).compareTo(secondCoverage.linesTotal)
      case LinesPercentage => intWrapper(firstCoverage.linesCovered).compareTo(secondCoverage.linesCovered)
      case CoveredBranches => intWrapper(firstCoverage.branchesCovered).compareTo(secondCoverage.branchesCovered) 
      case TotalBranches => intWrapper(firstCoverage.branchesTotal).compareTo(secondCoverage.branchesTotal)
      case BranchesPercentage => intWrapper(firstCoverage.branchesCovered).compareTo(secondCoverage.branchesCovered)
    }
   
    if (sortDirection == Descending) comparisonValue else -comparisonValue
  }
}
