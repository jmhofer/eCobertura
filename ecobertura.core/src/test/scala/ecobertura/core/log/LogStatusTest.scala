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
