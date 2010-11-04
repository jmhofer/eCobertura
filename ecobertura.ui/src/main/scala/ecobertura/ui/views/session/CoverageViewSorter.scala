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
