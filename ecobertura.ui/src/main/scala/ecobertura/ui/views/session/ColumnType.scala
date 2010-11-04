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

object ColumnType extends Enumeration {
  type ColumnType = Value
  
  val Name = Value
  val CoveredLines, TotalLines, LinesPercentage = Value
  val CoveredBranches, TotalBranches, BranchesPercentage = Value
}