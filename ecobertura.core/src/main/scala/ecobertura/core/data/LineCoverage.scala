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
}

class LineCoverageImpl(lineData: LineData) extends LineCoverage {
	override def lineNumber = lineData.getLineNumber
	override def hits = lineData.getHits
}
