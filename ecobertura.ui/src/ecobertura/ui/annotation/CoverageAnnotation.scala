package ecobertura.ui.annotation

import org.eclipse.jface.text.Position
import org.eclipse.jface.text.source.Annotation

object CoverageAnnotation {
	val ID = "ecobertura.ui.annotation.coverageAnnotation" //$NON-NLS-1$
		
	def fromPosition(offset: Int, length: Int): CoverageAnnotation = {
		if (offset < 0 || length <= 0) 
			throw new IllegalArgumentException("invalid position") //$NON-NLS-1$
		new CoverageAnnotation(offset, length)
	}
}

class CoverageAnnotation(offset: Int, length: Int) 
		extends Annotation(CoverageAnnotation.ID, false, null) {
	
	val position = new Position(offset, length)

	def getPosition = new Position(position.offset, position.length)
}
