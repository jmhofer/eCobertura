package ecobertura.ui.annotation

import org.junit._
import Assert._

import org.eclipse.jface.text.Position

class CoverageAnnotationTest {
	var ca: CoverageAnnotation = null
	
	@Before
	def setUp = {
		ca = CoverageAnnotation fromPosition (10, 20)
	}
	
	@Test
	def testGetPosition = assertEquals(new Position(10, 20), ca.getPosition)
	
	@Test
	def testIsPersistent = assertFalse(ca.isPersistent)
	
	@Test
	def testGetType = assertEquals(CoverageAnnotation.ID, ca.getType)
			
	@Test
	def testIsMarkedDeleted = assertFalse(ca.isMarkedDeleted)
	
	@Test
	def testGetText = assertNull(ca.getText)
	
	@Test(expected=classOf[IllegalArgumentException])
	def testFromPositionIllegalOffset: Unit = CoverageAnnotation fromPosition (-1, 10)

	@Test(expected=classOf[IllegalArgumentException])
	def testFromPositionIllegalLength: Unit = CoverageAnnotation fromPosition (10, -1)
}
