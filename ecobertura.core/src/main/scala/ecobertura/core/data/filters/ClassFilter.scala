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

object ClassFilter {
  val ClassFilterPattern = """ClassFilter\(([^,]*), ([^\)]*)\)""".r
  
  def apply(classFilterString: String): ClassFilter = classFilterString match {
    case ClassFilterPattern(kindString, patternString) =>
        ClassFilter(KindOfFilter.fromString(kindString), patternString)
    case _ => throw new IllegalArgumentException("invalid filter attribute string: " + 
        classFilterString)
  }
}

/**
 * This is mutable so that it can be used with the Eclipse JFace table model.
 */
case class ClassFilter(var kind: KindOfFilter, var pattern: String) {
  def toAttributeString = "ClassFilter(%s, %s)".format(kind.asLabel, pattern)
  override def toString = toAttributeString
}

