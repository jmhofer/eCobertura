package ecobertura.ui.annotation

import org.junit._
import org.junit.Assert._

class NoValidAnnotationModelExceptionTest {
	var t: Throwable = null
	
	@Before
	def setUp = {
		t = new NoValidAnnotationModelException("message")
	}
	
	@Test
	def testGetMessage = {
		assertEquals("message", t.getMessage)
	}
}