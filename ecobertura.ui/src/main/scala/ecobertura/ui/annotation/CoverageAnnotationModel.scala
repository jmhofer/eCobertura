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
	
	logger.fine("CoverageAnnotationModel created.") //$NON-NLS-1$
	initializeAnnotations(editor, document)
	
	document.addDocumentListener {
		removeAllAnnotations
	}
	
	CoverageSessionModel.get.addSessionResetListener(_ => {
		logger.fine("session has been reset...")
		removeAllAnnotations
		initializeAnnotations(editor, document)
	})
	
	private def initializeAnnotations(editor: ITextEditor, document: IDocument) = {
		if (!editor.isDirty) { 
			CoverageSessionModel.get.currentCoverageSession match {
				case Some(session) => {
					logger.fine("CoverageSession active") /* session active */
					val coveredLines = LineCoverageFinder.forSession(session).findInEditor(editor)
					annotateLines(coveredLines)
				}
				case _ => /* nothing to do */
			}
		}

		def annotateLines(lines: List[LineCoverage]) = {
			for (line <- lines; lineNumber = line.lineNumber - 1) {
				if (document.getLineLength(lineNumber) > 0) {
					val offset = document.getLineOffset(lineNumber)
					val length = document.getLineLength(lineNumber)
					val annotation = 
						if (line.isCovered) CoverageAnnotation.coveredAtPosition(offset, length)
						else CoverageAnnotation.notCoveredAtPosition(offset, length)
						
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
