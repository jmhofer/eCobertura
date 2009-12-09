package ecobertura.core.state

import java.io.File
import org.eclipse.core.runtime.IPath

object PluginState {
	def initialize(stateLocation: IPath) = new PluginState(stateLocation)
}

class PluginState(stateLocation: IPath) {
	val instrumentationDataDirectory = new File(stateLocation toFile, "cobertura")
	instrumentationDataDirectory mkdirs
	
	def cleanUp = {
		instrumentationDataDirectory.listFiles map (_.delete)
		instrumentationDataDirectory delete
	}
}
