package ecobertura.core.data

import java.util.TreeSet
import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

object PackageCoverage {
	def fromCoberturaPackageData(packageData: PackageData): PackageCoverage = {
		new CoberturaPackageData(packageData)
	}
}

trait PackageCoverage extends CoverageData {
	def name: String
	def classes: List[ClassCoverage]
	def sourceFileLines: Map[String, List[LineCoverage]]
	override def toString = String.format("PackageCoverage(%s)%s", name, super.toString)
}

class CoberturaPackageData(packageData: PackageData) extends PackageCoverage {
	override val name = packageData.getName
	
	override val classes = {
		val classSet = packageData.getClasses.asInstanceOf[TreeSet[ClassData]]
		classSet.map(ClassCoverage.fromCoberturaClassData(_)).toList
	}
	
	private var internalSourceFileLines = Map[String, List[LineCoverage]]()
	for (classCoverage <- classes) {
		internalSourceFileLines += classCoverage.sourceFileName -> 
			(internalSourceFileLines.get(classCoverage.sourceFileName) match {
				case Some(list) => list ++ classCoverage.lines
				case None => classCoverage.lines
			})
	}
	val sourceFileLines = internalSourceFileLines
	
	override val linesCovered = packageData.getNumberOfCoveredLines
	override val linesTotal = packageData.getNumberOfValidLines
	override val branchesCovered = packageData.getNumberOfCoveredBranches
	override val branchesTotal = packageData.getNumberOfValidBranches
}
