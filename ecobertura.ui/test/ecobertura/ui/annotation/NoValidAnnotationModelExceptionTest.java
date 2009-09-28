package ecobertura.ui.annotation;

import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;

import ecobertura.ui.annotation.NoValidAnnotationModelException;

public class NoValidAnnotationModelExceptionTest {

	Throwable t;
	
	@Before
	public void setUp() {
		t = new NoValidAnnotationModelException("message");
	}
	
	@Test
	public void testGetMessage() {
		assertEquals("message", t.getMessage());
	}

}
