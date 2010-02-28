package ecobertura.core.data

import java.util.TreeSet
import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

object CoverageSession {
	def fromCoberturaProjectData(projectData: ProjectData): CoverageSession = {
		new CoberturaSessionImpl(projectData)
	}
}

trait CoverageData {
	def linesCovered: Int
	def linesTotal: Int
}

trait CoverageSession extends CoverageData {
	def packages: List[PackageCoverage]
}

class CoberturaSessionImpl(projectData: ProjectData) extends CoverageSession {
	override def packages = {
		val packageSet = projectData.getPackages.asInstanceOf[TreeSet[PackageData]]
		
		packageSet.map(PackageCoverage.fromCoberturaPackageData(_)).toList
	}
	
	override def linesCovered = projectData.getNumberOfCoveredLines
	override def linesTotal = projectData.getNumberOfValidLines
}
