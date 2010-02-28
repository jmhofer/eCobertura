package ecobertura.ui.views.session

trait CoverageSessionPublisher {
	private var listeners: List[CoverageSessionListener] = Nil

	def addListener(listener: CoverageSessionListener) =
		listeners ::= listener
	
	def removeListener(listener: CoverageSessionListener) = 
		listeners.filterNot(_ == listener)
		
	protected def fireSessionReset = listeners.foreach(_.sessionReset)
}
