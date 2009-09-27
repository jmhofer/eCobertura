package ecobertura.core.log;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ecobertura.core.log.EclipseLogger;

public class EclipseLoggerTest {

	private ILog ilog;
	private ArgumentCaptor<IStatus> status;
	
	@Before
	public void setUp() {
		ilog = mock(ILog.class);
		status = ArgumentCaptor.forClass(IStatus.class);
		
		EclipseLogger.logFor(ilog);
	}
	
	@Test
	public void testInfoString() {
		EclipseLogger.info("hello");
		assertLogMessageSeverity("hello", Status.INFO);
	}

	@Test
	public void testWarnString() {
		EclipseLogger.warn("hello");
		assertLogMessageSeverity("hello", Status.WARNING);
	}

	@Test
	public void testErrorString() {
		EclipseLogger.error("hello");
		assertLogMessageSeverity("hello", Status.ERROR);
	}

	@Test
	public void testInfoStringThrowable() {
		EclipseLogger.info("hello", new Exception("hello exc"));
		assertLogMessageSeverityException("hello", Status.INFO, new Exception("hello exc"));
	}

	@Test
	public void testWarnStringThrowable() {
		EclipseLogger.warn("hello", new Exception("hello exc"));
		assertLogMessageSeverityException("hello", Status.WARNING, new Exception("hello exc"));
	}

	@Test
	public void testErrorStringThrowable() {
		EclipseLogger.error("hello", new Exception("hello exc"));
		assertLogMessageSeverityException("hello", Status.ERROR, new Exception("hello exc"));
	}
	
	@Test
	public void testInfoThrowable() {
		EclipseLogger.info(new Exception("hello"));
		assertLogMessageSeverityException("hello", Status.INFO, new Exception("hello"));
	}

	@Test
	public void testWarnThrowable() {
		EclipseLogger.warn(new Exception("hello"));
		assertLogMessageSeverityException("hello", Status.WARNING, new Exception("hello"));
	}

	@Test
	public void testErrorThrowable() {
		EclipseLogger.error(new Exception("hello"));
		assertLogMessageSeverityException("hello", Status.ERROR, new Exception("hello"));
	}

	private void assertLogMessageSeverity(String message, int severity) {
		verify(ilog, times(1)).log(status.capture());
		assertEquals(message, status.getValue().getMessage());
		assertEquals(severity, status.getValue().getSeverity());
		assertNull(status.getValue().getException());
	}
	
	private void assertLogMessageSeverityException(String message, int severity, Throwable throwable) {
		verify(ilog, times(1)).log(status.capture());
		assertEquals(message, status.getValue().getMessage());
		assertEquals(severity, status.getValue().getSeverity());
		assertEquals(throwable.getClass(), status.getValue().getException().getClass());
		assertEquals(throwable.getMessage(), status.getValue().getException().getMessage());
	}
}
