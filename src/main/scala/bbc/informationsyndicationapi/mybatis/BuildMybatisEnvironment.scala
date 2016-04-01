package bbc.informationsyndicationapi.mybatis

import java.io.{PrintWriter, IOException, FileOutputStream, File}
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.util

import scala.io.Source

object BuildMybatisEnvironment {
  val targetDirectory = new File("./target")

  def getOutputFile(s: String) = new File(s"target/$s")

  def getInputStream(s: String) = getClass.getClassLoader.getResourceAsStream(s)

  def copyFromClassPathToTarget(s: String) = {
    val inputStream = getInputStream(s)

    val outputFile = getOutputFile(s)
    if (outputFile.exists()) {
      println("deleteing: " + outputFile)
      outputFile.delete()
    }
    outputFile.getParentFile.mkdirs()
    println("From is: " + inputStream + " to " + outputFile)
    Files.copy(inputStream, Paths.get(outputFile.getAbsolutePath))
  }

  def visitFiles(root: String)(fn: String => _) {
    Files.walkFileTree(Paths.get(root), new util.HashSet(), 4, new FileVisitor[Path] {
      def visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE

      def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
        val name = file.toString.drop("src/main/resources/".length)
        println(name)
        fn(name)
        FileVisitResult.CONTINUE
      }

      def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = FileVisitResult.CONTINUE

      def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
    })
  }


  def mustache(template: String, map: Map[String, Any], toName: String): Unit = {
    val s = Mustache(template)(map)
    val targetFileName = getOutputFile(toName)
    targetFileName.getParentFile.mkdirs()
    val pw = new PrintWriter(targetFileName)
    pw.write(s)
    pw.close
  }

  def main(args: Array[String]) {
    val map = Map("driver" -> "theDriver", "username" -> "theUserName", "password" -> "thepassword", "url" -> "theUrl")

    visitFiles("src/main/resources/mybatis")((name: String) => copyFromClassPathToTarget(name))
    mustache("templates/developer.properties.mustache", map, "mybatis/environment/developer.properties")
    //stop the instances
    //get the cosmos config and create the development.properties
    //run the batch file
    //stop the instances
  }

}
