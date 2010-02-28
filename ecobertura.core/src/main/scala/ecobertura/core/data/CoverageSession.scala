package ecobertura.core.data

import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

object CoverageSession {
	def fromCoberturaProjectData(projectData: ProjectData): CoverageSession = {
		new CoberturaSessionImpl(projectData)
	}
}

trait CoverageSession {
	def packages: Set[PackageCoverage]
}

class CoberturaSessionImpl(projectData: ProjectData) extends CoverageSession {
	override def packages = {
		val packageSet = projectData.getPackages.asInstanceOf[Set[PackageData]]
		
		// FIXME can't convert java.util.TreeSet to scala.collection.immutable.Set
		packageSet.map(PackageCoverage.fromCoberturaPackageData(_))
	}
}
