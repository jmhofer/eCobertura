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

import java.util.logging._

import org.eclipse.jface.text.IDocument
import org.eclipse.jface.text.source._
import org.eclipse.ui.texteditor._

object AnnotationModelAttacher {
	private def logger = Logger getLogger "ecobertura.ui.annotation" //$NON-NLS-1$
	
	def attachTo(editor: ITextEditor) = {
		new AnnotationModelAttacher(editor)
	}
}

class AnnotationModelAttacher(editor: ITextEditor) {
	import AnnotationModelAttacher._
	
	try {
		attachIfNecessary
	} catch {
		case e: NoValidAnnotationModelException =>
			logger.log(Level.WARNING, "unable to attach to editors", e) //$NON-NLS-1$
	}
	
	private def attachIfNecessary = {
		val provider = editor.getDocumentProvider
		if (provider == null) 
			throw new NoValidAnnotationModelException("no document provider found") //$NON-NLS-1$
		
		val model = provider getAnnotationModel editor.getEditorInput
		val modelToAttachTo = model match {
			case modelExt: IAnnotationModelExtension => modelExt
			case _ => throw new NoValidAnnotationModelException(
					"unknown annotation model extension type: " + model.getClass.getName) //$NON-NLS-1$
		}

		if (!alreadyAttached) attach

		def alreadyAttached = 
			modelToAttachTo.getAnnotationModel(CoverageAnnotationModel.MODEL_ID) != null
			
		def attach = {
			val document = provider getDocument editor.getEditorInput
			val modelToAttach = CoverageAnnotationModel.createForEditorDocument(editor, document)
			modelToAttachTo.addAnnotationModel(CoverageAnnotationModel.MODEL_ID, modelToAttach)
			logger fine "CoverageAnnotationModel attached to " + editor.getTitle() //$NON-NLS-1$
		}
	}
}