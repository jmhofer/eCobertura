/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
 * All rights reserved.
 *
 * eCobertura is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *  
 * eCobertura is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with eCobertura.  If not, see <http://www.gnu.org/licenses/>.
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
