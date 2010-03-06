package ecobertura.core.data

import net.sourceforge.cobertura.coveragedata._
import scala.collection.JavaConversions._

object ClassCoverage {
	def fromCoberturaClassData(classData: ClassData): ClassCoverage = {
		new CoberturaClassData(classData)
	}
}

trait ClassCoverage extends CoverageData {
	def name: String
	def packageName: String
	def sourceFileName: String
	def lines: List[LineCoverage]
	
	override def toString = String.format("ClassCoverage(%s)%s", name, super.toString)
}

class CoberturaClassData(classData: ClassData) extends ClassCoverage {
	override val name = classData.getBaseName
	override val packageName = classData.getPackageName
	override val sourceFileName = classData.getSourceFileName.substring(
			classData.getSourceFileName.lastIndexOf("/") + 1)
	
	override val linesCovered = classData.getNumberOfCoveredLines
	override val linesTotal = classData.getNumberOfValidLines
	override val branchesCovered = classData.getNumberOfCoveredBranches
	override val branchesTotal = classData.getNumberOfValidBranches
	
	override val lines = LineCoverage.fromLineDataSet(classData.getLines.toList)
}
