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
    case _ => throw new IllegalArgumentException("invalid filter kind index: " + index)
  }

  def fromString(kindString: String) = kindString match {
    case "include" => IncludeFilter
    case "exclude" => ExcludeFilter
    case _ => throw new IllegalArgumentException("invalid filter kind: " + kindString)
  }
}

sealed abstract class KindOfFilter {
  def toIndex: Int
  def asLabel: String
}

case object IncludeFilter extends KindOfFilter {
  override def toIndex = 0
  override def asLabel = "include"
}

case object ExcludeFilter extends KindOfFilter {
  override def toIndex = 1
  override def asLabel = "exclude"
}
