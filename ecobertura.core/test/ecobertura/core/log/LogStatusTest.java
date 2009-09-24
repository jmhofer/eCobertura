package ecobertura.core.log;

import static org.junit.Assert.*;


import org.eclipse.core.runtime.Status;
import org.junit.Test;

import ecobertura.core.log.LogStatus;

public class LogStatusTest {

	private LogStatus status;
	
	@Test
	public void testFromMessageWithSeverityInfo() {
		status = LogStatus.fromMessageWithSeverity("hello", Status.INFO);
		assertLogStatusMessage("hello", Status.INFO);
	}

	@Test
	public void testFromMessageWithSeverityWarning() {
		status = LogStatus.fromMessageWithSeverity("", Status.WARNING);
		assertLogStatusMessage("", Status.WARNING);
	}
	
	@Test
	public void testFromMessageWithSeverityError() {
		status = LogStatus.fromMessageWithSeverity("bla hello", Status.ERROR);
		assertLogStatusMessage("bla hello", Status.ERROR);
	}
	
	private void assertLogStatusMessage(String message, int severity) {
		assertEquals(message, status.getMessage());
		assertEquals(severity, status.getSeverity());
		assertNull(status.getException());
	}
	
	@Test(expected=AssertionError.class)
	public void testFromMessageWithSeverityNullMessage() {
		LogStatus.fromMessageWithSeverity(null, Status.INFO);
	}
		
	@Test(expected=AssertionError.class)
	public void testFromMessageWithInvalidSeverity() {
		LogStatus.fromMessageWithSeverity("bla", Status.CANCEL);
	}
		
	@Test
	public void testFromExceptionWithSeverityInfo() {
		status = LogStatus.fromExceptionWithSeverity("hello", new Exception("hello"), Status.INFO);
		assertLogStatusException("hello", new Exception("hello"), Status.INFO);
	}

	@Test
	public void testFromExceptionWithSeverityWarning() {
		status = LogStatus.fromExceptionWithSeverity("", new Exception(""), Status.WARNING);
		assertLogStatusException("", new Exception(""), Status.WARNING);
	}
	
	@Test
	public void testFromExceptionWithSeverityError() {
		status = LogStatus.fromExceptionWithSeverity("bla bla", new Throwable("blurp"), Status.ERROR);
		assertLogStatusException("bla bla", new Throwable("blurp"), Status.ERROR);
	}
	
	private void assertLogStatusException(String message, Throwable throwable, int severity) {
		assertEquals(message, status.getMessage());
		assertEquals(throwable.getClass(), status.getException().getClass());
		assertEquals(throwable.getMessage(), status.getException().getMessage());
		assertEquals(severity, status.getSeverity());
	}
	
	
	@Test(expected=AssertionError.class)
	public void testFromExceptionWithSeverityNullMessage() {
		LogStatus.fromExceptionWithSeverity(null, new Throwable("bla"), Status.INFO);
	}
		
	@Test(expected=AssertionError.class)
	public void testFromExceptionWithSeverityNullThrowable() {
		LogStatus.fromExceptionWithSeverity("bla", null, Status.INFO);
	}
		
	@Test(expected=AssertionError.class)
	public void testFromExceptionWithInvalidSeverity() {
		LogStatus.fromExceptionWithSeverity("bla", new Throwable("bla"), Status.CANCEL);
	}
		
}
