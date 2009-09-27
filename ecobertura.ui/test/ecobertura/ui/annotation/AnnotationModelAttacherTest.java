package ecobertura.ui.annotation;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

// TODO define handler for logger and check warnings
public class AnnotationModelAttacherTest {

	abstract class AnnotationModelImpl implements IAnnotationModel, IAnnotationModelExtension {};
	
	private Handler handler;
	private ArgumentCaptor<LogRecord> record;
	
	private ITextEditor editor;
	private IDocumentProvider provider;
	private IAnnotationModel invalidModel;
	private IAnnotationModel nonNullModel;
	private IAnnotationModelExtension modelExt;
	
	@Before
	public void setUp() throws Exception {
		registerTraceHandler();
		mockEnvironment();
	}

	private void registerTraceHandler() {
		handler = mock(Handler.class);
		Logger.getLogger("ecobertura.ui").addHandler(handler);
		Logger.getLogger("ecobertura.ui").setLevel(Level.ALL);
		record = ArgumentCaptor.forClass(LogRecord.class);
	}

	private void mockEnvironment() {
		editor = mock(ITextEditor.class);
		provider = mock(IDocumentProvider.class);
		invalidModel = mock(IAnnotationModel.class);
		nonNullModel = mock(IAnnotationModel.class);
		modelExt = mock(AnnotationModelImpl.class);
	}

	@Test
	public void testAttachToInvalidProvider() {
		when(editor.getDocumentProvider()).thenReturn(null);
		new AnnotationModelAttacher().attachTo(editor);
		
		verify(handler).publish(record.capture());
		
		LogRecord logged = record.getValue();
		assertEquals("unable to attach to editors", logged.getMessage());
		assertEquals(Level.WARNING, logged.getLevel());
		Throwable thrown = logged.getThrown();
		assertEquals("no document provider found", thrown.getMessage());
		assertEquals(NoValidAnnotationModelException.class, thrown.getClass());
	}
	
	@Test
	public void testAttachToInvalidModel() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn(invalidModel);
		new AnnotationModelAttacher().attachTo(editor);
		
		verify(handler).publish(record.capture());
		
		LogRecord logged = record.getValue();
		assertEquals("unable to attach to editors", logged.getMessage());
		assertEquals(Level.WARNING, logged.getLevel());
		Throwable thrown = logged.getThrown();
		assertTrue(thrown.getMessage().startsWith("unknown annotation model extension type"));
		assertEquals(NoValidAnnotationModelException.class, thrown.getClass());
	}
	
	@Test
	public void testAttachTo() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		new AnnotationModelAttacher().attachTo(editor);
		
		verify(handler, times(2)).publish(record.capture());
		
		LogRecord logged = record.getValue();
		assertTrue(logged.getMessage().startsWith("CoverageAnnotationModel attached to"));
		assertEquals(Level.FINE, logged.getLevel());
	}

	@Test
	public void testAttachToExisting() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		when(modelExt.getAnnotationModel(CoverageAnnotationModel.MODEL_ID)).thenReturn(nonNullModel);
		new AnnotationModelAttacher().attachTo(editor);
		verify(handler, never()).publish(any(LogRecord.class));
	}
}
