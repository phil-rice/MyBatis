name := "information-syndication-api"

version := scala.util.Properties.envOrElse("BUILD_VERSION", "dev")

organization := "bbc"

scalaVersion := "2.11.7"


lazy val versions = new {
  val mustache = "0.9.1"
  val apacheCommons = "2.3"
}

libraryDependencies += "com.github.spullara.mustache.java" % "scala-extensions-2.11" % versions.mustache

libraryDependencies += "commons-io" % "commons-io" %  versions.apacheCommons
