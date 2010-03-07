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

import scala.collection.JavaConversions

import java.util.logging._

import org.eclipse.jface.text._
import org.eclipse.jface.text.source._
import org.eclipse.ui.texteditor.ITextEditor

import ecobertura.core.data.LineCoverage
import ecobertura.ui.util.Predef._
import ecobertura.ui.editors.LineCoverageFinder
import ecobertura.ui.views.session.CoverageSessionModel

object CoverageAnnotationModel {
	private val logger = Logger.getLogger("ecobertura.ui.annotation") //$NON-NLS-1$

	class Key { /* internal marker class */ }
	val MODEL_ID = new Key
	
	def attachTo(editor: ITextEditor) =
		AnnotationModelAttacher.attachTo(editor)
	
	def createForEditorDocument(editor: ITextEditor, document: IDocument) = {
		new CoverageAnnotationModel(editor, document)
	}
}

class CoverageAnnotationModel(editor: ITextEditor, document: IDocument) 
		extends AbstractAnnotationModel {
	import CoverageAnnotationModel.logger
	
	private var annotations = List[CoverageAnnotation]()
	private var documentChanged = false
	
	logger.fine("CoverageAnnotationModel created.") //$NON-NLS-1$
	initializeAnnotations(editor, document)
	
	document.addDocumentListener {
		documentChanged = true
		removeAllAnnotations
	}
	
	CoverageSessionModel.get.addSessionResetListener(() => {
		if (!documentChanged) {
			removeAllAnnotations
			initializeAnnotations(editor, document)
		}
	})
	
	private def initializeAnnotations(editor: ITextEditor, document: IDocument) = {
		
		CoverageSessionModel.get.coverageSession match {
			case Some(session) => {
				logger.fine("CoverageSession active") /* session active */
				val coveredLines = LineCoverageFinder.forSession(session).findInEditor(editor)
				annotateLines(coveredLines)
			}
			case _ => /* nothing to do */
		}

		def annotateLines(lines: List[LineCoverage]) = {
			for (line <- lines; lineNumber = line.lineNumber - 1) {
				if (document.getLineLength(lineNumber) > 0) {
					val annotation = CoverageAnnotation.fromPosition(document.getLineOffset(lineNumber), 
							document.getLineLength(lineNumber))
					annotations ::= annotation
					val event = new AnnotationModelEvent(this)
					event.annotationAdded(annotation)
					fireModelChanged(event)
				}
			}
		}
	}
	
	private def removeAllAnnotations = {
		val event = new AnnotationModelEvent(this)
		annotations.foreach(event.annotationRemoved(_))
		annotations = Nil
		fireModelChanged(event)
	}
	
	override def connect(document: IDocument) = {
		logger.fine("CoverageAnnotationModel connected") //$NON-NLS-1$
		addAnnotationsTo(document)
		
		def addAnnotationsTo(document: IDocument) = {
			try {
				annotations.foreach(annotation => document.addPosition(annotation.getPosition))
			} catch {
				case e: BadLocationException =>
					logger.log(Level.WARNING, "unable to add annotation to document", e) //$NON-NLS-1$
			}
		}
	}
	
	override def disconnect(document: IDocument) = {
		removeAnnotationsFrom(document)
		logger.fine("CoverageAnnotationModel disconnected") //$NON-NLS-1$
		
		def removeAnnotationsFrom(document: IDocument) =
			annotations.foreach(annotation => document.removePosition(annotation.getPosition))
	}
	
	override def getAnnotationIterator = JavaConversions.asIterator(annotations.iterator)
	
	override def getPosition(annotation: Annotation) = annotation match {
		case coverageAnnotation: CoverageAnnotation => coverageAnnotation.getPosition
		case _ => null
	}
}
