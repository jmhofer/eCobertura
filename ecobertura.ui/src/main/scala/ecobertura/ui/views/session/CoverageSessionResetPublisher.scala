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
package ecobertura.ui.views.session
import ecobertura.core.data.CoverageSession

import java.util.logging.Logger

trait CoverageSessionResetPublisher {
	private val logger = Logger.getLogger("ecobertura.ui.views.session") //$NON-NLS-1$

	private var listeners: List[Option[CoverageSession] => Unit] = Nil

	def addSessionResetListener(listener: Option[CoverageSession] => Unit) =
		listeners ::= listener
	
	def removeSessionResetListener(listener: Option[CoverageSession] => Unit) = 
		listeners.filterNot(_ == listener)
		
	protected def fireSessionReset(coverageSession: Option[CoverageSession]) = {
		logger.fine("coverage session reset...")
		listeners.foreach(_(coverageSession))
	}
}
