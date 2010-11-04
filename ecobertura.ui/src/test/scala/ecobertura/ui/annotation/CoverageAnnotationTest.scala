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

import org.junit._
import Assert._

import org.eclipse.jface.text.Position

class CoverageAnnotationTest {
	var ca: CoverageAnnotation = null
	
	@Before
	def setUp = {
		ca = CoverageAnnotation.coveredAtPosition(10, 20)
	}
	
	@Test
	def testGetPosition = assertEquals(new Position(10, 20), ca.getPosition)
	
	@Test
	def testIsPersistent = assertFalse(ca.isPersistent)
	
	@Test
	def testGetType = assertEquals(CoverageAnnotation.ID_COVERED, ca.getType)
			
	@Test
	def testIsMarkedDeleted = assertFalse(ca.isMarkedDeleted)
	
	@Test
	def testGetText = assertNull(ca.getText)
	
	@Test(expected=classOf[IllegalArgumentException])
	def testFromPositionIllegalOffset: Unit = CoverageAnnotation.coveredAtPosition(-1, 10)

	@Test(expected=classOf[IllegalArgumentException])
	def testFromPositionIllegalLength: Unit = CoverageAnnotation.coveredAtPosition(10, -1)
}
