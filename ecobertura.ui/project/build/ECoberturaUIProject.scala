import sbt._
import scala.io
import java.io._
import java.util.jar.Manifest

class ECoberturaUIProject(info: ProjectInfo) extends DefaultProject(info) {

  // dependencies
  val ecobertura_core = "jmhofer" % "ecobertura-core_2.8.0.Beta1" % "1.0"

  def eclipseHome = Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))
  def eclipsePlugins = descendents(eclipseHome, "*.jar")

  // add jars to "unmanaged" path 
  // just including *all* eclipse plugin jars seems like a bit of a hack,
  // but it works for now...
  // TODO be more specific here
  override def unmanagedClasspath = super.unmanagedClasspath +++ eclipsePlugins +++ 
      (Path.fromFile(new File("../ecobertura.core/lib")) * "*.jar")

  // special eclipse plugin resources path
  override def mainResources = ".options" +++ "build.properties" +++ "plugin.xml" +++ (("src" +++ "OSGI-INF" +++ "lib") ** "*")

  // TODO package needs to include our own manifest instead of the generated one, and also
  // needs a specific resources path
  override def packageOptions = JarManifest(new Manifest(new FileInputStream("META-INF/MANIFEST.MF"))) :: Nil

}
