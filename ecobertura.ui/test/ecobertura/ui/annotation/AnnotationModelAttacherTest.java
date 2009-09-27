package ecobertura.ui.annotation;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.junit.Before;
import org.junit.Test;

// TODO define handler for logger and check warnings
public class AnnotationModelAttacherTest {

	abstract class AnnotationModelImpl implements IAnnotationModel, IAnnotationModelExtension {};
	
	private ITextEditor editor;
	private IDocumentProvider provider;
	private IAnnotationModel invalidModel;
	private IAnnotationModel nonNullModel;
	private IAnnotationModelExtension modelExt;
	
	@Before
	public void setUp() throws Exception {
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
//		verify(ilog, times(1)).log(status.capture());
//		assertEquals("no document provider found", status.getValue().getMessage());
	}
	
	@Test
	public void testAttachToInvalidModel() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn(invalidModel);
		new AnnotationModelAttacher().attachTo(editor);
//		verify(ilog, times(1)).log(status.capture());
//		assertTrue(status.getValue().getMessage().startsWith("unknown annotation model extension type"));
	}
	
	@Test
	public void testAttachTo() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		new AnnotationModelAttacher().attachTo(editor);
//		verify(ilog, times(2)).log(status.capture());
//		assertTrue(status.getValue().getMessage().startsWith("CoverageAnnotationModel attached to"));
	}

	@Test
	public void testAttachToExisting() {
		when(editor.getDocumentProvider()).thenReturn(provider);
		when(provider.getAnnotationModel(any())).thenReturn((IAnnotationModel) modelExt);
		when(modelExt.getAnnotationModel(CoverageAnnotationModel.MODEL_ID)).thenReturn(nonNullModel);
		new AnnotationModelAttacher().attachTo(editor);
//		verify(ilog, never()).log(any(IStatus.class));
	}
}
