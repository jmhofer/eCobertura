package ecobertura.ui.annotation;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.AnnotationModelEvent;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.text.source.IAnnotationModelListener;
import org.eclipse.jface.text.source.IAnnotationModelListenerExtension;

public abstract class AbstractAnnotationModel implements IAnnotationModel {

	protected final List<IAnnotationModelListener> listeners = 
		new CopyOnWriteArrayList<IAnnotationModelListener>();

	@Override
	public void addAnnotationModelListener(IAnnotationModelListener listener) {
		listeners.add(listener);
		fireModelChanged(new AnnotationModelEvent(this, true));
	}

	@Override
	public void removeAnnotationModelListener(IAnnotationModelListener listener) {
		listeners.remove(listener);
	}
	
	protected void fireModelChanged(final AnnotationModelEvent event) {
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

	@Override
	public void addAnnotation(Annotation annotation, Position position) {
		throw new UnsupportedOperationException(
				"adding annotations externally is not supported"); //$NON-NLS-1$
	}

	@Override
	public void removeAnnotation(Annotation annotation) {
		throw new UnsupportedOperationException(
				"removing annotations externally is not supported"); //$NON-NLS-1$
	}

	@Override
	abstract public void connect(IDocument document);

	@Override
	abstract public void disconnect(IDocument document);

	@Override
	abstract public Iterator<CoverageAnnotation> getAnnotationIterator();

	@Override
	abstract public Position getPosition(Annotation annotation);

}
