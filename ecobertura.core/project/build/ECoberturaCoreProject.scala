import sbt._
import scala.io
import java.io.File

class ECoberturaCoreProject(info: ProjectInfo) extends DefaultProject(info) {
	def eclipseHome = 
		Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))
		
	def eclipsePlugins = descendents(eclipseHome, "*.jar")
}
