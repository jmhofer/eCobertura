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
package ecobertura.ui.views.session

import ecobertura.core.data._
import org.eclipse.swt.graphics.Image
import org.eclipse.ui.PlatformUI
import org.eclipse.jdt.ui._

trait CoverageSessionTreeNode {
	private var nodeChildren: List[CoverageSessionTreeNode] = Nil
	
	var parent: Option[CoverageSessionTreeNode] = None
	def name: String
	def icon: Image

	def coverageData: CoverageData
	
	def children: List[CoverageSessionTreeNode] = nodeChildren
	def hasChildren = nodeChildren != Nil
	
	def addChild(child: CoverageSessionTreeNode) = {
		nodeChildren ::= child
		child.parent = Some(this)
	}
	
	def removeAllChildren = {
		nodeChildren = Nil
	}
}

abstract class CoverageSessionNode

object CoverageSessionRoot 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override val name = "root"
	override val icon = null  
		
	override val coverageData = EmptyCoverageData
}

class CoverageSessionAllPackages(session: CoverageSession) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	
	override val name = String.format("All Packages (%s)", session.displayName)
	override val icon = PlatformUI.getWorkbench.getSharedImages.getImage(
			org.eclipse.ui.ISharedImages.IMG_OBJ_PROJECT)
				
	override val coverageData = session
}

class CoverageSessionPackage(packageCoverage: PackageCoverage) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override val icon = JavaUI.getSharedImages.getImage(ISharedImages.IMG_OBJS_PACKAGE)
	override val name = packageCoverage.name	
		
	override val coverageData = packageCoverage
}

class CoverageSessionClass(classCoverage: ClassCoverage) 
		extends CoverageSessionNode with CoverageSessionTreeNode {
	override val icon = JavaUI.getSharedImages.getImage(ISharedImages.IMG_OBJS_CLASS)
	override val name = classCoverage.name	
	override val coverageData = classCoverage
}
