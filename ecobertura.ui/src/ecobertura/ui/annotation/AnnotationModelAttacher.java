package ecobertura.ui.annotation;


import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelExtension;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import ecobertura.core.log.Logger;


class AnnotationModelAttacher {
	private ITextEditor editor;
	private IDocumentProvider provider;
	private IAnnotationModelExtension modelToAttachTo;
	
	void attachTo(final ITextEditor editor) {
		this.editor = editor;
		try {
			attachIfNecessary();
		} catch(NoValidAnnotationModelException e) {
			Logger.info(e);
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
	    	throw new NoValidAnnotationModelException("no document provider found");
	    }
	    return provider;
	}

	private IAnnotationModelExtension getAnnotationModel() {
	    final IAnnotationModel model = provider.getAnnotationModel(editor.getEditorInput());
	    if (!(model instanceof IAnnotationModelExtension)) {
	    	throw new NoValidAnnotationModelException("unknown annotation model extension type: " 
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
		Logger.info("CoverageAnnotationModel attached to " + editor.getTitle());
	}
}
