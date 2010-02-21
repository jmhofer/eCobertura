import sbt._
import scala.io
import java.io.File

class ECoberturaCoreProject(info: ProjectInfo) extends DefaultProject(info) {

  def eclipseHome = Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))
  def eclipsePlugins = descendents(eclipseHome, "*.jar")

  // add jars to "unmanaged" path 
  // just including *all* eclipse plugin jars seems like a bit of a hack,
  // but it works for now...
  override def unmanagedClasspath = super.unmanagedClasspath +++ eclipsePlugins
}
