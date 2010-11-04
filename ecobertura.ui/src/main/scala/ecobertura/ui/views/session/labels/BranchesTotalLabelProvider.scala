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
package ecobertura.ui.views.session.labels

import org.eclipse.jface.viewers.ColumnLabelProvider

import ecobertura.ui.views.session.CoverageSessionTreeNode

class BranchesTotalLabelProvider extends ColumnLabelProvider {
	override def getText(node: Any) = node match {
		case coverageNode: CoverageSessionTreeNode => coverageNode.coverageData.branchesTotal.toString
		case _ => "???"
	}
}
