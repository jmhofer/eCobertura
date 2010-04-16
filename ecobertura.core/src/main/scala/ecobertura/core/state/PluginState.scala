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
package ecobertura.core.state

import java.io.File
import org.eclipse.core.runtime.IPath

object PluginState {
	def initialize(stateLocation: IPath) = new PluginState(stateLocation)
}

class PluginState(stateLocation: IPath) {
	val instrumentationDataDirectory = new File(stateLocation.toFile, "cobertura")
	val instrumentedClassesDirectory = new File(stateLocation.toFile, "bin")
	
	instrumentationDataDirectory.mkdirs
	instrumentedClassesDirectory.mkdirs
	
	def cleanUp = {
		deleteRecursively(instrumentationDataDirectory)
		deleteRecursively(instrumentedClassesDirectory)
	}
	
	private def deleteRecursively(file: File) : Unit = {
		if (file.isFile) file.delete
		else {
			file.listFiles.foreach (deleteRecursively(_))
			file.delete
		}
	}
}
