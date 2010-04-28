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

import scala.collection.mutable.ArrayBuffer

class ClassFilters extends Iterable[ClassFilter] {
  private val filters = ArrayBuffer[ClassFilter]()
  
  override def iterator = filters.iterator
  
  def addIncludeFilter(pattern: String) = filters += ClassFilter(IncludeFilter, pattern)
  def addExcludeFilter(pattern: String) = filters += ClassFilter(ExcludeFilter, pattern)
  def removeFilterInRow(rowIndex: Int) = filters.remove(rowIndex)

  def setFilterKindInRow(kind: KindOfFilter, rowIndex: Int) = 
    filters(rowIndex) = ClassFilter(kind, filters(rowIndex).pattern)
    
  def setPatternInRow(pattern: String, rowIndex: Int) = 
    filters(rowIndex) = ClassFilter(filters(rowIndex).kind, pattern)
    
  def getFilterInRow(rowIndex: Int) = filters(rowIndex)
}