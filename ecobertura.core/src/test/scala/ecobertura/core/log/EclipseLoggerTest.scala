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
