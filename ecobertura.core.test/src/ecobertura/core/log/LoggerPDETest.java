package ecobertura.core.log;

import static org.junit.Assert.*;


import org.eclipse.core.runtime.ILogListener;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import ecobertura.core.*;
import ecobertura.core.log.EclipseLogger$;

public class LoggerPDETest implements ILogListener {

	private Bundle core;
	private int logSeverity;
	private String logMessage;
	private Throwable logThrowable;
	
	@Before
	public void setUp() throws Exception {
		core = Platform.getBundle(CorePlugin$.MODULE$.pluginId());
		Platform.addLogListener(this);
		core.start();
	}

	@After
	public void tearDown() throws Exception {
		Platform.removeLogListener(this);
		core.stop();
	}
	
	@Test
	public void testInfoString() {
		shouldLog(Status.INFO, "hello world", null);
		EclipseLogger$.MODULE$.info("hello world");
	}

	@Test
	public void testInfoStringThrowable() {
		shouldLog(Status.INFO, "hello world", new Exception("hello"));
		EclipseLogger$.MODULE$.info("hello world", new Exception("hello"));
	}

	@Test
	public void testInfoThrowable() {
		shouldLog(Status.INFO, "hello", new Exception("hello"));
		EclipseLogger$.MODULE$.info(new Exception("hello"));
	}

	@Test
	public void testWarnString() {
		shouldLog(Status.WARNING, "hello", null);
		EclipseLogger$.MODULE$.warn("hello");
	}

	@Test
	public void testWarnThrowable() {
		shouldLog(Status.WARNING, "helloExc", new Exception("helloExc"));
		EclipseLogger$.MODULE$.warn(new Exception("helloExc"));
	}

	@Test
	public void testWarnStringThrowable() {
		shouldLog(Status.WARNING, "helloMsg", new Exception("helloExc"));
		EclipseLogger$.MODULE$.warn("helloMsg", new Exception("helloExc"));
	}

	@Test
	public void testErrorString() {
		shouldLog(Status.ERROR, "", null);
		EclipseLogger$.MODULE$.error("");
	}

	@Test
	public void testErrorThrowable() {
		shouldLog(Status.ERROR, "rte", new RuntimeException("rte"));
		EclipseLogger$.MODULE$.error(new RuntimeException("rte"));
	}

	@Test
	public void testErrorStringThrowable() {
		shouldLog(Status.ERROR, "rte msg", new RuntimeException("rte"));
		EclipseLogger$.MODULE$.error("rte msg", new RuntimeException("rte"));
	}

	public void shouldLog(int severity, String message, Throwable throwable) {
		logSeverity = severity;
		logMessage = message;
		logThrowable = throwable;
	}

	@Override
	public void logging(IStatus status, String plugin) {
		assertSame(logSeverity, status.getSeverity());
		assertEquals(logMessage, status.getMessage());
		if (logThrowable == null) {
			assertNull(status.getException());
		} else {
			assertEquals(logThrowable.getMessage(), status.getException().getMessage());
			assertEquals(logThrowable.getClass(), status.getException().getClass());
			assertEquals(logThrowable.getCause(), status.getException().getCause());
		}
	}

}
