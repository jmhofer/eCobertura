/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
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
package ecobertura.core.data.filters

sealed abstract class KindOfFilter {
  def asLabel: String
  override def toString = asLabel
}

case object IncludeFilter extends KindOfFilter {
  val asLabel = "include"
}

case object ExcludeFilter extends KindOfFilter {
  val asLabel = "exclude"
}

case class ClassFilter(kind: KindOfFilter, pattern: String) {
  override def toString = "ClassFilter(%s, %s)".format(kind.toString, pattern)
}
