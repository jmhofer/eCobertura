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
}

class CoberturaPackageData(packageData: PackageData) extends PackageCoverage {
	override def name = packageData.getName
	
	override def classes = {
		val classSet = packageData.getClasses.asInstanceOf[TreeSet[ClassData]]
		
		classSet.map(ClassCoverage.fromCoberturaClassData(_)).toList
	}
	
	override def linesCovered = packageData.getNumberOfCoveredLines
	override def linesTotal = packageData.getNumberOfValidLines
}
