package ecobertura.core.cobertura.data

import net.sourceforge.cobertura.coveragedata._

import scala.collection.JavaConversions._

object ClassCoverage {
	def fromCoberturaClassData(classData: ClassData): ClassCoverage = {
		new CoberturaClassData(classData)
	}
}

trait ClassCoverage {
	def name: String
}

class CoberturaClassData(classData: ClassData) extends ClassCoverage {
	override def name = classData.getName
}
