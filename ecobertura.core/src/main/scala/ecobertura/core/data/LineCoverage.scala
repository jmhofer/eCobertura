/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.core.data

import net.sourceforge.cobertura.coveragedata.LineData
import net.sourceforge.cobertura.coveragedata.{CoverageData => CoberturaCoverageData}

object LineCoverage {
  def fromLineDataSet(lineDataList: List[CoberturaCoverageData]) = 
    for {
      singleCoverageData <- lineDataList if singleCoverageData.isInstanceOf[LineData]
    } yield fromSingleLineData(singleCoverageData.asInstanceOf[LineData])
    
  def fromSingleLineData(lineData: LineData): LineCoverage = new LineCoverageImpl(lineData)
  
  private class LineCoverageImpl(lineData: LineData) extends LineCoverage {
    override def lineNumber = lineData.getLineNumber
    override def hits = lineData.getHits
  }
}

trait LineCoverage {
  def lineNumber: Int
  def hits: Long
  def isCovered = hits > 0
  
  override def toString = "LineCoverage(%d, %d)".format(lineNumber, hits)
}
