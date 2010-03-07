/*
 * This file is part of eCobertura.
 * 
 * Copyright (c) 2009, 2010 Joachim Hofer
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
import sbt._
import scala.io
import java.io._
import java.util.jar.Manifest

class ECoberturaCoreProject(info: ProjectInfo) extends DefaultProject(info) {

  def eclipseHome = Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))
  def eclipsePlugins = descendents(eclipseHome, "*.jar")

  // add jars to "unmanaged" path 
  // just including *all* eclipse plugin jars seems like a bit of a hack,
  // but it works for now...
  // TODO be more specific here
  override def unmanagedClasspath = super.unmanagedClasspath +++ eclipsePlugins

  // special eclipse plugin resources path
  override def mainResources = ".options" +++ "build.properties" +++ "plugin.xml" +++ (("src" +++ "OSGI-INF" +++ "lib") ** "*")

  // package needs to include our own manifest instead of the generated one
  override def packageOptions = JarManifest(new Manifest(new FileInputStream("META-INF/MANIFEST.MF"))) :: Nil
}
