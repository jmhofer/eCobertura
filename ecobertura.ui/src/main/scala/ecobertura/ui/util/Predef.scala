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
package ecobertura.ui.util

import org.eclipse.jface.text.DocumentEvent
import org.eclipse.jface.text.IDocumentListener
import org.eclipse.jface.viewers._
import org.eclipse.jface.action._
import org.eclipse.swt.events._

object Predef {
  implicit def actionFunction2Action(func: => Unit) = new Action {
    override def run = func
  }

  implicit def selectionListener(func: SelectionEvent => Unit): SelectionListener = new SelectionAdapter {
    override def widgetSelected(event: SelectionEvent) = func(event)
  }

  implicit def selectionChangedListener(func: SelectionChangedEvent => Unit) = new ISelectionChangedListener {
    override def selectionChanged(event: SelectionChangedEvent) = func(event)
  }

  implicit def documentListener(func: => Unit) = new IDocumentListener {
    override def documentChanged(event: DocumentEvent) = func
    override def documentAboutToBeChanged(event: DocumentEvent) = { /* nothing to do */ }
  }

  implicit def menuListener(func: IMenuManager => Unit) = new IMenuListener {
    override def menuAboutToShow(menuManager: IMenuManager) = func(menuManager)
  }

  implicit def doubleClickListener(func: DoubleClickEvent => Unit) = new IDoubleClickListener {
    override def doubleClick(event: DoubleClickEvent) = func(event)
  }

  implicit def run(taskToRun: => Unit) = new Runnable() {
    override def run = taskToRun
  }
}
