package ecobertura.ui.views.session

import java.util.logging.Logger

trait CoverageSessionPublisher {
	private val logger = Logger.getLogger("ecobertura.ui.views.session") //$NON-NLS-1$

	private var listeners: List[CoverageSessionListener] = Nil

	def addListener(listener: CoverageSessionListener) =
		listeners ::= listener
	
	def removeListener(listener: CoverageSessionListener) = 
		listeners.filterNot(_ == listener)
		
	protected def fireSessionReset = {
		logger.fine("Building from coverage session...")
		listeners.foreach(_.sessionReset)
	}
}
