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
	override def toString = String.format("ClassCoverage(%s)%s", name, super.toString)
}

class CoberturaClassData(classData: ClassData) extends ClassCoverage {
	override def name = classData.getBaseName
	
	override def linesCovered = classData.getNumberOfCoveredLines
	override def linesTotal = classData.getNumberOfValidLines
	override def branchesCovered = classData.getNumberOfCoveredBranches
	override def branchesTotal = classData.getNumberOfValidBranches
}
