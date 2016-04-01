package bbc.informationsyndicationapi.mybatis

import java._

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.DefaultMustacheFactory
import org.apache.commons.io.output.StringBuilderWriter
import org.apache.commons.io.output.StringBuilderWriter

object Mustache {
  val mf = new DefaultMustacheFactory()

  def toJavaTypes(s: Any): Any = s match {
    case m: Map[_, _] => m.foldLeft(new util.HashMap[String, Any]) { case (acc, (k: String, v)) =>
      acc.put(k, toJavaTypes(v))
      acc
    }
    case l: Seq[_] => l.foldLeft(new util.ArrayList[Any]) { (acc, v) =>
      acc.add(toJavaTypes(v))
      acc
    }
    case _ => s
  }

  def apply(template: String)(someData: Any) = {
    val m = mf.compile(template)
    val writer = new StringBuilderWriter()
    m.execute(writer, toJavaTypes(someData))
    writer.getBuilder.toString
  }
}
