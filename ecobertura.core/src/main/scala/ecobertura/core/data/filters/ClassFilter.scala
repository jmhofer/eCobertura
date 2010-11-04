/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2010 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package ecobertura.core.data.filters

import scala.util.matching.Regex

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
  private val regexPattern = new Regex("^" + 
      pattern.replaceAll("\\*", ".*").replaceAll("\\?", ".") + "$")
  
  def includes(className: String) = kind match {
    case IncludeFilter => matches(className)
    case ExcludeFilter => !matches(className)
  }
  
  private def matches(className: String) = regexPattern.findFirstIn(className).isDefined

  def toAttributeString = "ClassFilter(%s, %s)".format(kind.asLabel, pattern)
  override def toString = toAttributeString
}

