package ecobertura.core.results
import ecobertura.core.data.CoverageSession

trait CoverageResultsListener {
	def coverageRunCompleted(coverageSesion: CoverageSession) : Unit
}
