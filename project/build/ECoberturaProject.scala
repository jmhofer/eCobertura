import sbt._
import scala.io
import java.io.File

class ECoberturaProject(info: ProjectInfo) extends ParentProject(info) {
	def eclipseHome = 
		Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))	
	def eclipsePlugins = descendents(eclipseHome, "*.jar")
		
//	override def localScala: Seq[ScalaInstance] = 
//		defineScala("2.8.0-local", new File(eclipseHome + "/configuration/org.eclipse.osgi/bundles/623/1/.cp/lib"))
	
	lazy val core = project("ecobertura.core", "eCobertura Core")
	lazy val ui = project("ecobertura.ui", "eCobertura UI", core)
}
