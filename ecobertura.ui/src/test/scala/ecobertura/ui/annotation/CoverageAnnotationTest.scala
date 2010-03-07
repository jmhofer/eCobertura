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
