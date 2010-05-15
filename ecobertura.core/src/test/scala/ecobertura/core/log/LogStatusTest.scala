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
import org.junit.Test;

import org.eclipse.core.runtime.Status

import _root_.ecobertura.core.log.LogStatus._

class LogStatusTest {
  var status: Status = null

  @Test
  def testFromMessageWithSeverityInfo = {
    status = fromMessageWithSeverity("hello", Severity.Info)
    assertLogStatusMessage("hello", Severity.Info)
  }

  @Test
  def testFromMessageWithSeverityWarning = {
    status = fromMessageWithSeverity("", Severity.Warning)
    assertLogStatusMessage("", Severity.Warning)
  }
  
  @Test
  def testFromMessageWithSeverityError = {
    status = fromMessageWithSeverity("bla hello", Severity.Error)
    assertLogStatusMessage("bla hello", Severity.Error)
  }
  
  private def assertLogStatusMessage(message: String, severity: Severity.Value) = {
    assertEquals(message, status.getMessage)
    assertEquals(severity.id, status.getSeverity)
    assertNull(status.getException)
  }
  
  @Test(expected=classOf[AssertionError])
  def testFromMessageWithSeverityNullMessage : Unit = fromMessageWithSeverity(null, Severity.Info)
  
  @Test
  def testFromExceptionWithSeverityInfo = {
    status = fromExceptionWithSeverity("hello", new Exception("hello"), Severity.Info)
    assertLogStatusException("hello", new Exception("hello"), Severity.Info)
  }
  
  @Test
  def testFromExceptionWithSeverityWarning = {
    status = fromExceptionWithSeverity("", new Exception(""), Severity.Warning)
    assertLogStatusException("", new Exception(""), Severity.Warning)
  }
  
  @Test
  def testFromExceptionWithSeverityError = {
    status = fromExceptionWithSeverity("bla bla", new Throwable("blurp"), Severity.Error)
    assertLogStatusException("bla bla", new Throwable("blurp"), Severity.Error)
  }
  
  @Test(expected=classOf[AssertionError])
  def testFromExceptionWithSeverityNullMessage : Unit = 
      fromExceptionWithSeverity(null, new Throwable("bla"), Severity.Info)
  
  @Test(expected=classOf[AssertionError])
  def testFromExceptionWithSeverityNullThrowable : Unit =
      fromExceptionWithSeverity("bla", null, Severity.Info);

  private def assertLogStatusException(
      message: String, throwable: Throwable, severity: Severity.Value) = {
    assertEquals(message, status.getMessage)
    assertEquals(throwable.getClass, status.getException.getClass)
    assertEquals(throwable.getMessage, status.getException.getMessage)
    assertEquals(severity.id, status.getSeverity)
  }
}
