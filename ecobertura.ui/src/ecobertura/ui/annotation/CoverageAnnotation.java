package ecobertura.ui.annotation;

import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;

/**
 * Coverage annotation.
 */
public class CoverageAnnotation extends Annotation {

	private final Position position;
	  
	static final String ID = 
		"ecobertura.ui.annotation.coverageAnnotation"; //$NON-NLS-1$
	
	public static CoverageAnnotation fromPosition(int offset, int length) {
		if (offset < 0 || length <= 0) {
			throw new IllegalArgumentException("invalid position"); //$NON-NLS-1$
		}
		return new CoverageAnnotation(offset, length);
	}
	
	private CoverageAnnotation(int offset, int length) {
		super(ID, false, null);
		position = new Position(offset, length);
	}
	  
	Position getPosition() {
		return new Position(position.offset, position.length);
	}
	
}
