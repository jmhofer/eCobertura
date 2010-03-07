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
	def sourceFileLines: Map[String, List[LineCoverage]]
	override def toString = String.format("PackageCoverage(%s)%s", name, super.toString)
}

class CoberturaPackageData(packageData: PackageData) extends PackageCoverage {
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
