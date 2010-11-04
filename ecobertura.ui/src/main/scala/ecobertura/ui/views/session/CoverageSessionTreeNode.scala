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
