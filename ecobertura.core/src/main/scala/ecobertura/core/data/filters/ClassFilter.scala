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

object KindOfFilter {
  def fromIndex(index: Int) = index match {
    case 0 => IncludeFilter
    case 1 => ExcludeFilter
  }
  def fromString(kindString: String) = kindString match {
    case "include" => IncludeFilter
    case "exclude" => ExcludeFilter
  }
}

sealed abstract class KindOfFilter {
  def asLabel: String
  def toIndex: Int
  override def toString = asLabel
}

case object IncludeFilter extends KindOfFilter {
  val asLabel = "include"
  val toIndex = 0
}

case object ExcludeFilter extends KindOfFilter {
  val asLabel = "exclude"
  val toIndex = 1
}

object ClassFilter {
  val ClassFilterPattern = """ClassFilter\(([^,]*), ([^\)]*)\)""".r
  
  def apply(classFilterString: String): ClassFilter = classFilterString match {
    case ClassFilterPattern(kindString, patternString) =>
        new ClassFilter(KindOfFilter.fromString(kindString), patternString)
  }
}

case class ClassFilter(var kind: KindOfFilter, var pattern: String) {
  override def toString = "ClassFilter(%s, %s)".format(kind.toString, pattern)
}
