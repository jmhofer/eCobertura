/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.core.data

import java.util.TreeSet
import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

trait PackageCoverage extends CoverageData {
  def name: String
  def classes: List[ClassCoverage]
  def sourceFileLines: Map[String, List[LineCoverage]]
  override def toString = "PackageCoverage(%s)%s".format(name, super.toString)
}

object PackageCoverage {
  def fromCoberturaPackageData(packageData: PackageData): PackageCoverage =
      new CoberturaPackageData(packageData)

  private class CoberturaPackageData(packageData: PackageData) extends PackageCoverage {
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
}
