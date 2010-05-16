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

import scala.collection.mutable.ListBuffer
import scala.collection.JavaConversions._

import java.util.{ArrayList, List => JavaList}

import org.eclipse.debug.core._

object ClassFilters {
  def apply(launchConfiguration: ILaunchConfiguration) : ClassFilters = {
    val classFilterList = launchConfiguration.getAttribute("classFilters", 
        new ArrayList[String])
        
    ClassFilters((
        for (filterString <- classFilterList.asInstanceOf[JavaList[String]])
        yield ClassFilter(filterString)) : _*)
  }

  def apply(classFilterArray: ClassFilter*) : ClassFilters = {
    val classFilters = new ClassFilters
    for (classFilter <- classFilterArray) classFilters.add(classFilter)
    classFilters
  }
}

class ClassFilters {
  private var classFilters = ListBuffer[ClassFilter]()
  
  def toArray = classFilters.toArray
  
  def add(classFilter: ClassFilter) = classFilters += classFilter
  def remove(classFilter: ClassFilter) = classFilters -= classFilter
  
  def addToLaunchConfiguration(launchConfiguration: ILaunchConfigurationWorkingCopy) = {

    val javaList: JavaList[String] = new ArrayList[String]
    for (filter <- classFilters) javaList.add(filter.toAttributeString)
    launchConfiguration.setAttribute("classFilters", javaList)
  }
  
  override def toString = "ClassFilters(%s)".format(classFilters.toString)
}