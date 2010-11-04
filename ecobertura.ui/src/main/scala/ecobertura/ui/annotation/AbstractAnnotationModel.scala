/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.ui.annotation

import java.util.Iterator

import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.Position
import org.eclipse.jface.text.source._

abstract class AbstractAnnotationModel extends IAnnotationModel {
	private var listeners = List[IAnnotationModelListener]()
	
	override def addAnnotationModelListener(listener: IAnnotationModelListener) = {
		listeners ::= listener
		fireModelChanged(new AnnotationModelEvent(this, true))
	}
	
	override def removeAnnotationModelListener(listener: IAnnotationModelListener) =
		listeners.filterNot(_ == listener)
		
	protected def fireModelChanged(event: AnnotationModelEvent) = {
		event.markSealed
		if (!event.isEmpty) {
			for (listener <- listeners) {
				listener match {
					case extended: IAnnotationModelListenerExtension => extended modelChanged event
					case listener => listener modelChanged this
				}
			}
		}
	}
	
	override def addAnnotation(annotation: Annotation, position: Position) =
		throw new UnsupportedOperationException("adding annotations externally is not supported") //$NON-NLS-1$

	override def removeAnnotation(annotation: Annotation) =
		throw new UnsupportedOperationException("removing annotations externally is not supported") //$NON-NLS-1$
	
	override def connect(document: IDocument)
	override def disconnect(document: IDocument)
	override def getAnnotationIterator : Iterator[CoverageAnnotation]
	override def getPosition(annotation: Annotation) : Position
}
