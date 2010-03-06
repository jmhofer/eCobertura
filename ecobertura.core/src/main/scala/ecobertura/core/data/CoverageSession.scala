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
	def packageMap: Map[String, PackageCoverage]
	override def toString = String.format("CoverageSession%s", super.toString)
}

class CoberturaSessionImpl(projectData: ProjectData) extends CoverageSession {
	override val packages = {
		val packageSet = projectData.getPackages.asInstanceOf[TreeSet[PackageData]]
		
		packageSet.map(PackageCoverage.fromCoberturaPackageData(_)).toList
	}

	private var internalPackageMap = Map[String, PackageCoverage]()
	for (packageCov <- packages) internalPackageMap += packageCov.name -> packageCov
	
	override val packageMap = internalPackageMap
	
	override val linesCovered = projectData.getNumberOfCoveredLines
	override val linesTotal = projectData.getNumberOfValidLines
	override val branchesCovered = projectData.getNumberOfCoveredBranches
	override val branchesTotal = projectData.getNumberOfValidBranches
}
