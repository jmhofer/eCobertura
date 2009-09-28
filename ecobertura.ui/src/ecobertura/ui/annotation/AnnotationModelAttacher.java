package ecobertura.ui.annotation;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

class AnnotationModelAttacher {
	private static final Logger logger = Logger.getLogger("ecobertura.ui.annotation"); //$NON-NLS-1$
	
	private ITextEditor editor;
	private IDocumentProvider provider;
	private IAnnotationModelExtension modelToAttachTo;
	
	void attachTo(final ITextEditor editor) {
		this.editor = editor;
		try {
			attachIfNecessary();
		} catch(NoValidAnnotationModelException e) {
			logger.log(Level.WARNING, "unable to attach to editors", e); //$NON-NLS-1$
		}
	}
	
	private void attachIfNecessary() {
		provider = getProvider();
		modelToAttachTo = getAnnotationModel();
	    if (!alreadyAttached()) {
		    attach();
	    }
	}

	private IDocumentProvider getProvider() {
	    final IDocumentProvider provider = editor.getDocumentProvider();
	    if (provider == null) {
	    	throw new NoValidAnnotationModelException("no document provider found"); //$NON-NLS-1$
	    }
	    return provider;
	}

	private IAnnotationModelExtension getAnnotationModel() {
	    final IAnnotationModel model = provider.getAnnotationModel(editor.getEditorInput());
	    if (!(model instanceof IAnnotationModelExtension)) {
	    	throw new NoValidAnnotationModelException("unknown annotation model extension type: "  //$NON-NLS-1$
	    			+ model.getClass().getName());
	    }
	    return (IAnnotationModelExtension) model;
	}

	private boolean alreadyAttached() {
		return modelToAttachTo.getAnnotationModel(CoverageAnnotationModel.MODEL_ID) != null;
	}

	private void attach() {
		final IDocument document = provider.getDocument(editor.getEditorInput());
	    final CoverageAnnotationModel modelToAttach 
	    	= CoverageAnnotationModel.createForEditorDocument(editor, document);
	    modelToAttachTo.addAnnotationModel(CoverageAnnotationModel.MODEL_ID, modelToAttach);
	    logger.fine("CoverageAnnotationModel attached to " + editor.getTitle()); //$NON-NLS-1$
	}
}
