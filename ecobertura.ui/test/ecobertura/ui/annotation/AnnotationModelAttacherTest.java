package ecobertura.ui.annotation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import ecobertura.core.log.Logger;
import ecobertura.ui.annotation.AnnotationModelAttacher;
import ecobertura.ui.annotation.CoverageAnnotationModel;


public class AnnotationModelAttacherTest {

	abstract class AnnotationModelImpl implements IAnnotationModel, IAnnotationModelExtension {};
	
	private ILog ilog;
	private ArgumentCaptor<IStatus> status;
	
	private ITextEditor editor;
	private IDocumentProvider provider;
	private IAnnotationModel invalidModel;
	private IAnnotationModel nonNullModel;
	private IAnnotationModelExtension modelExt;
	
	@Before
	public void setUp() throws Exception {
		ilog = mock(ILog.class);
		editor = mock(ITextEditor.class);
		provider = mock(IDocumentProvider.class);
		invalidModel = mock(IAnnotationModel.class);
		nonNullModel = mock(IAnnotationModel.class);
		modelExt = mock(AnnotationModelImpl.class);
		
		status = ArgumentCaptor.forClass(IStatus.class);
		Logger.logFor(ilog);
	}

	@Test
	public void testAttachToInvalidProvider() {
		when(editor.getDocumentProvider()).thenReturn(null);
		new AnnotationModelAttacher().attachTo(editor);
		verify(ilog, times(1)).log(status.capture());
		assertEquals("no document provider found", status.getValue().getMessage());
	}
	
	@Test
	public void testAttachToInvalidModel() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn(invalidModel);
		new AnnotationModelAttacher().attachTo(editor);
		verify(ilog, times(1)).log(status.capture());
		assertTrue(status.getValue().getMessage().startsWith("unknown annotation model extension type"));
	}
	
	@Test
	public void testAttachTo() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		new AnnotationModelAttacher().attachTo(editor);
		verify(ilog, times(2)).log(status.capture());
		assertTrue(status.getValue().getMessage().startsWith("CoverageAnnotationModel attached to"));
	}

	@Test
	public void testAttachToExisting() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		when(modelExt.getAnnotationModel(CoverageAnnotationModel.MODEL_ID)).thenReturn(nonNullModel);
		new AnnotationModelAttacher().attachTo(editor);
		verify(ilog, never()).log(any(IStatus.class));
	}
}
