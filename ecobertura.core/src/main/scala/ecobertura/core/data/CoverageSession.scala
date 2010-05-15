/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
 */
package ecobertura.core.data
import java.util.Date

import java.util.TreeSet
import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

trait CoverageSession extends CoverageData {
  def packages: List[PackageCoverage]
  def packageMap: Map[String, PackageCoverage]
  def createdOn: Date
  def displayName: String
  override def toString = String.format("CoverageSession%s", super.toString)
}

trait CoverageData {
  def linesCovered: Int
  def linesTotal: Int
  def branchesCovered: Int
  def branchesTotal: Int
  override def toString = "(%d, %d, %d, %d)".format(
      linesCovered, linesTotal, branchesCovered, branchesTotal)
}

object CoverageSession {
  def fromCoberturaProjectData(projectData: ProjectData): CoverageSession =
      new CoberturaSessionImpl(projectData)

  private class CoberturaSessionImpl(projectData: ProjectData) extends CoverageSession {
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
    
    override val createdOn = new Date
    override val displayName = "%1$tF %<tT".format(createdOn)
  }
}

object EmptyCoverageData extends CoverageData {
  override def linesCovered = 0
  override def linesTotal = 0
  override def branchesCovered = 0
  override def branchesTotal = 0
  override def toString = "EmptyCoverageSession%s".format(super.toString)
}
