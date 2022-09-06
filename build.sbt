ThisBuild / organization := "io.github.marcinzh"
ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "3.2.0"
ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
)

val Deps = {
  object deps {
  }
  deps
}

lazy val root = project
  .in(file("."))
  .settings(name := "yamlike-root")
  .settings(sourcesInBase := false)
  .settings(dontPublishMe: _*)
  .aggregate(yamlike, examples)

lazy val yamlike = project
  .in(file("modules/yamlike"))
  .settings(name := "yamlike")

lazy val examples = project
  .in(file("modules/examples"))
  .settings(name := "examples")
  .settings(scalaVersion := "3.2.1-RC1-bin-20220905-c93098b-NIGHTLY")
  .settings(scalacOptions += "-language:experimental.fewerBraces")
  .dependsOn(yamlike)

//=================================================

lazy val dontPublishMe = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

ThisBuild / watchBeforeCommand := Watch.clearScreen
ThisBuild / watchTriggeredMessage := Watch.clearScreenOnTrigger
ThisBuild / watchForceTriggerOnAnyChange := true

//=================================================

ThisBuild / description := "YAML-like list syntax for Scala 3"
ThisBuild / organizationName := "marcinzh"
ThisBuild / homepage := Some(url("https://github.com/marcinzh/yamlike"))
ThisBuild / scmInfo := Some(ScmInfo(url("https://github.com/marcinzh/yamlike"), "scm:git@github.com:marcinzh/yamlike.git"))
ThisBuild / licenses := List("MIT" -> new URL("http://www.opensource.org/licenses/MIT"))
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishMavenStyle := true
ThisBuild / publishTo := {
  val nexus = "https://s01.oss.sonatype.org/"
  isSnapshot.value match {
    case true => Some("snapshots" at nexus + "content/repositories/snapshots")
    case false => Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
}
ThisBuild / pomExtra := (
  <developers>
    <developer>
      <id>marcinzh</id>
      <name>Marcin Żebrowski</name>
      <url>https://github.com/marcinzh</url>
    </developer>
  </developers>
)
