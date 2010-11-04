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
