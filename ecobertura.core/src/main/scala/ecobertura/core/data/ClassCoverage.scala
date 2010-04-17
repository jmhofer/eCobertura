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

import net.sourceforge.cobertura.coveragedata._
import scala.collection.JavaConversions._

object ClassCoverage {
	def fromCoberturaClassData(classData: ClassData): ClassCoverage = {
		new CoberturaClassData(classData)
	}
}

trait ClassCoverage extends CoverageData {
	def name: String
	def packageName: String
	def sourceFileName: String
	def lines: List[LineCoverage]
	
	override def toString = "ClassCoverage(%s, %s)%s".format(name, sourceFileName, super.toString)
}

class CoberturaClassData(classData: ClassData) extends ClassCoverage {
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
