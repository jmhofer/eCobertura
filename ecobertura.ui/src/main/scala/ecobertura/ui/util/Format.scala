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

object Format {
	def asPercentage(numerator: Int, denominator: Int) =
		if (denominator == 0) "-"
		else "%3.2f %%".format(numerator.toDouble / denominator * 100.0) 
}
