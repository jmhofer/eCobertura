package ecobertura.ui.annotation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;
import org.eclipse.ui.texteditor.ITextEditor;

import ecobertura.core.log.Logger;


// TODO fire events to listeners
// TODO react to document/editor changes 
// TODO react to coverage changes

public class CoverageAnnotationModel implements IAnnotationModel {
	
	static class Key {}
	static final Key MODEL_ID = new Key();
	
	private final List<IAnnotationModelListener> listeners = 
		new CopyOnWriteArrayList<IAnnotationModelListener>();
	
	private final List<CoverageAnnotation> annotations =
		new ArrayList<CoverageAnnotation>(64);
	
	public static void attachTo(final ITextEditor editor) {
		new AnnotationModelAttacher().attachTo(editor);
	}

	static CoverageAnnotationModel createForEditorDocument(
			final ITextEditor editor, final IDocument document) {
		return new CoverageAnnotationModel(editor, document);
	}
	
	private CoverageAnnotationModel(final ITextEditor editor, final IDocument document) {
		Logger.info("CoverageAnnotationModel created.");
		// TODO what about editor and document?
		initializeAnnotations();
	}

	private void initializeAnnotations() {
		// TODO correct stuff instead of dummy stuff
		CoverageAnnotation annotation = CoverageAnnotation.fromPosition(0, 10);
		annotations.add(annotation);
		AnnotationModelEvent event = new AnnotationModelEvent(this);
		event.annotationAdded(annotation);
		fireModelChanged(event);
	}
	
	@Override
	public void connect(final IDocument document) {
		Logger.info("CoverageAnnotationModel connected to " + document.get());

	    addAnnotationsTo(document);
		// TODO connecting
	}

	private void addAnnotationsTo(final IDocument document) {
		for (CoverageAnnotation ann : annotations) {
	        try {
	        	document.addPosition(ann.getPosition());
	        } catch (BadLocationException e) {
	        	Logger.warn(e);
	        }
	      }
	}

	@Override
	public void disconnect(final IDocument document) {
		// TODO disconnecting
		removeAnnotationsFrom(document);
		Logger.info("CoverageAnnotationModel disconnected from " + document.get());
	}

	private void removeAnnotationsFrom(IDocument document) {
		for (CoverageAnnotation ann : annotations) {
		      document.removePosition(ann.getPosition());
		}
	}

	@Override
	public Iterator<CoverageAnnotation> getAnnotationIterator() {
		return annotations.iterator();
	}

	@Override
	public Position getPosition(Annotation annotation) {
		if (annotation instanceof CoverageAnnotation) {
			return ((CoverageAnnotation) annotation).getPosition();
		} else {
			return null;
		}
	}

	@Override
	public void addAnnotation(Annotation annotation, Position position) {
		throw new UnsupportedOperationException(
				"adding annotations externally is not supported");
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		throw new UnsupportedOperationException(
				"removing annotations externally is not supported");
	}

	@Override
	public void addAnnotationModelListener(IAnnotationModelListener listener) {
		listeners.add(listener);
		fireModelChanged(new AnnotationModelEvent(this, true));
	}

	@Override
	public void removeAnnotationModelListener(IAnnotationModelListener listener) {
		listeners.remove(listener);
	}
	
	private void fireModelChanged(final AnnotationModelEvent event) {
		event.markSealed();
		if (event.isEmpty()) {
			return;
		}
		for (final IAnnotationModelListener listener : listeners) {
	        if (listener instanceof IAnnotationModelListenerExtension) {
	        	((IAnnotationModelListenerExtension) listener).modelChanged(event);
	        } else {
	        	listener.modelChanged(this);
	        }
		}
	}
}
