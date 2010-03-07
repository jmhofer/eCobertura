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
