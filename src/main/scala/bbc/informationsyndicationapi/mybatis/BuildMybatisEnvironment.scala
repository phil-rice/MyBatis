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
    println("From is: " + s + " to " + outputFile)
    Files.copy(inputStream, Paths.get(outputFile.getAbsolutePath))
  }



  class OurVisitor(prefixToIgnoreLength: Int)(fn: String => _) extends  FileVisitor[Path] {
    def visitFileFailed(file: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE

    def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
      val name = file.toString.drop(prefixToIgnoreLength)
      println(name)
      fn(name)
      FileVisitResult.CONTINUE
    }

    def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = FileVisitResult.CONTINUE

    def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = FileVisitResult.CONTINUE
  }

  def visitFiles(prefixToIgnoreLength: Int)(root: String)(fn: String => _) {
    Files.walkFileTree(Paths.get(root), new util.HashSet(), 4, new OurVisitor(prefixToIgnoreLength)(fn))
  }


  def mustache(template: String, map: Map[String, Any], toName: String): Unit = {
    val s = Mustache(template)(map)
    val targetFileName = getOutputFile(toName)
    targetFileName.getParentFile.mkdirs()
    val pw = new PrintWriter(targetFileName)
    pw.write(s)
    pw.close
  }

  def copyManyFilesFromClassPath: Unit ={
    copyFromClassPathToTarget("mybatis/bin/migrate")
    copyFromClassPathToTarget("mybatis/drivers/h2-1.4.191.jar")
    copyFromClassPathToTarget("mybatis/drivers/mysql-connector-java-5.1.38-bin.jar")
    copyFromClassPathToTarget("mybatis/lib/mybatis-3.2.4.jar")
    copyFromClassPathToTarget("mybatis/lib/mybatis-migrations-3.2.0.jar")
    copyFromClassPathToTarget("mybatis/scripts/20160331132054_create_changelog.sql")
    copyFromClassPathToTarget("mybatis/scripts/20160331132055_first_migration.sql")
    copyFromClassPathToTarget("mybatis/migrateStuff.sh")
  }


  def main(args: Array[String]) {
    val map = Map("driver" -> "org.h2.Driver", "username" -> "sa", "password" -> "", "url" -> "jdbc:h2:~/test")

//    visitFiles("src/main/resources/".length)("src/main/resources/mybatis")(copyFromClassPathToTarget _)
    copyManyFilesFromClassPath
    println("==============")
    mustache("templates/developer.properties.mustache", map, "mybatis/environments/development.properties")
    Runtime.getRuntime().exec("chmod u+x target/mybatis/migrateStuff.sh")
    Runtime.getRuntime().exec("target/mybatis/migrateStuff.sh")
    //stop the instances
    //get the cosmos config and create the development.properties
    //run the batch file
    //stop the instances
  }

}
