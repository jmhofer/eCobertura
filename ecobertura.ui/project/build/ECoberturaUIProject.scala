import sbt._
import scala.io
import java.io.File

class ECoberturaUIProject(info: ProjectInfo) extends DefaultProject(info) {

  def eclipseHome = Path.fromFile(new File(System.getenv("ECLIPSE_HOME")))
  def eclipsePlugins = descendents(eclipseHome, "*.jar")

  // add jars to "unmanaged" path 
  // just including *all* eclipse plugin jars seems like a bit of a hack,
  // but it works for now...
  // TODO be more specific here
  override def unmanagedClasspath = super.unmanagedClasspath +++ eclipsePlugins

  // TODO package needs to include our own manifest instead of the generated one, and also
  // needs a specific resources path
  //override def packageOptions = JarManifest(new Manifest(new FileInputStream("src/main/resources/META-INF/MANIFEST.MF"))) :: Nil
}
