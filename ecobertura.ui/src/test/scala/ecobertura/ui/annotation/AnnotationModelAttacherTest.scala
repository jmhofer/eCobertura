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

import org.junit.Assert._;
import org.mockito.Matchers._;
import org.mockito.Mockito._;

import java.util.logging._

import org.eclipse.jface.text.source._
import org.eclipse.ui.texteditor._
import org.junit._
import org.mockito.ArgumentCaptor;

class AnnotationModelAttacherTest {
	abstract class AnnotationModelImpl extends IAnnotationModel with IAnnotationModelExtension {}
	
	private var handler: Handler = null
	private var record: ArgumentCaptor[LogRecord] = null
	
	private var editor: ITextEditor = null
	private var provider: IDocumentProvider = null
	private var invalidModel: IAnnotationModel = null
	private var nonNullModel: IAnnotationModel = null
	private var modelExt: IAnnotationModelExtension = null
	
	@Before
	def setUp = {
		registerTraceHandler
		mockEnvironment

		def registerTraceHandler = {
			handler = mock(classOf[Handler])
			Logger.getLogger("ecobertura.ui").addHandler(handler)
			Logger.getLogger("ecobertura.ui").setLevel(Level.ALL)
			record = ArgumentCaptor.forClass(classOf[LogRecord])
		}
		
		def mockEnvironment = {
			editor = mock(classOf[ITextEditor])
			provider = mock(classOf[IDocumentProvider])
			invalidModel = mock(classOf[IAnnotationModel])
			nonNullModel = mock(classOf[IAnnotationModel])
			modelExt = mock(classOf[AnnotationModelImpl])
		}
	}

	@Test
	def testAttachToInvalidProvider = {
		when(editor.getDocumentProvider).thenReturn(null)
		AnnotationModelAttacher.attachTo(editor)
		
		verify(handler).publish(record.capture)
		
		val logged = record.getValue
		assertEquals("unable to attach to editors", logged.getMessage)
		assertEquals(Level.WARNING, logged.getLevel)
		val thrown = logged.getThrown
		assertEquals("no document provider found", thrown.getMessage)
		assertEquals(classOf[NoValidAnnotationModelException], thrown.getClass)
	}
	
	@Test
	def testAttachToInvalidModel = {
		when(editor.getDocumentProvider()).thenReturn(provider)
		when(provider.getAnnotationModel(any())).thenReturn(invalidModel)
		AnnotationModelAttacher.attachTo(editor)

		verify(handler).publish(record.capture)
		
		val logged = record.getValue
		assertEquals("unable to attach to editors", logged.getMessage)
		assertEquals(Level.WARNING, logged.getLevel)
		val thrown = logged.getThrown
		assertTrue(thrown.getMessage.startsWith("unknown annotation model extension type"))
		assertEquals(classOf[NoValidAnnotationModelException], thrown.getClass())
	}
	
	@Test
	def testAttachTo = {
		when(editor.getDocumentProvider()).thenReturn(provider)
		when(provider.getAnnotationModel(any())).thenReturn(modelExt.asInstanceOf[IAnnotationModel])
		AnnotationModelAttacher.attachTo(editor)

		verify(handler, times(2)).publish(record.capture())
		
		val logged = record.getValue
		assertTrue(logged.getMessage.startsWith("CoverageAnnotationModel attached to"))
		assertEquals(Level.FINE, logged.getLevel)
	}
	
	@Test
	def testAttachToExisting = {
		when(editor.getDocumentProvider()).thenReturn(provider)
		when(provider.getAnnotationModel(any())).thenReturn(modelExt.asInstanceOf[IAnnotationModel])
		when(modelExt.getAnnotationModel(CoverageAnnotationModel.MODEL_ID)).thenReturn(nonNullModel)
		AnnotationModelAttacher.attachTo(editor)
		verify(handler, never()).publish(any(classOf[LogRecord]))
	}
}
