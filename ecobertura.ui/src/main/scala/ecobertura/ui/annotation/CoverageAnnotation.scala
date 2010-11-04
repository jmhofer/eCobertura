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
package ecobertura.ui.annotation

import org.eclipse.jface.text.Position
import org.eclipse.jface.text.source.Annotation

object CoverageAnnotation {
	val ID_COVERED = "ecobertura.ui.annotation.coverageAnnotation.covered" //$NON-NLS-1$
	val ID_NOT_COVERED = "ecobertura.ui.annotation.coverageAnnotation.notCovered" //$NON-NLS-1$
		
	def coveredAtPosition(offset: Int, length: Int): CoverageAnnotation =
		new CoverageAnnotation(ID_COVERED, offset, length)
	
	def notCoveredAtPosition(offset: Int, length: Int): CoverageAnnotation =
		new CoverageAnnotation(ID_NOT_COVERED, offset, length)
}

class CoverageAnnotation(typeId: String, offset: Int, length: Int) 
		extends Annotation(typeId, false, null) {
	
	if (offset < 0 || length <= 0) 
		throw new IllegalArgumentException("invalid position") //$NON-NLS-1$
	
	val position = new Position(offset, length)

	def getPosition = new Position(position.offset, position.length)
}
