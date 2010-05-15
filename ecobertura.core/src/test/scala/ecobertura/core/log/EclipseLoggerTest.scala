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
package ecobertura.core.log

import org.junit.Assert._
import org.mockito.Mockito._

import org.eclipse.core.runtime._
import org.junit._
import org.mockito.ArgumentCaptor

class EclipseLoggerTest {
  private var ilog: ILog = null
  private var status: ArgumentCaptor[IStatus] = null
  
  @Before
  def setUp = {
    ilog = mock(classOf[ILog])
    status = ArgumentCaptor forClass classOf[IStatus]
    EclipseLogger logFor ilog
  }
  
  @Test
  def testInfoString = {
    EclipseLogger info "hello"
    assertLogMessageSeverity("hello", IStatus.INFO);
  }

  @Test
  def testWarnString = {
    EclipseLogger warn "hello"
    assertLogMessageSeverity("hello", IStatus.WARNING);
  }
  
  @Test
  def testErrorString = {
    EclipseLogger error "hello"
    assertLogMessageSeverity("hello", IStatus.ERROR);
  }
  
  private def assertLogMessageSeverity(message: String, severity: Int) = {
    verify(ilog, times(1)) log status.capture
    assertEquals(message, status.getValue.getMessage)
    assertEquals(severity, status.getValue.getSeverity)
    assertNull(status.getValue.getException)
  }
  
  @Test
  def testInfoStringThrowable = {
    EclipseLogger info ("hello", new Exception("hello exc"))
    assertLogMessageSeverityException("hello", IStatus.INFO, new Exception("hello exc"))
  }

  @Test
  def testWarnStringThrowable = {
    EclipseLogger warn ("hello", new Exception("hello exc"))
    assertLogMessageSeverityException("hello", IStatus.WARNING, new Exception("hello exc"))
  }

  @Test
  def testErrorStringThrowable = {
    EclipseLogger error ("hello", new Exception("hello exc"))
    assertLogMessageSeverityException("hello", IStatus.ERROR, new Exception("hello exc"))
  }

  @Test
  def testInfoThrowable = {
    EclipseLogger info (new Exception("hello"))
    assertLogMessageSeverityException("hello", IStatus.INFO, new Exception("hello"))
  }

  @Test
  def testWarnThrowable = {
    EclipseLogger warn (new Exception("hello"))
    assertLogMessageSeverityException("hello", IStatus.WARNING, new Exception("hello"))
  }

  @Test
  def testErrorThrowable = {
    EclipseLogger error (new Exception("hello"))
    assertLogMessageSeverityException("hello", IStatus.ERROR, new Exception("hello"))
  }

  private def assertLogMessageSeverityException(
      message: String, severity: Int, throwable: Throwable) = {
    verify(ilog, times(1)) log status.capture
    assertEquals(message, status.getValue.getMessage)
    assertEquals(severity, status.getValue.getSeverity)
    assertEquals(throwable.getClass, status.getValue.getException.getClass)
    assertEquals(throwable.getMessage, status.getValue.getException.getMessage)
  }
}
