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
	def branchesCovered: Int
	def branchesTotal: Int
	override def toString =
		String.format("(%d, %d, %d, %d)", int2Integer(linesCovered), 
				int2Integer(linesTotal), int2Integer(branchesCovered), int2Integer(branchesTotal))
}

object EmptyCoverageData extends CoverageData {
	override def linesCovered = 0
	override def linesTotal = 0
	override def branchesCovered = 0
	override def branchesTotal = 0
	override def toString = String.format("EmptyCoverageSession%s", super.toString)
}

trait CoverageSession extends CoverageData {
	def packages: List[PackageCoverage]
	override def toString = String.format("CoverageSession%s", super.toString)
}

class CoberturaSessionImpl(projectData: ProjectData) extends CoverageSession {
	override def packages = {
		val packageSet = projectData.getPackages.asInstanceOf[TreeSet[PackageData]]
		
		packageSet.map(PackageCoverage.fromCoberturaPackageData(_)).toList
	}
	
	override def linesCovered = projectData.getNumberOfCoveredLines
	override def linesTotal = projectData.getNumberOfValidLines
	override def branchesCovered = projectData.getNumberOfCoveredBranches
	override def branchesTotal = projectData.getNumberOfValidBranches
}
