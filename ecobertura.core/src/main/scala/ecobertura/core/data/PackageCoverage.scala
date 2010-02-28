package ecobertura.core.data

import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

object PackageCoverage {
	def fromCoberturaPackageData(packageData: PackageData): PackageCoverage = {
		new CoberturaPackageData(packageData)
	}
}

trait PackageCoverage {
	def name: String
	def classes: Set[ClassCoverage]
}

class CoberturaPackageData(packageData: PackageData) extends PackageCoverage {
	override def name = packageData.getName
	
	override def classes = {
		val classSet = packageData.getClasses.asInstanceOf[Set[ClassData]]
		
		// FIXME can't convert java.util.TreeSet to scala.collection.immutable.Set
		classSet.map(ClassCoverage.fromCoberturaClassData(_))
	}
}
