package ecobertura.ui.util

object Format {
	def asPercentage(numerator: Int, denominator: Int) =
		if (denominator == 0) "-"
		else String.format("%3.2f %%", double2Double(numerator.toDouble / denominator * 100.0)) 
}
