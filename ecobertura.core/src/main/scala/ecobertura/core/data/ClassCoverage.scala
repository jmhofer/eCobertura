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

import net.sourceforge.cobertura.coveragedata._
import scala.collection.JavaConversions._

object ClassCoverage {
  def fromCoberturaClassData(classData: ClassData): ClassCoverage =
      new CoberturaClassData(classData)
}

trait ClassCoverage extends CoverageData {
  def name: String
  def packageName: String
  def sourceFileName: String
  def lines: List[LineCoverage]
  
  override def toString = "ClassCoverage(%s, %s)%s".format(name, sourceFileName, super.toString)
}

class CoberturaClassData private[data] (classData: ClassData) extends ClassCoverage {
  override val name = classData.getBaseName
  override val packageName = classData.getPackageName
  override val sourceFileName = classData.getSourceFileName.substring(
      classData.getSourceFileName.lastIndexOf("/") + 1)
  
  override val linesCovered = classData.getNumberOfCoveredLines
  override val linesTotal = classData.getNumberOfValidLines
  override val branchesCovered = classData.getNumberOfCoveredBranches
  override val branchesTotal = classData.getNumberOfValidBranches
  
  override val lines = LineCoverage.fromLineDataSet(classData.getLines.toList)
}
