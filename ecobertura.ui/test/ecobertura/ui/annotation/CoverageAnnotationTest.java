package ecobertura.ui.annotation;

import static org.junit.Assert.*;


import org.eclipse.jface.text.Position;
import org.junit.Before;
import org.junit.Test;

import ecobertura.ui.annotation.CoverageAnnotation;

public class CoverageAnnotationTest {

	CoverageAnnotation ca;
	
	@Before
	public void setUp() {
		ca = CoverageAnnotation.fromPosition(10, 20);
	}

	@Test
	public void testGetPosition() {
		assertEquals(new Position(10, 20), ca.getPosition());
	}

	@Test
	public void testIsPersistent() {
		assertFalse(ca.isPersistent());
	}

	@Test
	public void testGetType() {
		assertEquals(CoverageAnnotation.ID, ca.getType());
	}

	@Test
	public void testIsMarkedDeleted() {
		assertFalse(ca.isMarkedDeleted());
	}

	@Test
	public void testGetText() {
		assertNull(ca.getText());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromPositionIllegalOffset() {
		CoverageAnnotation.fromPosition(-1, 10);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testFromPositionIllegalLength() {
		CoverageAnnotation.fromPosition(10, -1);
	}
}
