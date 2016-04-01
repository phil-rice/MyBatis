name := "information-syndication-api"

version := scala.util.Properties.envOrElse("BUILD_VERSION", "dev")

organization := "bbc"

scalaVersion := "2.11.7"


lazy val versions = new {
  val mustache = "0.9.1"
  val apacheCommonsIO = "2.3"
  val apacheCommonsDBCP = "2.1.1"
}

libraryDependencies += "com.github.spullara.mustache.java" % "scala-extensions-2.11" % versions.mustache

libraryDependencies += "commons-io" % "commons-io" %  versions.apacheCommonsIO

libraryDependencies += "org.apache.commons" % "commons-dbcp2" % versions.apacheCommonsDBCP

