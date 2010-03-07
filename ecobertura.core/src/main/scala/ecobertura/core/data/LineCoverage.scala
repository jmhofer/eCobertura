/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
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
package ecobertura.core.data

import net.sourceforge.cobertura.coveragedata.LineData
import net.sourceforge.cobertura.coveragedata.{CoverageData => CoberturaCoverageData}

object LineCoverage {
	def fromLineDataSet(lineDataList: List[CoberturaCoverageData]) =
		for {
			singleCoverageData <- lineDataList if singleCoverageData.isInstanceOf[LineData]
		} yield fromSingleLineData(singleCoverageData.asInstanceOf[LineData])
	
	def fromSingleLineData(lineData: LineData): LineCoverage =
		new LineCoverageImpl(lineData)
}

trait LineCoverage {
	def lineNumber: Int
	def hits: Long
	def isCovered = hits > 0
	
	override def toString = String.format("LineCoverage(%d, %d)", int2Integer(lineNumber), long2Long(hits))
}

class LineCoverageImpl(lineData: LineData) extends LineCoverage {
	override def lineNumber = lineData.getLineNumber
	override def hits = lineData.getHits
}
